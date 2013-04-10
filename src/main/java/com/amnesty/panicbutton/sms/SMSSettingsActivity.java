package com.amnesty.panicbutton.sms;

import android.os.Bundle;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;

public class SMSSettingsActivity extends RoboFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_settings_layout);
    }
}