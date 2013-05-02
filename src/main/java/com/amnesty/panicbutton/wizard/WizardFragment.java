package com.amnesty.panicbutton.wizard;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class WizardFragment extends Fragment {
    protected ActionButtonStateListener actionButtonStateListener;
    protected Context context;

    public String action() {
        return "";
    }

    public void performAction() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity.getApplicationContext();
        setActionButtonStateListener(activity);
    }

    private void setActionButtonStateListener(Activity activity) {
        if (activity instanceof ActionButtonStateListener)
            this.actionButtonStateListener = (ActionButtonStateListener) activity;
    }

    public void onFragmentSelected() {
        if (actionButtonStateListener != null) {
            actionButtonStateListener.onActionStateChanged(true);
        }
    }
}
