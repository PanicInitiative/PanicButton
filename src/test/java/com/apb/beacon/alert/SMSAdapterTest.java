package com.apb.beacon.alert;

import android.telephony.SmsManager;

import com.apb.beacon.alert.SMSAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowSmsManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    @Test
    public void shouldIgnoreErrorsWhileSendingSMS() {
        String phoneNumber = "---";
        String message = "Test Message";
        final SmsManager mockSmsManager = mock(SmsManager.class);
        doThrow(new RuntimeException("Test Exception")).when(mockSmsManager).sendTextMessage(phoneNumber, null, message, null, null);

        smsAdapter = new SMSAdapter() {
            SmsManager getSmsManager() {
                return mockSmsManager;
            }
        };

        smsAdapter.sendSMS(phoneNumber, message);

        verify(mockSmsManager).sendTextMessage(phoneNumber, null, message, null, null);
    }
}
