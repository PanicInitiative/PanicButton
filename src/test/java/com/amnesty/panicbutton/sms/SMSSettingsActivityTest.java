package com.amnesty.panicbutton.sms;

import android.widget.Button;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowHandler;
import org.robolectric.shadows.ShadowToast;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsActivityTest {
    private SMSSettingsActivity smsSettingsActivity;
    private Button saveButton;
    private Button previousButton;
    @Mock
    private SMSSettingsFragment mockSMSSettingsFragment;

    @Before
    public void setup() {
        initMocks(this);
        smsSettingsActivity = new SMSSettingsActivity() {
            SMSSettingsFragment getSMSSettingsFragment() {
                return mockSMSSettingsFragment;
            }
        };
        smsSettingsActivity.onCreate(null);
        saveButton = (Button) smsSettingsActivity.findViewById(R.id.save_button);
        previousButton = (Button) smsSettingsActivity.findViewById(R.id.sms_previous_button);
    }

    @Test
    public void shouldSaveSettingsOnSave() {
        saveButton.performClick();
        verify(mockSMSSettingsFragment).performAction();

        ShadowHandler.idleMainLooper();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("SMS settings saved successfully"));
    }

    @Test
    public void shouldShowSettingsScreenOnClickingBack() {
        previousButton.performClick();
        assertTrue(shadowOf(smsSettingsActivity).isFinishing());
    }
}
