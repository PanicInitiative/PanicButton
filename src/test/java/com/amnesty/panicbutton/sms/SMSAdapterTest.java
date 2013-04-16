package com.amnesty.panicbutton.sms;

import android.telephony.SmsManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowSmsManager;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SMSAdapterTest {
    private SMSAdapter smsAdapter;

    @Before
    public void setUp() {
        smsAdapter = new SMSAdapter();
    }

    @Test
    public void shouldSendSMSToTheGivenPhoneNumber() {
        String message = "Test Message";
        String phoneNumber = "123-123-1222";

        smsAdapter.sendSMS(phoneNumber, message);

        ShadowSmsManager shadowSmsManager = shadowOf(SmsManager.getDefault());
        ShadowSmsManager.TextSmsParams lastSentTextMessageParams = shadowSmsManager.getLastSentTextMessageParams();

        assertEquals(phoneNumber, lastSentTextMessageParams.getDestinationAddress());
        assertEquals(message, lastSentTextMessageParams.getText());
    }
}
