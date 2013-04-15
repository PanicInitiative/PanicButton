package com.amnesty.panicbutton;

import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TableRow;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.shadow.ShadowCustomSmsManager;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowSmsManager;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(CustomRobolectricTestRunner.class)
public class SettingsActivityTest {
    private SettingsActivity settingsActivity;

    private TableRow smsRow;
    private Button activateButton;

    @Before
    public void setup() {
        settingsActivity = new SettingsActivity();
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
    public void shouldSendSMSToAllConfiguredPhoneNumbersIgnoringInValidNumbers() {
        String message = "Help! I am in trouble";
        String mobile1 = "123-123-1222";
        String mobile2 = "";
        String mobile3 = "6786786789";

        List<String> phoneNumbers = asList(mobile1, mobile2, mobile3);

        SMSSettings.save(application, new SMSSettings(phoneNumbers, message));
        activateButton.performClick();

        ShadowCustomSmsManager shadowSmsManager = (ShadowCustomSmsManager) shadowOf(SmsManager.getDefault());
        List<ShadowSmsManager.TextSmsParams> allSentTextSmsParams = shadowSmsManager.getAllSentTextMessageParams();

        assertEquals(2, allSentTextSmsParams.size());

        assertEquals(mobile1, allSentTextSmsParams.get(0).getDestinationAddress());
        assertEquals(message, allSentTextSmsParams.get(0).getText());

        assertEquals(mobile3, allSentTextSmsParams.get(1).getDestinationAddress());
        assertEquals(message, allSentTextSmsParams.get(1).getText());
    }
}
