package com.apb.beacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowView;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.apb.beacon.alert.AlertStatus;
import com.apb.beacon.alert.PanicAlert;
import com.apb.beacon.sms.SMSSettingsActivity;
import com.apb.beacon.twitter.TwitterSettingsActivity;

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
        settingsActivity.onPostCreate(null);

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

        assertAlertStatusStripColor(R.color.active_color);
    }

    @Test
    public void shouldDeActivateAlert() {
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.STANDBY);
        when(mockPanicAlert.isActive()).thenReturn(true);
        activateButton.performClick();
        verify(mockPanicAlert).deActivate();

        assertAlertStatusStripColor(R.color.standby_color);
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

    private void assertAlertStatusStripColor(int color) {
        ShadowView alertStatusStrip = shadowOf(settingsActivity.findViewById(R.id.alert_status_strip));
        assertThat(alertStatusStrip.getBackgroundColor(), Is.is(Robolectric.application.getResources().getColor(color)));
    }

    private void assertNextStartedActivity(Class expectedActivity) {
        ShadowActivity shadowActivity = shadowOf(settingsActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertEquals(expectedActivity.getName(), startedIntent.getComponent().getClassName());
    }
}