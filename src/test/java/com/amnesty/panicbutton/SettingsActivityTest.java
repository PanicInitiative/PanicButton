package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import android.widget.TableRow;
import com.amnesty.panicbutton.alert.MessageAlerter;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {
    private SettingsActivity settingsActivity;
    private TableRow smsRow;
    private Button activateButton;

    @Mock
    private MessageAlerter mockMessageAlerter;

    @Before
    public void setup() {
        initMocks(this);

        settingsActivity = new SettingsActivity() {
            MessageAlerter getMessageAlerter() {
                return mockMessageAlerter;
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
    public void shouldActivateAlertOnActivation() {
        activateButton.performClick();
        verify(mockMessageAlerter).start();
    }

    @Test
    public void shouldReturnNewMessageAlerter() {
        assertNotNull(settingsActivity.getMessageAlerter());
    }
}