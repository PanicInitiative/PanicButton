package com.apb.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.apb.beacon.alert.AlertStatus;
import com.apb.beacon.alert.PanicAlert;
import com.apb.beacon.sms.SMSSettingsActivity;
import com.apb.beacon.twitter.TwitterSettingsActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.settings_layout)
public class SettingsActivity extends RoboActivity {
    @InjectView(R.id.activate_alert)
    private Button activateButton;
    @InjectView(R.id.alert_status_text)
    private TextView alertStatusText;
    @InjectView(R.id.sms_row)
    private TextView smsSettingsLink;
    @InjectView(R.id.twitter_row)
    private TextView twitterSettingsLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    public void launchSmsActivity(View view) {
        startActivity(new Intent(this, SMSSettingsActivity.class));
    }

    public void launchTwitterActivity(View view) {
        startActivity(new Intent(this, TwitterSettingsActivity.class));
    }

    public void goBack(View view) {
        this.finish();
    }

    public void performAlertAction(View view) {
        toggleAlert();
        updateView();
    }

    private void toggleAlert() {
        PanicAlert panicAlert = getPanicAlert();
        if(panicAlert.isActive()) {
            panicAlert.deActivate();
            return;
        }
        panicAlert.activate();
    }

    private void updateView() {
        AlertStatus alertStatus = getPanicAlert().getAlertStatus();
        activateButton.setText(alertStatus.getAction());
        activateButton.setBackgroundResource(alertStatus.getStyle());
        alertStatusText.setText(alertStatus.getDescription());
        smsSettingsLink.setEnabled(alertStatus.isSettingsEnabled());
        twitterSettingsLink.setEnabled(alertStatus.isSettingsEnabled());
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(this);
    }
}