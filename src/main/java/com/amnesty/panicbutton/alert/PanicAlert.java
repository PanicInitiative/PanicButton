package com.amnesty.panicbutton.alert;

import android.content.Context;
import android.location.Location;
import android.os.Vibrator;
import android.util.Log;
import com.amnesty.panicbutton.location.LocationFormatter;
import com.amnesty.panicbutton.location.LocationProvider;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.sms.SMSAdapter;
import com.amnesty.panicbutton.twitter.ShortCodeSettings;
import com.amnesty.panicbutton.twitter.TwitterSettings;

import static com.amnesty.panicbutton.twitter.TwitterSettings.retrieve;

public class PanicAlert extends Thread {
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;
    private Context context;
    private LocationProvider locationProvider;

    public PanicAlert(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        boolean isSMSConfigured = smsSettings.isConfigured();
        boolean isTwitterEnabled = TwitterSettings.isEnabled(context);
        if (isSMSConfigured || isTwitterEnabled) {
            startLocationProviderInBackground();
            vibrate();
        }
        if (isSMSConfigured) {
            sendSMS(smsSettings);
        }
        if (isTwitterEnabled) {
            tweet(retrieve(context));
        }
    }

    private void tweet(TwitterSettings twitterSettings) {
        SMSAdapter smsAdapter = getSMSAdapter();
        ShortCodeSettings shortCodeSettings = twitterSettings.getShortCodeSettings();
        String message = new PanicAlertMessage(twitterSettings.getMessage(), location()).getTwitterText();
        smsAdapter.sendSMS(shortCodeSettings.getShortCode(), message);
    }

    private void startLocationProviderInBackground() {
        locationProvider = getLocationProvider();
        locationProvider.start();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(HAPTIC_FEEDBACK_DURATION);
    }

    private void sendSMS(SMSSettings smsSettings) {
        SMSAdapter smsAdapter = getSMSAdapter();
        String message = new PanicAlertMessage(smsSettings.trimmedMessage(), location()).getSMSText();

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

    public static final int MAX_RETRIES = 10;
}