package com.amnesty.panicbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;

public class SettingsActivity extends Activity {
    private static String TAG = SettingsActivity.class.getSimpleName();

    private Button activateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActivateButton();
    }

    private void initActivateButton() {
        activateButton = (Button) findViewById(R.id.activate_alert);
        SMSSettings smsSettings = SMSSettings.retrieve(this);

        if (smsSettings.isConfigured()) {
            activateButton.setEnabled(true);
        } else {
            activateButton.setEnabled(false);
        }
    }

    public void launchSmsActivity(View view) {
        startActivity(new Intent(this, SMSSettingsActivity.class));
    }

    public void activateAlert(View view) {
        SmsManager smsManager = SmsManager.getDefault();
        SMSSettings smsSettings = SMSSettings.retrieve(this);
        for(String phoneNumber: smsSettings.validPhoneNumbers()) {
            smsManager.sendTextMessage(phoneNumber, null, smsSettings.message(), null, null);
        }
    }
}