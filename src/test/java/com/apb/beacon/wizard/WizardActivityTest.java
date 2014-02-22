package com.apb.beacon.wizard;

import android.app.Application;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Button;

import com.apb.beacon.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WizardActivityTest {
    private WizardActivity wizardActivity;
    private Button previousButton;
    private Button actionButton;
//    private WizardViewPager viewPager;
    private static Application context;

    @Mock
    private FragmentStatePagerAdapter mockPagerAdapter;


    @Before
    public void setUp() throws IllegalAccessException {
        context = Robolectric.application;
        initMocks(this);
        wizardActivity = new WizardActivity() {
            FragmentStatePagerAdapter getWizardPagerAdapter() {
                return mockPagerAdapter;
            }
        };
//        wizardActivity.onCreate(null);
//
//        when(mockFragment.action()).thenReturn("Action");
//        when(mockFragment.performAction()).thenReturn(true);
//        when(mockFragment.getView()).thenReturn(mock(EditText.class));
//        when(mockPagerAdapter.getItem(anyInt())).thenReturn(mockFragment);
//        when(mockPagerAdapter.getCount()).thenReturn(3);

//        previousButton = (Button) wizardActivity.findViewById(R.id.previous_button);
//        actionButton = (Button) wizardActivity.findViewById(R.id.action_button);
//        viewPager = (WizardViewPager) wizardActivity.findViewById(R.id.wizard_view_pager);
    }

    @Test
    public void shouldLoadTheWizardLayoutAndInitializeViewPagerOnCreate() {
        assertEquals(R.id.wizard_layout_root, shadowOf(wizardActivity).getContentView().getId());
//        assertEquals(mockPagerAdapter, viewPager.getAdapter());
    }


    @Test
    public void shouldHavePreviousHiddenForFirstScreen() {
        assertFalse(previousButton.isShown());
//        assertEquals(0, viewPager.getCurrentItem());
    }

    @Test
    public void shouldSetActionButtonTextForFirstScreen() {
    }


    @Test
    public void shouldPerformActionAndNavigateToNextScreenAndShowPreviousButton() {
        moveNext(1);
//        assertEquals(1, viewPager.getCurrentItem());
        assertTrue(previousButton.isShown());
    }

    @Test
    public void shouldHavePreviousHiddenOnNavigatingBackToFirstScreen() {
        moveNext(1);
        movePrevious(1);
        assertFalse(previousButton.isShown());
    }

    @Test
    public void shouldNavigateToPreviousScreen() {
        moveNext(2);
        movePrevious(1);
//        assertEquals(1, viewPager.getCurrentItem());
    }

    @Test
    public void shouldNavigateToPreviousScreenOnHardwareBack() {
        moveNext(2);
        wizardActivity.onBackPressed();
//        assertEquals(1, viewPager.getCurrentItem());
    }

    @Test
    public void shouldFinishActivityOnHardwareBackFromFirstScreen() {
        wizardActivity.onBackPressed();
        assertTrue(wizardActivity.isFinishing());
    }

    @Test
    public void shouldCreateWizardPagerAdapter() {
//        assertNotNull(new WizardActivity().getWizardPagerAdapter());
    }

    @Test
    public void shouldHideNextOnNavigatingToFinishScreen() {
        moveNext(2);
        assertFalse(actionButton.isShown());
    }



    private void moveNext(int times) {
        for (int i = 0; i < times; i++) {
            actionButton.performClick();
        }
    }

    private void movePrevious(int times) {
        for (int i = 0; i < times; i++) {
            previousButton.performClick();
        }
    }
}
