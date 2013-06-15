package com.apb.beacon.sms;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import com.apb.beacon.R;
import com.apb.beacon.SettingsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowFragmentActivity;
import org.robolectric.shadows.ShadowHandler;
import org.robolectric.shadows.ShadowToast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
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

        saveButton = (Button) smsSettingsActivity.findViewById(R.id.sms_save_button);
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

    @Test
    public void shouldAlertUserOnBackPressedWhenSMSSettingsHaveChangedAndSaveEnabled() {
        when(mockSMSSettingsFragment.hasSettingsChanged()).thenReturn(true);
        saveButton.setEnabled(true);

        smsSettingsActivity.onBackPressed();

        ShadowAlertDialog shadowAlertDialog = shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        assertEquals("Settings Modified", shadowAlertDialog.getTitle());
        assertEquals("Do you want to save the changes?", shadowAlertDialog.getMessage());
        assertTrue(shadowAlertDialog.isShowing());
    }

    @Test
    public void shouldCloseTheScreenWithoutAlertingTheUserOnInvalidSMSSettingsChanges() {
        when(mockSMSSettingsFragment.hasSettingsChanged()).thenReturn(true);
        saveButton.setEnabled(false);

        smsSettingsActivity.onBackPressed();

        assertTrue(shadowOf(smsSettingsActivity).isFinishing());
    }

    @Test
    public void shouldSaveSettingsOnSelectingYesOnAlertDialog() {
        when(mockSMSSettingsFragment.hasSettingsChanged()).thenReturn(true);

        smsSettingsActivity.onBackPressed();

        ShadowAlertDialog shadowAlertDialog = shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        shadowAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

        verify(mockSMSSettingsFragment).performAction();
        assertTrue(shadowOf(smsSettingsActivity).isFinishing());
    }

    @Test
    public void shouldNotSaveSettingsOnSelectingNoOnAlertDialog() {
        when(mockSMSSettingsFragment.hasSettingsChanged()).thenReturn(true);

        smsSettingsActivity.onBackPressed();

        ShadowAlertDialog shadowAlertDialog = shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        shadowAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();

        verify(mockSMSSettingsFragment, never()).performAction();
        assertTrue(shadowOf(smsSettingsActivity).isFinishing());
    }

    @Test
    public void shouldGoToSettingsScreenWhenSMSSettingsHasNotChanged() {
        when(mockSMSSettingsFragment.hasSettingsChanged()).thenReturn(false);
        smsSettingsActivity.onBackPressed();
        assertTrue(shadowOf(smsSettingsActivity).isFinishing());
    }

    @Test
    public void shouldEnableOrDisableSaveButton() {
        smsSettingsActivity.enableActionButton(true);
        assertTrue(saveButton.isEnabled());
        smsSettingsActivity.enableActionButton(false);
        assertTrue(!saveButton.isEnabled());
    }
    
    @Test
    public void shouldGoBackToSettingOnClickingBack() {
    	previousButton.performClick();
        ShadowFragmentActivity shadow = shadowOf(smsSettingsActivity);
		assertTrue(shadow.isFinishing());
        
		Intent nextStartedActivity = shadow.getNextStartedActivity();
		assertNotNull("Settings activity has not been started", nextStartedActivity);
        assertThat(nextStartedActivity.getComponent().getClassName(), equalTo(SettingsActivity.class.getName()));
    }
  
}
