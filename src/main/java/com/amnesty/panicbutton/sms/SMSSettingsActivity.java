package com.amnesty.panicbutton.sms;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.SoftKeyboard;
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

    public void showSettings(View view){
        this.finish();
    }

    SMSSettingsFragment getSMSSettingsFragment() {
        return SMSSettingsFragment.create(R.string.sms_settings_header);
    }

    public void save(View view) {
        smsSettingsFragment.performAction();
        SoftKeyboard.hide(this, this.findViewById(android.R.id.content));
        Toast.makeText(this, R.string.sms_save_successful, Toast.LENGTH_SHORT).show();
    }
}