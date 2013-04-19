package com.amnesty.panicbutton;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.amnesty.panicbutton.location.LocationFormatter;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;

public class MessageAlerter extends Thread {
    private Context context;

    public MessageAlerter(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        activateAlert();
    }

    void activateAlert() {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        SMSAdapter smsAdapter = getSMSAdapter();
        String message = smsSettings.trimmedMessage() + location();

        for (String phoneNumber : smsSettings.validPhoneNumbers()) {
            smsAdapter.sendSMS(phoneNumber, message);
        }
    }

    private String location() {
        Location location = null;
        int retryCount = 0;

        LocationProvider locationProvider = startLocationProviderInBackground();
        while (retryCount < MAX_RETRIES && location == null) {
            location = locationProvider.currentBestLocation();
            if (location == null) {
                try {
                    retryCount++;
                    Thread.sleep(LOCATION_WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.e("SettingsActivity", "Location wait InterruptedException", e);
                }
            }
        }
        return new LocationFormatter(location).format();
    }

    LocationProvider startLocationProviderInBackground() {
        LocationProvider locationProvider = new LocationProvider(context);
        locationProvider.start();
        return locationProvider;
    }

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }

    public static final int LOCATION_WAIT_TIME = 1000;
    public static final int MAX_RETRIES = 5;
}