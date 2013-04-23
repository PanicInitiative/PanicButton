package com.amnesty.panicbutton;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Vibrator;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.location.LocationTestUtil;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowVibrator;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MessageAlerterTest {
    private MessageAlerter messageAlerter;

    @Mock
    public SMSAdapter mockSMSAdapter;

    @Mock
    public LocationProvider mockLocationProvider;

    private String message;
    private String mobile1, mobile2, mobile3;
    private Location location;
    private double latitude, longitude;
    private Application context;
    private ShadowVibrator shadowVibrator;

    @Before
    public void setUp() throws InterruptedException {
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
        messageAlerter = new MessageAlerter(context) {
            SMSAdapter getSMSAdapter() {
                return mockSMSAdapter;
            }

            LocationProvider getLocationProvider() {
                return mockLocationProvider;
            }
        };
    }

    @Test
    public void shouldSendSMSWithLocationToAllConfiguredPhoneNumbersIgnoringInValidNumbersWithHapticFeedback() {
        SMSSettings smsSettings = new SMSSettings(asList(mobile1, mobile2, mobile3), message);
        SMSSettings.save(application, smsSettings);

        when(mockLocationProvider.currentBestLocation()).thenReturn(location);

        messageAlerter.run();

        String messageWithLocation = message + ". I'm at http://maps.google.com/maps?q=" + latitude + "," + longitude + " via network";
        assertEquals(3000, shadowVibrator.getMilliseconds());
        verify(mockSMSAdapter).sendSMS(mobile1, messageWithLocation);
        verify(mockSMSAdapter).sendSMS(mobile3, messageWithLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldSendSMSWithOutLocationToAllConfiguredPhoneNumbersIfTheLocationIsNotAvailableWithHapticFeedback() {
        when(mockLocationProvider.currentBestLocation()).thenReturn(null);
        SMSSettings.save(application, new SMSSettings(asList(mobile1, mobile2, mobile3), message));

        messageAlerter.run();

        assertEquals(3000, shadowVibrator.getMilliseconds());
        verify(mockSMSAdapter).sendSMS(mobile1, message);
        verify(mockSMSAdapter).sendSMS(mobile3, message);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldReturnSMSAdapter() {
        assertNotNull(new MessageAlerter(context).getSMSAdapter());
    }

    @Test
    public void shouldReturnLocationProvider() {
        assertNotNull(new MessageAlerter(context).getLocationProvider());
    }
}
