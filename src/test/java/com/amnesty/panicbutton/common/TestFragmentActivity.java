package com.amnesty.panicbutton.common;

import com.amnesty.panicbutton.twitter.TwitterShortCodeFragment;
import com.amnesty.panicbutton.wizard.ActionButtonStateListener;
import roboguice.activity.RoboFragmentActivity;

public class TestFragmentActivity extends RoboFragmentActivity
        implements ActionButtonStateListener,
        TwitterShortCodeFragment.ShortCodeSelectedListener {
    private boolean actionButtonState;
    private boolean shortCodeFlag;

    @Override
    public void onActionStateChanged(boolean state) {
        this.actionButtonState = state;
    }

    public boolean getActionButtonState() {
        return actionButtonState;
    }

    @Override
    public void onShortCodeSelection(boolean successFlag) {
        this.shortCodeFlag = successFlag;
    }

    public boolean getShortCodeFlag() {
        return shortCodeFlag;
    }
}


