package org.iilab.pb.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.Intents;
import org.iilab.pb.location.CurrentLocationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static org.iilab.pb.common.Intents.locationPendingIntent;
import static org.iilab.pb.common.AppConstants.*;
public class PanicAlert {
    private static final String TAG = PanicAlert.class.getName();
    private LocationManager locationManager;
    private Context context;
    private AlarmManager alarmManager1, alarmManager2;

    public PanicAlert(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void activate() {
        AppUtil.close(context);
        vibrateOnceForConfirmationOfAlertTriggered();

        if (isActive()
//                || ApplicationSettings.isRestartedSetup(context)
                ) {
            return;
        }

        ApplicationSettings.setAlertActive(context, true);
        getExecutorService().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        activateAlert();
                    }
                }
        );
    }

    private void vibrateOnceForConfirmationOfAlertTriggered() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if(DEFAULT_ALARM_SENDING_CONFIRMATION_PATTERN_LONG.equals((ApplicationSettings.getConfirmationFeedbackVibrationPattern(context)))){
            vibrateContinusly(vibrator,ONE_SECOND *Integer.parseInt(ALERT_CONFIRMATION_VIBRATION_DURATION));
        }else  if(ALARM_SENDING_CONFIRMATION_PATTERN_REPEATED_SHORT.equals((ApplicationSettings.getConfirmationFeedbackVibrationPattern(context)))){
            repeatedThreeShortVibrations(vibrator);
        }else  if(ALARM_SENDING_CONFIRMATION_PATTERN_THREESHORT_PAUSE_THREESHORT.equals((ApplicationSettings.getConfirmationFeedbackVibrationPattern(context)))){
            vibrateThreeShortPauseThreeShort(vibrator);
        }else  if(ALARM_SENDING_CONFIRMATION_PATTERN_SOS.equals((ApplicationSettings.getConfirmationFeedbackVibrationPattern(context)))){
            vibrateSOS(vibrator);
        }
    }

    private void activateAlert() {
        ApplicationSettings.setAlertActive(context, true);
        sendFirstAlert();
        registerLocationUpdate();
        scheduleFutureAlert();
    }

    public void deActivate() {
        Log.d(TAG, "Deactivating the triggered alert");
        ApplicationSettings.setAlertActive(context, false);
        locationManager.removeUpdates(locationPendingIntent(context));
        alarmManager2.cancel(Intents.alarmPendingIntent(context));
        ApplicationSettings.setFirstMsgWithLocationTriggered(context, false);
        ApplicationSettings.setFirstMsgSent(context, false);
    }

    private void sendFirstAlert() {
        CurrentLocationProvider currentLocationProvider = getCurrentLocationProvider();
        Location loc = getLocation(currentLocationProvider);
        if(loc != null) {
            ApplicationSettings.setFirstMsgWithLocationTriggered(context, true);
        } else {
//            ApplicationSettings.setFirstMsgWithLocationTriggered(context, false);
            scheduleFirstLocationAlert();
        }
        createPanicMessage().sendAlertMessage(loc);
    }

    PanicMessage createPanicMessage() {
        return new PanicMessage(context);
    }

    CurrentLocationProvider getCurrentLocationProvider() {
        return new CurrentLocationProvider(context);
    }

    private void scheduleFirstLocationAlert() {
        PendingIntent alarmPendingIntent = Intents.singleAlarmPendingIntent(context);
        long firstTimeTriggerAt = SystemClock.elapsedRealtime() + ONE_MINUTE * 1;             // we schedule this alarm after 1 minute
        alarmManager1.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTimeTriggerAt, alarmPendingIntent);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTimeTriggerAt, interval, alarmPendingIntent);
    }

    private void scheduleFutureAlert() {
        PendingIntent alarmPendingIntent = Intents.alarmPendingIntent(context);
        long firstTimeTriggerAt = SystemClock.elapsedRealtime() + ONE_MINUTE * ApplicationSettings.getAlertDelay(context);
        long interval = ONE_MINUTE * ApplicationSettings.getAlertDelay(context);
        alarmManager2.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTimeTriggerAt, interval, alarmPendingIntent);
    }

    private void registerLocationUpdate() {

//        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
//            locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
//        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
//            locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
//
        int threadRunCount = 0;
        while(!ApplicationSettings.isFirstMsgWithLocationTriggered(context) && threadRunCount < 4){
            try {
                Thread.sleep(20000);
                threadRunCount++;

                if (locationManager != null && locationPendingIntent(context) != null) {
                    locationManager.removeUpdates(locationPendingIntent(context));
                }
                if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                    locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
                if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
                Log.d(TAG, "threadRunCount = " + threadRunCount);
            } catch (SecurityException e ) {
                Log.e(TAG,"SecurityException exception "+e.getMessage());
                e.printStackTrace();
            }catch (InterruptedException e ) {
                Log.e(TAG,"SecurityException exception "+e.getMessage());
                e.printStackTrace();
            }
        }

        if (locationManager != null && locationPendingIntent(context) != null) {
            locationManager.removeUpdates(locationPendingIntent(context));
        }
        try{
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
        } catch (SecurityException e ) {
            Log.e(TAG, "SecurityException exception " + e.getMessage());
            e.printStackTrace();
        }
//        HomeActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Handler locationRefreshIntervalHandler = new Handler();
//                locationRefreshIntervalHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (locationManager != null && locationPendingIntent(context) != null) {
//                            locationManager.removeUpdates(locationPendingIntent(context));
//                        }
//                        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
//                            locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
//                        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
//                            locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
//                    }
//                }, 60 * 1000);          // after 1 minute
//            }
//        });

     }

    public boolean isActive() {
        return ApplicationSettings.isAlertActive(context);
    }

    public void vibrateForHapticFeedback() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //Code to fetch the Haptic feedback pattern
        if(DEFAULT_HAPTIC_FEEDBACK_PATTERN_CONTINUSLY.equals(ApplicationSettings.getHapticFeedbackVibrationPattern(context))){
            vibrateContinusly(vibrator,(ONE_SECOND *Integer.parseInt(ApplicationSettings.getConfirmationWaitVibrationDuration(context))));
        }else{
            vibrateEverySecond(vibrator,Integer.parseInt(ApplicationSettings.getConfirmationWaitVibrationDuration(context)));
        }


    }

    private void vibrateContinusly(Vibrator vibrator, int feedbackDuration){
        vibrator.vibrate(feedbackDuration);
    }

    private void vibrateEverySecond(Vibrator vibrator, int feedbackDuration){
        Log.d(TAG,"vibrate every second pattern selected and feedback duration is "+ feedbackDuration );

            vibrator.vibrate(getPattern(feedbackDuration), -1);
    }

    private long[] getPattern(int i) {
        switch (i) {
            case 1:
                return new long[]{0, 600, 400};
            case 2:
                return new long[]{0, 600, 400,600, 400};
            case 3:
                return new long[]{0, 600, 400,600, 400,600, 400};
            case 4:
                return new long[]{0, 600, 400,600, 400,600, 400,600, 400};
        }
        return  new long[]{0, 600, 400};
    }

    private void repeatedThreeShortVibrations(Vibrator vibrator) {
        long[] pattern = {0, 600, 400,600, 400,600, 400};
                vibrator.vibrate(pattern,-1);
    }

    private void vibrateThreeShortPauseThreeShort(Vibrator vibrator){
        long[] pattern = {0, 400,200,400,200,400,1000,400,200,400,200,400,500};
            vibrator.vibrate(pattern,-1);

    }
    // SOS: Three short - Three long - Three short
    private void vibrateSOS(Vibrator vibrator){
        long[] pattern = {0, 400,200,400,200,400,200, 1000,400,1000,400,1000,400, 400,200,400,200,400,200};
        vibrator.vibrate(pattern,-1);
    }

    private Location getLocation(CurrentLocationProvider currentLocationProvider) {
        Location location = null;
        int retryCount = 0;

        while (retryCount < MAX_RETRIES && location == null) {
            location = currentLocationProvider.getLocation();
            if (location == null) {
                try {
                    retryCount++;
                    Thread.sleep(LOCATION_WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Location wait InterruptedException", e);
                }
            }
        }
        return location;
    }

//    public AlertStatus getAlertStatus() {
//        if (isActive()) {
//            return AlertStatus.ACTIVE;
//        }
//        return AlertStatus.STANDBY;
//    }

    ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    public static final int MAX_RETRIES = 10;
    public static final int LOCATION_WAIT_TIME = 1000;
}