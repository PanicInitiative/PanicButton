package com.amnesty.panicbutton.sms;

import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsActivityTest {
    private SMSSettingsActivity smsSettingsActivity;

    @Before
    public void setup() {
        smsSettingsActivity = new SMSSettingsActivity();
        smsSettingsActivity.onCreate(null);
    }

    @Test
    public void shouldLoadTheSMSSettingsLayoutOnCreate() {
        assertEquals(R.id.sms_settings_layout_root, shadowOf(smsSettingsActivity).getContentView().getId());
    }
}
