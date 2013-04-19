package com.amnesty.panicbutton;

import android.content.Intent;
import android.location.Location;
import android.widget.Button;
import android.widget.TableRow;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.location.LocationTestUtil;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import java.util.List;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {
    private SettingsActivity settingsActivity;
    private TableRow smsRow;
    private Button activateButton;

    @Mock
    public SMSAdapter mockSMSAdapter;
    @Mock
    public LocationProvider mockLocationProvider;

    @Before
    public void setup() {
        initMocks(this);

        settingsActivity = new SettingsActivity() {
            SMSAdapter getSMSAdapter() {
                return mockSMSAdapter;
            }
            LocationProvider startLocationProviderInBackground() {
                return mockLocationProvider;
            }
        };
        settingsActivity.onCreate(null);

        smsRow = (TableRow) settingsActivity.findViewById(R.id.sms_row);
        activateButton = (Button) settingsActivity.findViewById(R.id.activate_alert);
    }

    @Test
    public void shouldStartTheSMSConfigActivityOnSmsRowClick() throws Exception {
        smsRow.performClick();

        ShadowActivity shadowActivity = shadowOf(settingsActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertThat(startedIntent.getComponent().getClassName(), equalTo(SMSSettingsActivity.class.getName()));
    }

    @Test
    public void shouldDisplayActivationButtonGrayedWhenSettingsNotConfigured() {
        settingsActivity.onResume();
        assertFalse(activateButton.isEnabled());
    }

    @Test
    public void shouldDisplayActivationButtonClickableWhenSettingsConfigured() {
        SMSSettings.save(application, new SMSSettings(asList("123-123-1222"), ""));
        settingsActivity.onResume();
        assertTrue(activateButton.isEnabled());
    }

    @Test
    public void shouldSendSMSWithLocationToAllConfiguredPhoneNumbersIgnoringInValidNumbers() {
        double latitude = -183.123456;
        double longitude = 78.654321;
        Location location = LocationTestUtil.location(NETWORK_PROVIDER, latitude, longitude, currentTimeMillis(), 10.0f);
        when(mockLocationProvider.currentBestLocation()).thenReturn(location);

        String message = "Help! I am in trouble";
        String messageWithLocation = message + ". I'm at http://maps.google.com/maps?q=" + latitude + "," + longitude +" via network";
        String mobile1 = "123-123-1222";
        String mobile2 = "";
        String mobile3 = "6786786789";
        List<String> phoneNumbers = asList(mobile1, mobile2, mobile3);
        SMSSettings.save(application, new SMSSettings(phoneNumbers, message));

        activateButton.performClick();

        verify(mockSMSAdapter).sendSMS(mobile1, messageWithLocation);
        verify(mockSMSAdapter).sendSMS(mobile3, messageWithLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldSendSMSWithOutLocationToAllConfiguredPhoneNumbersIfTheLocationIsNotAvailable() {
        when(mockLocationProvider.currentBestLocation()).thenReturn(null);

        String message = "Help! I am in trouble";
        String mobile1 = "123-123-1222";
        String mobile2 = "";
        String mobile3 = "6786786789";
        List<String> phoneNumbers = asList(mobile1, mobile2, mobile3);
        SMSSettings.save(application, new SMSSettings(phoneNumbers, message));

        activateButton.performClick();

        verify(mockSMSAdapter).sendSMS(mobile1, message);
        verify(mockSMSAdapter).sendSMS(mobile3, message);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldReturnSMSAdapter() {
        SettingsActivity settingsActivity = new SettingsActivity();
        assertNotNull(settingsActivity.getSMSAdapter());
    }

    @Test
    public void shouldReturnLocationProvider() {
        SettingsActivity settingsActivity = new SettingsActivity();
        assertNotNull(settingsActivity.startLocationProviderInBackground());
    }
}
