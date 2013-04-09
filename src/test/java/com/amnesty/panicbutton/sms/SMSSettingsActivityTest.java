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
public class SMSSettingsActivityTest {
    private SMSSettingsActivity smsSettingsActivity;

    @Before
    public void setup() {
        smsSettingsActivity = new SMSSettingsActivity();
        smsSettingsActivity.onCreate(null);
    }

    @Test
    public void shouldLimitTheSMSEditTextOnCreate() {
        EditText smsEditText = (EditText) smsSettingsActivity.findViewById(R.id.message_edit_text);
        ShadowTextView shadowTextView = Robolectric.shadowOf(smsEditText);
    }
}
