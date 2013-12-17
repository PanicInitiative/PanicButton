package com.apb.beacon.wizard;

import android.util.AttributeSet;
import android.view.MotionEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class WizardViewPagerTest {
    @Test
    public void shouldHaveTouchDisabled() {
        WizardViewPager wizardViewPager = new WizardViewPager(Robolectric.application);
        assertFalse(wizardViewPager.onTouchEvent(mock(MotionEvent.class)));
    }

    @Test
    public void shouldNotInterceptTouchEvent() {
        WizardViewPager wizardViewPager = new WizardViewPager(Robolectric.application, mock(AttributeSet.class));
        assertFalse(wizardViewPager.onInterceptTouchEvent(mock(MotionEvent.class)));
    }
}
