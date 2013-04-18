package com.amnesty.panicbutton;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;
import com.amnesty.panicbutton.sms.SMSSettingsActivity;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.settings_layout)
public class SettingsActivity extends RoboActivity {
    public static final String GOOGLE_MAP_URL = "http://maps.google.com/maps?q=";
    public static final String URL_PREFIX = ". I'm at ";
    @InjectView(R.id.activate_alert) private Button activateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActivateButton();
    }

    private void initActivateButton() {
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
        SMSSettings smsSettings = SMSSettings.retrieve(this);
        SMSAdapter smsAdapter = getSMSAdapter();
        String message = smsSettings.trimmedMessage() + URL_PREFIX + googleMapURL();

        for (String phoneNumber : smsSettings.validPhoneNumbers()) {
            smsAdapter.sendSMS(phoneNumber, message);
        }
    }

    private String googleMapURL() {
        LocationProvider locationProvider = getLocationProvider();
        Location location = locationProvider.currentBestLocation();
        return GOOGLE_MAP_URL + location.getLatitude() + "," + location.getLongitude();
    }

    LocationProvider getLocationProvider() {
        return new LocationProvider(this);
    }

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }
}