package com.amnesty.panicbutton;

import android.content.Context;
import android.location.Location;
import android.os.Vibrator;
import android.util.Log;
import com.amnesty.panicbutton.location.LocationFormatter;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;

public class MessageAlerter extends Thread {
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;
    private Context context;
    private LocationProvider locationProvider;

    public MessageAlerter(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        startLocationProviderInBackground();
        vibrate();
        activateAlert();
    }

    private void startLocationProviderInBackground() {
        locationProvider = getLocationProvider();
        locationProvider.start();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(HAPTIC_FEEDBACK_DURATION);
    }

    private void activateAlert() {
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

    LocationProvider getLocationProvider() {
        return new LocationProvider(context);
    }

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }
    public static final int LOCATION_WAIT_TIME = 1000;

    public static final int MAX_RETRIES = 5;
}