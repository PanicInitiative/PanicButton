package com.amnesty.panicbutton.common;

import com.amnesty.panicbutton.wizard.ActionButtonStateListener;
import roboguice.activity.RoboFragmentActivity;

public class TestFragmentActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    private boolean actionButtonState;
    private boolean shortCodeFlag;

    @Override
    public void onActionStateChanged(boolean state) {
        this.actionButtonState = state;
    }

    public boolean getActionButtonState() {
        return actionButtonState;
    }

    public boolean getShortCodeFlag() {
        return shortCodeFlag;
    }
}


