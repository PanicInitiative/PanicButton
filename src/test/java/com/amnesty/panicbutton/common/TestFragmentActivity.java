package com.amnesty.panicbutton.common;

import com.amnesty.panicbutton.wizard.ActionButtonStateListener;
import roboguice.activity.RoboFragmentActivity;

public class TestFragmentActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    private boolean actionButtonEnabled;

    @Override
    public void onActionStateChanged(boolean state) {
        this.actionButtonEnabled = state;
    }

    public boolean isActionButtonEnabled() {
        return actionButtonEnabled;
    }
}


