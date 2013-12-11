package com.apb.beacon.wizard;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
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

    public void nextWithSkip() {
        Log.e(">>>>>>", "getCurrentItem before change = " + getCurrentItem());
            setCurrentItem(getCurrentItem() + 2);
        Log.e(">>>>>>", "getCurrentItem after change = " + getCurrentItem());
    }

    public void previous() {
        setCurrentItem(getCurrentItem() - 1);
    }

    public void previousWithSkip() {
        Log.e(">>>>>>", "getCurrentItem before change = " + getCurrentItem());
        if ((getCurrentItem() - 2) >= 0)
            setCurrentItem(getCurrentItem() - 2);
        Log.e(">>>>>>", "getCurrentItem after change = " + getCurrentItem());
    }

    public boolean isFirstPage() {
        return getCurrentItem() == 0;
    }
}

