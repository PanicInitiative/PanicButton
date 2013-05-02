package com.amnesty.panicbutton.sms;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.sms_settings_layout)
public class SMSSettingsActivity extends RoboFragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void save(View view) {
        getSMSSettingsFragment().performAction();
        Toast.makeText(this, R.string.successfully_saved, Toast.LENGTH_SHORT).show();
    }

    SMSSettingsFragment getSMSSettingsFragment() {
        return (SMSSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.sms_settings_fragment);
    }
}