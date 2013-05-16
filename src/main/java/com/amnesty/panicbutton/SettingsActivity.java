package com.amnesty.panicbutton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amnesty.panicbutton.alert.AlertStatus;
import com.amnesty.panicbutton.alert.PanicAlert;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import com.amnesty.panicbutton.twitter.TwitterSettingsActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.settings_layout)
public class SettingsActivity extends RoboActivity {
    @InjectView(R.id.activate_alert)
    private Button activateButton;
    @InjectView(R.id.alert_status_text)
    private TextView alertStatusText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAlertStatus();
    }

    private void setAlertStatus() {
        AlertStatus alertStatus = ApplicationSettings.getAlertStatus(this);
        activateButton.setEnabled(alertStatus.canAlert());
        alertStatusText.setText(alertStatus.getDescription());
    }

    public void launchSmsActivity(View view) {
        startActivity(new Intent(this, SMSSettingsActivity.class));
    }

    public void launchTwitterActivity(View view) {
        startActivity(new Intent(this, TwitterSettingsActivity.class));
    }

    public void activateAlert(View view) {
        getPanicAlert().start();
    }

    public void goBack(View view) {
        this.finish();
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(this);
    }
}