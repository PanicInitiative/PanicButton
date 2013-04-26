package com.amnesty.panicbutton.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.amnesty.panicbutton.R;
import roboguice.fragment.RoboFragment;

public class SMSSettingsFragment extends RoboFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sms_settings_fragment, container, false);
    }
}
