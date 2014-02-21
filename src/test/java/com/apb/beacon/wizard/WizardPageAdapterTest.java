package com.apb.beacon.wizard;

import android.support.v4.app.FragmentManager;

import com.apb.beacon.AppConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import roboguice.activity.RoboFragmentActivity;

import static com.apb.beacon.R.layout.wizard_emergency_alert1;
import static com.apb.beacon.R.layout.wizard_emergency_alert2;
import static com.apb.beacon.R.layout.wizard_emergency_alert3;
import static com.apb.beacon.R.string.next_action;

@RunWith(RobolectricTestRunner.class)
public class WizardPageAdapterTest {
//    private WizardPageAdapter wizardPageAdapter;

    @Before
    public void setUp() {
        FragmentManager fragmentManager = new RoboFragmentActivity().getSupportFragmentManager();
//        wizardPageAdapter = new WizardPageAdapter(fragmentManager);
    }

    @Test
    public void shouldReturnTheFragmentsInWizard() {
//        assertEquals(AppConstants.WIZARD_TOTAL_PAGE_COUNT, wizardPageAdapter.getCount());

//        assertSimpleFragment(0, wizard_start_screen, start_action);
//        assertEquals(WizardWelcomeFragment.class, wizardPageAdapter.getItem(AppConstants.PAGE_NUMBER_WIZARD_WELCOME).getClass());
//        assertEquals(WizardTrainingFragment.class, wizardPageAdapter.getItem(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING).getClass());
//        assertEquals(SetupCodeFragment.class, wizardPageAdapter.getItem(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN).getClass());
//        SMSSettingsFragment smsSettingsFragment = (SMSSettingsFragment) wizardPageAdapter.getItem(2);
//        assertEquals(R.string.sms_settings_wizard_header, smsSettingsFragment.getArguments().getInt("HEADER_TEXT_ID"));
        assertSimpleFragment(AppConstants.PAGE_NUMBER_EMERGENCY_ALERT1, wizard_emergency_alert1, next_action);
        assertSimpleFragment(AppConstants.PAGE_NUMBER_EMERGENCY_ALERT2, wizard_emergency_alert2, next_action);
        assertSimpleFragment(AppConstants.PAGE_NUMBER_EMERGENCY_ALERT3, wizard_emergency_alert3, next_action);
//        assertEquals(WizardFinishFragment.class, wizardPageAdapter.getItem(AppConstants.PAGE_NUMBER_FINISH_WIZARD).getClass());
    }

    private void assertSimpleFragment(int screenIndex, int layoutId, int actionId) {
//        SimpleFragment simpleFragment = (SimpleFragment) wizardPageAdapter.getItem(screenIndex);
//        Bundle arguments = simpleFragment.getArguments();
//        assertEquals(layoutId, arguments.getInt("layout_id"));
//        assertEquals(actionId, arguments.getInt("action_id"));
    }
}
