package com.amnesty.panicbutton.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import roboguice.fragment.RoboFragment;

public abstract class AbstractWizardFragment extends RoboFragment {
    private static String TAG = AbstractWizardFragment.class.getSimpleName();

    @Override
    public void onDestroyView() {
        try {
            FragmentManager fragmentManager = getFragmentManager();
            for (int fragmentId : getFragmentIds()) {
                FragmentTransaction mTransaction = fragmentManager.beginTransaction();
                Fragment fragment = fragmentManager.findFragmentById(fragmentId);
                mTransaction.remove(fragment);
                mTransaction.commit();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error on destroy view : ", e);
        }
        super.onDestroyView();
    }

    protected abstract int[] getFragmentIds();
}
