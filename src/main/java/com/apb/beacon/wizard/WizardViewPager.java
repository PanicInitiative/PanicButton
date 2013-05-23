package com.apb.beacon.wizard;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class WizardViewPager extends ViewPager {
    public WizardViewPager(Context context) {
        super(context);
    }

    public WizardViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    public void next() {
        setCurrentItem(getCurrentItem() + 1);
    }

    public void previous() {
        setCurrentItem(getCurrentItem() - 1);
    }

    public boolean isFirstPage() {
        return getCurrentItem() == 0;
    }
}

