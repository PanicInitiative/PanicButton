package com.apb.panicbutton.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SimpleFragment extends WizardFragment {
    private static final String LAYOUT_ID = "layout_id";
    private static final String ACTION_ID = "action_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(this.getLayoutId(), container, false);
    }

    public static SimpleFragment create(int layoutId, int actionId) {
        SimpleFragment simpleFragment = new SimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        bundle.putInt(ACTION_ID, actionId);
        simpleFragment.setArguments(bundle);
        return simpleFragment;
    }

    public int getLayoutId() {
        return getArguments().getInt(LAYOUT_ID);
    }

    @Override
    public String action() {
        return getString(getArguments().getInt(ACTION_ID));
    }
}