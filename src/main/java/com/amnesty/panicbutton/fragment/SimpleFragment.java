package com.amnesty.panicbutton.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SimpleFragment extends Fragment {
    private int layoutId;

    public SimpleFragment() {
    }

    public SimpleFragment(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(this.layoutId, container, false);
    }

    public int getLayoutId() {
        return this.layoutId;
    }
}
