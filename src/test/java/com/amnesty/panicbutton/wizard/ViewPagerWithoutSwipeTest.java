package com.amnesty.panicbutton.wizard;

import android.util.AttributeSet;
import android.view.MotionEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class ViewPagerWithoutSwipeTest {
    @Test
    public void shouldHaveTouchDisabled() {
        ViewPagerWithoutSwipe viewPagerWithoutSwipe = new ViewPagerWithoutSwipe(Robolectric.application);
        assertFalse(viewPagerWithoutSwipe.onTouchEvent(mock(MotionEvent.class)));
    }

    @Test
    public void shouldNotInterceptTouchEvent() {
        ViewPagerWithoutSwipe viewPagerWithoutSwipe = new ViewPagerWithoutSwipe(Robolectric.application, mock(AttributeSet.class));
        assertFalse(viewPagerWithoutSwipe.onInterceptTouchEvent(mock(MotionEvent.class)));
    }
}
