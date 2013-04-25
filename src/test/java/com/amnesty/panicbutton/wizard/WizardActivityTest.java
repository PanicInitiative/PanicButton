package com.amnesty.panicbutton.wizard;

import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WizardActivityTest {
    private WizardActivity wizardActivity;

    @Before
    public void setUp() {
        wizardActivity = new WizardActivity();
        wizardActivity.onCreate(null);
    }

    @Test
    public void shouldLoadTheWizardLayoutOnCreate() {
        assertEquals(R.id.wizard_layout_root, shadowOf(wizardActivity).getContentView().getId());
    }
}
