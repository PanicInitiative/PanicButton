package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class WizardFragment extends Fragment {
    protected ActionButtonStateListener actionButtonStateListener;
    protected Context context;

    public String action() {
        return getString(WizardAction.NEXT.actionId());
    }

    public boolean performAction() {
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity.getApplicationContext();
//        setActionButtonStateListener(activity);
    }

//    private void setActionButtonStateListener(Activity activity) {
//        if (activity instanceof ActionButtonStateListener)
//            this.actionButtonStateListener = (ActionButtonStateListener) activity;
//    }
//
    public void onFragmentSelected() {
//        if (actionButtonStateListener != null) {
//            actionButtonStateListener.enableActionButton(true);
//        }
    }

    public void onBackPressed() {
    }
}
