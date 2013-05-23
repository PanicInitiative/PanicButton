package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import com.amnesty.panicbutton.alert.AlertStatus;
import com.amnesty.panicbutton.alert.PanicAlert;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import com.amnesty.panicbutton.twitter.TwitterSettingsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {
    private SettingsActivity settingsActivity;
    private TextView smsRow;
    private TextView twitterRow;

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

        smsRow = (TextView) settingsActivity.findViewById(R.id.sms_row);
        twitterRow = (TextView) settingsActivity.findViewById(R.id.twitter_row);
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
    public void shouldShowStandbyInfoWhenAlertIsNotActive() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.STANDBY);
        settingsActivity.onResume();
        assertEquals("Alert is in standby", alertStatusText.getText().toString());
    }

    @Test
    public void shouldShowStopAlertInfoWhenAlertIsActive() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.ACTIVE);
        settingsActivity.onResume();
        assertEquals("Alert is active and Sending every five minutes", alertStatusText.getText().toString());
    }

    @Test
    public void shouldDisableLinksWhenAlertIsActive() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.STANDBY);
        settingsActivity.onResume();

        assertTrue(smsRow.isEnabled());
        assertTrue(twitterRow.isEnabled());
    }

    @Test
    public void shouldEnableLinksWhenAlertIsNotActive() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.ACTIVE);
        settingsActivity.onResume();

        assertFalse(smsRow.isEnabled());
        assertFalse(twitterRow.isEnabled());
    }

    @Test
    public void shouldActivateAlert() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.ACTIVE);
        when(mockPanicAlert.isActive()).thenReturn(false);
        activateButton.performClick();
        verify(mockPanicAlert).activate();
    }

    @Test
    public void shouldDeActivateAlert() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.STANDBY);
        when(mockPanicAlert.isActive()).thenReturn(true);
        activateButton.performClick();
        verify(mockPanicAlert).deActivate();
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