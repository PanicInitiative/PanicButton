package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class WizardFragment extends Fragment {
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
    }

    public void onFragmentSelected() {

    }

    public void onBackPressed() {
    }
}
