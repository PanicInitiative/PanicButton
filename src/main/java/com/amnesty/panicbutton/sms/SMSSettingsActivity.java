package com.amnesty.panicbutton.sms;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;

public class SMSSettingsActivity extends RoboFragmentActivity {
    private SMSSettingsFragment smsSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_settings_layout);

        this.smsSettingsFragment = getSMSSettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.sms_settings_layout_root, smsSettingsFragment).commit();
    }

    SMSSettingsFragment getSMSSettingsFragment() {
        return SMSSettingsFragment.create(R.string.sms_settings_header);
    }

    public void save(View view) {
        smsSettingsFragment.performAction();
        Toast.makeText(this, R.string.successfully_saved, Toast.LENGTH_SHORT).show();
    }
}