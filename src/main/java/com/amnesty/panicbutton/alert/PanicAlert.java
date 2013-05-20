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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.amnesty.panicbutton.twitter.TwitterSettings.retrieve;

public class PanicAlert {
    public static PanicAlert panicAlert;

    private ScheduledExecutorService executorService;
    private Context context;
    private LocationProvider locationProvider;
    private boolean isActive;

    private PanicAlert(Context context) {
        this.context = context;
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public static synchronized PanicAlert getInstance(Context context) {
        panicAlert = (panicAlert == null) ? new PanicAlert(context) : panicAlert;
        return panicAlert;
    }

    public synchronized void activate() {
        if(isActive) {
            return;
        }
        isActive = true;
        startLocationProviderInBackground();
        vibrate();
        scheduleAlert();
    }

    private void scheduleAlert() {
        executorService.scheduleAtFixedRate(
            new Runnable() {
                @Override
                public void run() {
                    activateAlert();
                }
            },
            0, ALERT_FREQUENCY_IN_SECS, TimeUnit.SECONDS
        );
    }

    public synchronized void deActivate() {
        executorService.shutdown();
        locationProvider.terminate();
        panicAlert = null;
    }

    public boolean isActive() {
        return isActive;
    }

    public AlertStatus getAlertStatus() {
        if(isActive()) {
            return AlertStatus.ACTIVE;
        }
        return AlertStatus.STANDBY;
    }


    void activateAlert() {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        if (smsSettings.isConfigured()) {
            sendSMS(smsSettings);
        }
        if (TwitterSettings.isEnabled(context)) {
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
        locationProvider = new LocationProvider(context);
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

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }
    public static final int LOCATION_WAIT_TIME = 1000;
    public static final int MAX_RETRIES = 10;
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;
    public static final int ALERT_FREQUENCY_IN_SECS = 60 * 5;
}