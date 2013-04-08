package com.amnesty.panicbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.amnesty.panicbutton.sms.SMSConfigActivity;

public class SettingsActivity extends Activity {
    private static String TAG = SettingsActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public void launchSmsActivity(View view) {
        startActivity(new Intent(this, SMSConfigActivity.class));
    }
}