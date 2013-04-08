package com.amnesty.panicbutton.sms;

import android.widget.EditText;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowTextView;

@RunWith(RobolectricTestRunner.class)
public class SMSConfigActivityTest {
    private SMSConfigActivity smsConfigActivity;

    @Before
    public void setup() {
        smsConfigActivity = new SMSConfigActivity();
        smsConfigActivity.onCreate(null);
    }

    @Test
    public void shouldLimitTheSMSEditTextOnCreate() {
        EditText smsEditText = (EditText) smsConfigActivity.findViewById(R.id.sms_message);
        ShadowTextView shadowTextView = Robolectric.shadowOf(smsEditText);
    }
}
