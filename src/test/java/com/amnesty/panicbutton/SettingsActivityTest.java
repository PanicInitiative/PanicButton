package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import com.amnesty.panicbutton.alert.PanicAlert;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static java.util.Arrays.asList;
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
    private PanicAlert mockPanicAlert;
    private TextView alertStatusText;

    @Before
    public void setup() {
        initMocks(this);

        settingsActivity = new SettingsActivity() {
            PanicAlert getPanicAlert() {
                return mockPanicAlert;
            }
        };
        settingsActivity.onCreate(null);

        smsRow = (TableRow) settingsActivity.findViewById(R.id.sms_row);
        activateButton = (Button) settingsActivity.findViewById(R.id.activate_alert);
        alertStatusText = (TextView) settingsActivity.findViewById(R.id.alert_status_text);
    }

    @Test
    public void shouldStartTheSMSConfigActivityOnSmsRowClick() throws Exception {
        smsRow.performClick();

        ShadowActivity shadowActivity = shadowOf(settingsActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(SMSSettingsActivity.class.getName(), startedIntent.getComponent().getClassName());
    }

    @Test
    public void shouldDisableActivationButtonWhenSettingsNotConfigured() {
        settingsActivity.onResume();

        assertFalse(activateButton.isEnabled());
        assertEquals("Alert cannot be sent. please choose contacts", alertStatusText.getText().toString());
    }

    @Test
    public void shouldEnableActivationButtonWhenSettingsConfigured() {
        SMSSettings.save(application, new SMSSettings(asList("123-123-1222"), ""));
        settingsActivity.onResume();

        assertTrue(activateButton.isEnabled());
        assertEquals("Alert is in standby", alertStatusText.getText().toString());
    }

    @Test
    public void shouldActivateAlertOnActivation() {
        activateButton.performClick();
        verify(mockPanicAlert).start();
    }

    @Test
    public void shouldReturnNewPanicAlert() {
        assertNotNull(new SettingsActivity().getPanicAlert());
    }
}