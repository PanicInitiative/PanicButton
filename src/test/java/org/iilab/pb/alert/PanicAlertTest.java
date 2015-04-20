package org.iilab.pb.alert;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.location.CurrentLocationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLocationManager;
import org.robolectric.shadows.ShadowVibrator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class PanicAlertTest {
    private PanicAlert panicAlert;

    private Application context;
    private ShadowVibrator shadowVibrator;
    @Mock
    private ExecutorService mockExecutor;
    @Mock
    private PanicMessage mockPanicMessage;
    @Mock
    private CurrentLocationProvider mockCurrentLocationProvider;
    @Mock
    private Location mockLocation;
    private ShadowLocationManager shadowLocationManager;
    private ShadowAlarmManager shadowAlarmManager;
    @Mock
    private PackageManager mockPackageManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        context = Robolectric.application;
        setupPackageManager();

        panicAlert = getPanicAlert(mockExecutor);
        shadowVibrator = shadowOf((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
        ApplicationSettings.setAlertActive(context, false);
        shadowLocationManager = shadowOf((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        shadowAlarmManager = shadowOf((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));

        shadowLocationManager.setProviderEnabled(NETWORK_PROVIDER, true);
        shadowLocationManager.setProviderEnabled(GPS_PROVIDER, true);
    }

    private void setupPackageManager() {
        ShadowApplication shadowApplication = shadowOf(Robolectric.application);
        shadowApplication.setPackageManager(mockPackageManager);
        List<ResolveInfo> activities = new ArrayList<ResolveInfo>();
        activities.add(resolveInfo("com.test.package.Class1"));
        activities.add(resolveInfo("com.test.package.Class2"));
        when(mockPackageManager.queryIntentActivities(any(Intent.class), anyInt())).thenReturn(activities);
    }

    private ResolveInfo resolveInfo(String packageName) {
        ResolveInfo resolveInfo = new ResolveInfo();
        ActivityInfo activityInfo = new ActivityInfo();
        activityInfo.packageName = packageName;
        resolveInfo.activityInfo = activityInfo;
        return resolveInfo;
    }

    @Test
    public void shouldActiveTheAlertWithHapticFeedback() throws IllegalAccessException {
        panicAlert.activate();
        assertEquals(500, shadowVibrator.getMilliseconds());
        assertTrue(panicAlert.isActive());
        verify(mockExecutor).execute(any(Runnable.class));

        Intent startedIntent = shadowOf(context).peekNextStartedActivity();
        assertEquals(startedIntent.getPackage(), "com.test.package.Class1");
    }

    @Test
    public void shouldNotActiveAgainIfItIsAlreadyActive() throws IllegalAccessException {
        ApplicationSettings.setAlertActive(context, true);
        panicAlert.activate();
        assertEquals(500, shadowVibrator.getMilliseconds());
        verifyZeroInteractions(mockExecutor);
    }

    @Test
    public void shouldReturnActiveAlertStatus() throws Exception {
        ApplicationSettings.setAlertActive(context, true);
//        assertEquals(AlertStatus.ACTIVE, panicAlert.isActive());
        assertEquals(true, panicAlert.isActive());
    }

    @Test
    public void shouldReturnStandByAlertStatus() throws Exception {
//        assertEquals(AlertStatus.STANDBY, panicAlert.isActive());
        assertEquals(false, panicAlert.isActive());
    }

    @Test
    public void shouldSendTheFirstAlertOnActivation() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(mockLocation);

        panicAlert.activate();

        verify(mockPanicMessage).sendAlertMessage(mockLocation);
    }

    @Test
    public void shouldSendAlertWithOutLocationIfLocationNotAvaiable() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(null);

        panicAlert.activate();

        verify(mockPanicMessage).sendAlertMessage(null);
    }

    @Test
    public void shouldScheduleForContinuousAlertOnActivationAndGoToHomeScreen() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(mockLocation);

        panicAlert.activate();

        Map<PendingIntent, String> requestLocationUpdates = shadowLocationManager.getRequestLocationUdpateProviderPendingIntents();
        List<ShadowAlarmManager.ScheduledAlarm> scheduledAlarms = shadowAlarmManager.getScheduledAlarms();
        ShadowAlarmManager.ScheduledAlarm alarm = scheduledAlarms.get(0);

        assertEquals(2, requestLocationUpdates.size());
        assertTrue(requestLocationUpdates.values().containsAll(Arrays.asList(NETWORK_PROVIDER, GPS_PROVIDER)));

        assertEquals(1, scheduledAlarms.size());
        assertEquals(AppConstants.DEFAULT_ALARM_INTERVAL * 1000 * 60, alarm.interval);
        assertEquals(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarm.type);
        Intent startedIntent = shadowOf(context).peekNextStartedActivity();
        List<String> categories = new ArrayList<String>(startedIntent.getCategories());
        assertEquals(categories.get(0), Intent.CATEGORY_HOME);
        assertEquals(startedIntent.getPackage(), "com.test.package.Class1");
    }

    @Test
    public void shouldDeactivateTheAlert() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(mockLocation);
        panicAlert.activate();
        panicAlert.deActivate();

        List<ShadowAlarmManager.ScheduledAlarm> scheduledAlarms = shadowAlarmManager.getScheduledAlarms();

        assertFalse(ApplicationSettings.isAlertActive(context));
        assertEquals(0, scheduledAlarms.size());
    }

    private class TestExecutorService extends ScheduledThreadPoolExecutor {

        public TestExecutorService() {
            super(1);
        }

        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }

    }

    private PanicAlert getPanicAlert(final ExecutorService executorService) {
        return new PanicAlert(context) {
            ExecutorService getExecutorService() {
                return executorService;
            }

            PanicMessage createPanicMessage() {
                return mockPanicMessage;
            }

            CurrentLocationProvider getCurrentLocationProvider() {
                return mockCurrentLocationProvider;
            }
        };
    }
}
