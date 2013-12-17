package com.apb.beacon.wizard;

import android.app.Activity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.apb.beacon.R;

import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowInputMethodManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WizardActivityTest {
    private WizardActivity wizardActivity;
    private Button previousButton;
    private Button actionButton;
    private WizardViewPager viewPager;
    @Mock
    private FragmentStatePagerAdapter mockPagerAdapter;
    @Mock
    private SimpleFragment mockFragment;

    @Before
    public void setUp() throws IllegalAccessException {
        initMocks(this);
        wizardActivity = new WizardActivity() {
            FragmentStatePagerAdapter getWizardPagerAdapter() {
                return mockPagerAdapter;
            }
        };
        wizardActivity.onCreate(null);

        when(mockFragment.action()).thenReturn("Action");
        when(mockFragment.performAction()).thenReturn(true);
        when(mockFragment.getView()).thenReturn(mock(EditText.class));
        when(mockPagerAdapter.getItem(anyInt())).thenReturn(mockFragment);
        when(mockPagerAdapter.getCount()).thenReturn(3);

        previousButton = (Button) wizardActivity.findViewById(R.id.previous_button);
        actionButton = (Button) wizardActivity.findViewById(R.id.action_button);
        viewPager = (WizardViewPager) wizardActivity.findViewById(R.id.wizard_view_pager);
    }

    @Test
    public void shouldLoadTheWizardLayoutAndInitializeViewPagerOnCreate() {
        assertEquals(R.id.wizard_layout_root, shadowOf(wizardActivity).getContentView().getId());
        assertEquals(mockPagerAdapter, viewPager.getAdapter());
    }

    @Test
    public void shouldHideKeyboardOnNavigation() {
        ShadowInputMethodManager shadowInputMethodManager = shadowOf((InputMethodManager)
                application.getSystemService(Activity.INPUT_METHOD_SERVICE));
        shadowInputMethodManager.showSoftInput(mockFragment.getView(), 0);

        assertTrue(shadowInputMethodManager.isSoftInputVisible());
        moveNext(1);
        assertFalse(shadowInputMethodManager.isSoftInputVisible());
    }

    @Test
    public void shouldHavePreviousHiddenForFirstScreen() {
        assertFalse(previousButton.isShown());
        assertEquals(0, viewPager.getCurrentItem());
    }

    @Test
    public void shouldSetActionButtonTextForFirstScreen() {
        assertEquals("Start", actionButton.getText());
    }

    @Test
    public void shouldUpdateActionButtonTextOnNavigationToNextScreen() {
        when(mockFragment.action()).thenReturn("Save");
        moveNext(1);
        assertEquals("Save", actionButton.getText());
        verify(mockFragment).performAction();
    }

    @Test
    public void shouldPerformActionAndNavigateToNextScreenAndShowPreviousButton() {
        moveNext(1);
        assertEquals(1, viewPager.getCurrentItem());
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
        assertEquals(1, viewPager.getCurrentItem());
    }

    @Test
    public void shouldNavigateToPreviousScreenOnHardwareBack() {
        moveNext(2);
        wizardActivity.onBackPressed();
        assertEquals(1, viewPager.getCurrentItem());
    }

    @Test
    public void shouldFinishActivityOnHardwareBackFromFirstScreen() {
        wizardActivity.onBackPressed();
        assertTrue(wizardActivity.isFinishing());
    }

    @Test
    public void shouldCreateWizardPagerAdapter() {
        assertNotNull(new WizardActivity().getWizardPagerAdapter());
    }

    @Test
    public void shouldHideNextOnNavigatingToFinishScreen() {
        moveNext(2);
        assertFalse(actionButton.isShown());
    }

    @Test
    public void shouldChangeStateOfActionButton() {
        wizardActivity.enableActionButton(true);
        assertTrue(actionButton.isEnabled());
        wizardActivity.enableActionButton(false);
        assertTrue(!actionButton.isEnabled());
    }

    @Test
    public void shouldNotMoveNextIfPerformActionFails() throws IllegalAccessException {
        when(mockFragment.performAction()).thenReturn(false);
        WizardViewPager mockWizardViewPager = mock(WizardViewPager.class);
        ReflectionUtils.setVariableValueInObject(wizardActivity, "viewPager", mockWizardViewPager);

        wizardActivity.performAction(null);
        verify(mockWizardViewPager, never()).next();
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
