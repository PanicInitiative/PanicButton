package org.iilab.pb.alert;

import android.content.Context;
import android.telephony.SmsManager;

import org.iilab.pb.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSmsManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class SMSAdapterTest {
    private SMSAdapter smsAdapter;
    private Context context;

    @Before
    public void setUp() {

        smsAdapter = new SMSAdapter();
        context = RuntimeEnvironment.application;
    }

    @Test
    public void shouldSendSMSToTheGivenPhoneNumber() {
        String message = "Test Message";
        String phoneNumber = "123-123-1222";

        smsAdapter.sendSMS(context, phoneNumber, message);

        ShadowSmsManager shadowSmsManager = Shadows.shadowOf(SmsManager.getDefault());
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

        smsAdapter.sendSMS(context, phoneNumber, message);

        verify(mockSmsManager).sendTextMessage(phoneNumber, null, message, null, null);
    }
}
