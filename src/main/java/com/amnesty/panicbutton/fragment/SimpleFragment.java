package com.amnesty.panicbutton.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SimpleFragment extends Fragment {
    private static final String LAYOUT_ID = "layout_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(this.getLayoutId(), container, false);
    }

    public int getLayoutId() {
        return getArguments().getInt(LAYOUT_ID);
    }

    public static SimpleFragment create(int layoutId) {
        SimpleFragment simpleFragment = new SimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SimpleFragment.LAYOUT_ID, layoutId);
        simpleFragment.setArguments(bundle);
        return simpleFragment;
    }
}
