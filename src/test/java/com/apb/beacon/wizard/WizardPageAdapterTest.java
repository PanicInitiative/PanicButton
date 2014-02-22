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
        assertSimpleFragment(AppConstants.PAGE_NUMBER_EMERGENCY_ALERT1, wizard_emergency_alert1, next_action);
        assertSimpleFragment(AppConstants.PAGE_NUMBER_EMERGENCY_ALERT2, wizard_emergency_alert2, next_action);
        assertSimpleFragment(AppConstants.PAGE_NUMBER_EMERGENCY_ALERT3, wizard_emergency_alert3, next_action);
    }

    private void assertSimpleFragment(int screenIndex, int layoutId, int actionId) {

    }
}
