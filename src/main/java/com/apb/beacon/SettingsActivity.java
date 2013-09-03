package com.apb.beacon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.apb.beacon.alert.AlertStatus;
import com.apb.beacon.alert.PanicAlert;
import com.apb.beacon.dialer.DialerSettingsActivity;
import com.apb.beacon.guardian.GuardianSettingsActivity;
import com.apb.beacon.sms.SMSSettingsActivity;
import com.apb.beacon.twitter.TwitterSettingsActivity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.settings_layout)
public class SettingsActivity extends PanicButtonActivity {
    @InjectView(R.id.activate_alert)
    private Button activateButton;
    @InjectView(R.id.activate_guardian)
    private Button guardianButton;
    @InjectView(R.id.alert_status_text)
    private TextView alertStatusText;
    @InjectView(R.id.dialer_row)
    private TextView dialerSettingsLink;
    @InjectView(R.id.sms_row)
    private TextView smsSettingsLink;
    @InjectView(R.id.twitter_row)
    private TextView twitterSettingsLink;
    @InjectView(R.id.guardian_row)
    private TextView guardianSettingsLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState.getBoolean("ACTIVATE_ONLY", false)) {
//            killGuardian();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    public void launchSmsActivity(View view) {
        startActivity(new Intent(this, SMSSettingsActivity.class));
    }

    public void launchDialerSettingsActivity(View view) {
        startActivity(new Intent(this, DialerSettingsActivity.class));
    }

    public void launchTwitterActivity(View view) {
        startActivity(new Intent(this, TwitterSettingsActivity.class));
    }

    public void launchGuardianActivity(View view) {
        startActivity(new Intent(this, GuardianSettingsActivity.class));
    }

    public void goBack(View view) {
        this.finish();
    }

    public void performAlertAction(View view) {
        toggleAlert();
        updateView();
    }

    public void performGuardianAction(View view) {

        String alarm = view.getContext().ALARM_SERVICE;
        AlarmManager am = ( AlarmManager ) getSystemService( alarm );

        Intent intent = new Intent( "com.apb.beacon.ACTIVATE_ALERT_ACTION" );
        PendingIntent pi = PendingIntent.getBroadcast( this, 0, intent, 0 );

        if (ApplicationSettings.isGuardianActive(view.getContext())) {
            am.cancel(pi);
            ApplicationSettings.setIsGuardianActive(view.getContext(), false);
        } else {

            ApplicationSettings.setIsGuardianActive(view.getContext(), true);

            int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long interval = ApplicationSettings.getGuardianTime(view.getContext()) * 1000;
            long triggerTime = SystemClock.elapsedRealtime() + interval;

            am.set(type, triggerTime, pi );

        }
        updateView();
    }
//
//    public void killGuardian(){
//
//        String alarm = this.ALARM_SERVICE;
//        AlarmManager am = ( AlarmManager ) getSystemService( alarm );
//
//        Intent intent = new Intent( "com.apb.beacon.ACTIVATE_ALERT_ACTION" );
//        PendingIntent pi = PendingIntent.getBroadcast( this, 0, intent, 0 );
//        am.cancel(pi);
//        ApplicationSettings.setIsGuardianActive(this, false);
//        SettingsActivity.this.finish();
//    }

    private void toggleAlert() {
        PanicAlert panicAlert = getPanicAlert();
        if(panicAlert.isActive()) {
            panicAlert.deActivate();

            String alarm = this.getBaseContext().ALARM_SERVICE;
            AlarmManager am = ( AlarmManager ) getSystemService( alarm );

            Intent intent = new Intent( "com.apb.beacon.ACTIVATE_ALERT_ACTION" );
            PendingIntent pi = PendingIntent.getBroadcast( this, 0, intent, 0 );

            am.cancel(pi);

            return;
        }
        SettingsActivity.this.finish();
        panicAlert.activate();
    }

    private void updateView() {
        AlertStatus alertStatus = getPanicAlert().getAlertStatus();
        activateButton.setText(alertStatus.getAction());
        String guardianMessage;
        if (!ApplicationSettings.isGuardianActive(this.getBaseContext())) {
            guardianMessage = getString(R.string.guardian_inactive) + " ("
                    + ApplicationSettings.getGuardianTime(this.getBaseContext()).toString() + ")";
        } else {
            guardianMessage = getString(R.string.guardian_active);
        }
        guardianButton.setText(guardianMessage);
        guardianButton.setEnabled(!ApplicationSettings.isAlertActive(this.getBaseContext()));
        activateButton.setBackgroundResource(alertStatus.getStyle());
        alertStatusText.setText(alertStatus.getDescription());
        dialerSettingsLink.setEnabled(alertStatus.isSettingsEnabled());
        smsSettingsLink.setEnabled(alertStatus.isSettingsEnabled());
        twitterSettingsLink.setEnabled(alertStatus.isSettingsEnabled());
        guardianSettingsLink.setEnabled(alertStatus.isSettingsEnabled());
        updateAlertStatusStrip();
    }
}
