package com.amnesty.panicbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;

public class SettingsActivity extends Activity {
    private static String TAG = SettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
    }

    public void launchSmsActivity(View view) {
        startActivity(new Intent(this, SMSSettingsActivity.class));
    }
}