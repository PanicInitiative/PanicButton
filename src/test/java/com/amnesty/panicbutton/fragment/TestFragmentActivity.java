package com.amnesty.panicbutton.fragment;

import com.amnesty.panicbutton.wizard.ActionButtonStateListener;
import roboguice.activity.RoboFragmentActivity;

public class TestFragmentActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    private boolean state;

    @Override
    public void onActionStateChanged(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}


