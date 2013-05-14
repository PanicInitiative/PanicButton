package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import com.amnesty.panicbutton.alert.PanicAlert;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import com.amnesty.panicbutton.twitter.TwitterSettingsActivity;
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
    private TableRow twitterRow;

    private Button activateButton;
    @Mock
    private PanicAlert mockPanicAlert;
    private TextView alertStatusText;
    private Button backButton;

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
        twitterRow = (TableRow) settingsActivity.findViewById(R.id.twitter_row);
        activateButton = (Button) settingsActivity.findViewById(R.id.activate_alert);
        backButton = (Button) settingsActivity.findViewById(R.id.settings_back_button);
        alertStatusText = (TextView) settingsActivity.findViewById(R.id.alert_status_text);
    }

    @Test
    public void shouldStartSMSSettingsActivityOnSmsClick() throws Exception {
        smsRow.performClick();
        assertNextStartedActivity(SMSSettingsActivity.class);
    }

    @Test
    public void shouldStartTwitterSettingsActivityOnTwitterClick() throws Exception {
        twitterRow.performClick();
        assertNextStartedActivity(TwitterSettingsActivity.class);
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
    public void shouldGoBackToFacadeOnClickingBack() {
        backButton.performClick();
        assertTrue(shadowOf(settingsActivity).isFinishing());
    }

    @Test
    public void shouldReturnNewPanicAlert() {
        assertNotNull(new SettingsActivity().getPanicAlert());
    }

    private void assertNextStartedActivity(Class expectedActivity) {
        ShadowActivity shadowActivity = shadowOf(settingsActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertEquals(expectedActivity.getName(), startedIntent.getComponent().getClassName());
    }
}