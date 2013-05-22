package com.amnesty.panicbutton.alert;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import com.amnesty.panicbutton.ApplicationSettings;
import com.amnesty.panicbutton.location.CurrentLocationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLocationManager;
import org.robolectric.shadows.ShadowVibrator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class PanicAlertTest {
    private PanicAlert panicAlert;

    private Application context;
    private ShadowVibrator shadowVibrator;
    @Mock private ExecutorService mockExecutor;
    @Mock private PanicMessage mockPanicMessage;
    @Mock private CurrentLocationProvider mockCurrentLocationProvider;
    @Mock private Location mockLocation;
    private ShadowLocationManager shadowLocationManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        context = Robolectric.application;

        panicAlert = getPanicAlert(mockExecutor);
        shadowVibrator = shadowOf((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
        ApplicationSettings.setAlertActive(context, false);
        shadowLocationManager = shadowOf((LocationManager)context.getSystemService(Context.LOCATION_SERVICE));
        shadowLocationManager.setProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
        shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, true);
    }

    @Test
    public void shouldActiveTheAlertWithHapticFeedback() throws IllegalAccessException {
        panicAlert.activate();
        assertEquals(3000, shadowVibrator.getMilliseconds());
        assertTrue(panicAlert.isActive());
        verify(mockExecutor).execute(any(Runnable.class));
    }

    @Test
    public void shouldNotActiveAgainIfItIsAlreadyActive() throws IllegalAccessException {
        ApplicationSettings.setAlertActive(context, true);
        panicAlert.activate();
        assertEquals(3000, shadowVibrator.getMilliseconds());
        verifyZeroInteractions(mockExecutor);
    }

    @Test
    public void shouldReturnActiveAlertStatus() throws Exception {
        ApplicationSettings.setAlertActive(context, true);
        assertEquals(AlertStatus.ACTIVE, panicAlert.getAlertStatus());
    }

    @Test
    public void shouldReturnStandByAlertStatus() throws Exception {
        assertEquals(AlertStatus.STANDBY, panicAlert.getAlertStatus());
    }

    @Test
    public void shouldSendTheFirstAlertOnActivation() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(mockLocation);

        panicAlert.activate();

        verify(mockPanicMessage).send(mockLocation);
    }

    @Test
    public void shouldSendAlertWithOutLocationIfLocationNotAvaiable() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(null);

        panicAlert.activate();

        verify(mockPanicMessage).send(null);
    }

    @Test
    public void shouldDeactivateTheAlert() {
        panicAlert = getPanicAlert(new TestExecutorService());
        when(mockCurrentLocationProvider.getLocation()).thenReturn(mockLocation);
        panicAlert.activate();
        panicAlert.deActivate();

        assertFalse(ApplicationSettings.isAlertActive(context));
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
