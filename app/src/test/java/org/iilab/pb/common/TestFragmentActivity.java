package org.iilab.pb.common;

import android.support.v4.app.FragmentActivity;

public class TestFragmentActivity extends FragmentActivity {
    private boolean actionButtonEnabled;

//    @Override
//    public void enableActionButton(boolean isEnabled) {
//        this.actionButtonEnabled = isEnabled;
//    }

    public boolean isActionButtonEnabled() {
        return actionButtonEnabled;
    }
}


