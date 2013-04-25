package com.amnesty.panicbutton.wizard;

import android.widget.Button;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WizardActivityTest {
    private WizardActivity wizardActivity;
    private Button previousButton;
    private Button nextButton;
    private ViewPagerWithoutSwipe viewPager;

    @Before
    public void setUp() {
        wizardActivity = new WizardActivity();
        wizardActivity.onCreate(null);

        previousButton = (Button) wizardActivity.findViewById(R.id.previous_button);
        nextButton = (Button) wizardActivity.findViewById(R.id.next_button);
        viewPager = (ViewPagerWithoutSwipe) wizardActivity.findViewById(R.id.pager);
    }

    @Test
    public void shouldLoadTheWizardLayoutOnCreate() {
        assertEquals(R.id.wizard_layout_root, shadowOf(wizardActivity).getContentView().getId());
    }

    @Test
    public void shouldHavePreviousDisabledOnFirstScreen() {
        assertFalse(previousButton.isEnabled());
        assertEquals(0, viewPager.getCurrentItem());
    }

    @Test
    public void shouldNavigateToNextScreenAndEnablePreviousButton() {
        nextButton.performClick();
        assertEquals(1, viewPager.getCurrentItem());
        assertTrue(previousButton.isEnabled());
    }

    @Test
    public void shouldHavePreviousDisabledOnNavigatingBackToFirstScreen() {
        nextButton.performClick();
        previousButton.performClick();
        assertFalse(previousButton.isEnabled());
    }

    @Test
    public void shouldNavigateToPreviousScreen() {
        nextButton.performClick();
        nextButton.performClick();

        previousButton.performClick();
        assertEquals(1, viewPager.getCurrentItem());
    }

    @Test
    @Ignore
    public void shouldHaveNextDisabledOnLastScreen() {
        assertFalse(nextButton.isEnabled());
    }
}
