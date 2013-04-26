package com.amnesty.panicbutton.wizard;

import android.support.v4.view.PagerAdapter;
import android.widget.Button;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WizardActivityTest {
    private WizardActivity wizardActivity;
    private Button previousButton;
    private Button nextButton;
    private WizardViewPager viewPager;
    @Mock
    private PagerAdapter mockPagerAdapter;

    @Before
    public void setUp() throws IllegalAccessException {
        initMocks(this);
        wizardActivity = new WizardActivity() {
            PagerAdapter getWizardPagerAdapter() {
                return mockPagerAdapter;
            }
        };
        wizardActivity.onCreate(null);

        when(mockPagerAdapter.getCount()).thenReturn(3);
        previousButton = (Button) wizardActivity.findViewById(R.id.previous_button);
        nextButton = (Button) wizardActivity.findViewById(R.id.next_button);
        viewPager = (WizardViewPager) wizardActivity.findViewById(R.id.pager);
    }

    @Test
    public void shouldLoadTheWizardLayoutAndInitializeViewPagerOnCreate() {
        assertEquals(R.id.wizard_layout_root, shadowOf(wizardActivity).getContentView().getId());
        assertEquals(mockPagerAdapter, viewPager.getAdapter());
    }

    @Test
    public void shouldHavePreviousDisabledOnFirstScreen() {
        assertFalse(previousButton.isEnabled());
        assertEquals(0, viewPager.getCurrentItem());
    }

    @Test
    public void shouldNavigateToNextScreenAndEnablePreviousButton() {
        moveNext(1);
        assertEquals(1, viewPager.getCurrentItem());
        assertTrue(previousButton.isEnabled());
    }

    @Test
    public void shouldHavePreviousDisabledOnNavigatingBackToFirstScreen() {
        moveNext(1);
        movePrevious(1);
        assertFalse(previousButton.isEnabled());
    }

    @Test
    public void shouldNavigateToPreviousScreen() {
        moveNext(2);
        movePrevious(1);
        assertEquals(1, viewPager.getCurrentItem());
    }

    @Test
    public void shouldHaveNextDisabledOnLastScreen() {
        moveNext(2);
        assertFalse(nextButton.isEnabled());
    }

    @Test
    public void shouldCreateWizardPagerAdapter() {
        assertNotNull(new WizardActivity().getWizardPagerAdapter());
    }

    private void moveNext(int times) {
        for (int i = 0; i < times; i++) {
            nextButton.performClick();
        }
    }

    private void movePrevious(int times) {
        for (int i = 0; i < times; i++) {
            previousButton.performClick();
        }
    }
}
