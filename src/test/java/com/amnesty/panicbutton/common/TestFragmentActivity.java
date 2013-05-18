package com.amnesty.panicbutton.common;

import com.amnesty.panicbutton.wizard.ActionButtonStateListener;
import roboguice.activity.RoboFragmentActivity;

public class TestFragmentActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    private boolean actionButtonEnabled;

    @Override
    public void enableActionButton(boolean isEnabled) {
        this.actionButtonEnabled = isEnabled;
    }

    public boolean isActionButtonEnabled() {
        return actionButtonEnabled;
    }
}


