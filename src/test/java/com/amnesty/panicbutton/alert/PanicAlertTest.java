package com.amnesty.panicbutton.alert;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Vibrator;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.location.LocationTestUtil;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;
import com.amnesty.panicbutton.twitter.ShortCodeSettings;
import com.amnesty.panicbutton.twitter.TwitterSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowVibrator;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static org.codehaus.plexus.util.ReflectionUtils.getValueIncludingSuperclasses;
import static org.codehaus.plexus.util.ReflectionUtils.setVariableValueInObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class PanicAlertTest {
    private PanicAlert panicAlert;

    @Mock
    public SMSAdapter mockSMSAdapter;
    @Mock
    public LocationProvider mockLocationProvider;
    @Mock
    ScheduledExecutorService mockExecutor;

    private String message;
    private String mobile1, mobile2, mobile3;
    private Location location;
    private double latitude, longitude;
    private Application context;
    private ShadowVibrator shadowVibrator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        message = "Help! I am in trouble";
        mobile1 = "123-123-1222";
        mobile2 = "";
        mobile3 = "6786786789";
        latitude = -183.123456;
        longitude = 78.654321;

        location = LocationTestUtil.location(NETWORK_PROVIDER, latitude, longitude, currentTimeMillis(), 10.0f);
        shadowVibrator = shadowOf((Vibrator) application.getSystemService(Context.VIBRATOR_SERVICE));

        context = Robolectric.application;
        panicAlert = spy(PanicAlert.getInstance(context));
        when(panicAlert.getSMSAdapter()).thenReturn(mockSMSAdapter);
        setVariableValueInObject(panicAlert, "executorService", mockExecutor);
        setVariableValueInObject(panicAlert, "locationProvider", mockLocationProvider);
    }

    @After
    public void afterTest() {
        panicAlert.deActivate();
    }

    @Test
    public void shouldSendSMSWithLocationToAllConfiguredPhoneNumbersIgnoringInValidNumbers() {
        SMSSettings smsSettings = new SMSSettings(asList(mobile1, mobile2, mobile3), message);
        SMSSettings.save(application, smsSettings);

        when(mockLocationProvider.currentBestLocation()).thenReturn(location);

        panicAlert.activateAlert();

        String messageWithLocation = message + ". I'm at http://maps.google.com/maps?q=" + latitude + "," + longitude + " via network";
        verify(mockSMSAdapter).sendSMS(mobile1, messageWithLocation);
        verify(mockSMSAdapter).sendSMS(mobile3, messageWithLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldNotSendSMSIfSettingsNotConfigured() {
        panicAlert.activateAlert();
        verifyZeroInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldSendSMSWithOutLocationToAllConfiguredPhoneNumbersIfTheLocationIsNotAvailable() {
        when(mockLocationProvider.currentBestLocation()).thenReturn(null);
        SMSSettings.save(application, new SMSSettings(asList(mobile1, mobile2, mobile3), message));

        panicAlert.activateAlert();

        verify(mockSMSAdapter).sendSMS(mobile1, message);
        verify(mockSMSAdapter).sendSMS(mobile3, message);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldSendTwitterSMSIfEnabled() {
        String tweet = "Test Message";
        ShortCodeSettings shortCodeSettings = new ShortCodeSettings("India", "Airtel", "53000");
        TwitterSettings twitterSettings = new TwitterSettings(shortCodeSettings, tweet);
        TwitterSettings.enable(context);
        TwitterSettings.save(context, twitterSettings);

        panicAlert.activateAlert();

        verify(mockSMSAdapter).sendSMS("53000", tweet);
    }

    @Test
    public void shouldActiveTheAlertWithHapticFeedback() throws IllegalAccessException {
        setVariableValueInObject(panicAlert, "executorService", new TestExecutorService());
        panicAlert.activate();
        assertEquals(3000, shadowVibrator.getMilliseconds());
        assertTrue((Boolean) getValueIncludingSuperclasses("isActive", panicAlert));
    }

    @Test
    public void shouldScheduleTheAlertAtFixedRate() throws IllegalAccessException {
        panicAlert.activate();
        verify(mockExecutor).scheduleAtFixedRate(any(Runnable.class), eq(0L), eq(300L), eq(TimeUnit.SECONDS));
    }

    @Test
    public void shouldNotActiveAgainIfItIsAlreadyActive() throws IllegalAccessException {
        setVariableValueInObject(panicAlert, "isActive", true);
        panicAlert.activate();
        assertEquals(0, shadowVibrator.getMilliseconds());
        verifyZeroInteractions(mockExecutor);
    }

    private class TestExecutorService extends ScheduledThreadPoolExecutor {
        public TestExecutorService() {
            super(1);
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long l, long l2, TimeUnit timeUnit) {
            runnable.run();
            return null;
        }
    }
}
