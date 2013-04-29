package com.amnesty.panicbutton.wizard;

import android.support.v4.app.FragmentManager;
import com.amnesty.panicbutton.fragment.SMSSettingsFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import roboguice.activity.RoboFragmentActivity;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class WizardPageAdapterTest {
    private WizardPageAdapter wizardPageAdapter;

    @Before
    public void setUp() {
        FragmentManager fragmentManager = new RoboFragmentActivity().getSupportFragmentManager();
        wizardPageAdapter = new WizardPageAdapter(fragmentManager);
    }

    @Test
    public void shouldReturnTheFragmentsInWizard() {
        assertEquals(1, wizardPageAdapter.getCount());
        assertEquals(wizardPageAdapter.getItem(0).getClass(), SMSSettingsFragment.class);
    }
}
