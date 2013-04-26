package com.amnesty.panicbutton.wizard;

import android.support.v4.app.FragmentManager;
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
    public void shouldReturnTotalCountOfFragmentsInWizard() {
        assertEquals(0, wizardPageAdapter.getCount());
    }

    @Test
    public void shouldReturnTheFragmentAtTheGivenPosition() {
        assertEquals(null, wizardPageAdapter.getItem(0));
    }
}
