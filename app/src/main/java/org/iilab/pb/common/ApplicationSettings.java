package org.iilab.pb.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.iilab.pb.R;
import static org.iilab.pb.common.AppConstants.DEFAULT_ALARM_INTERVAL;
import static org.iilab.pb.common.AppConstants.DEFAULT_ALARM_NOT_CONFIRMED_NONE;
import static org.iilab.pb.common.AppConstants.DEFAULT_ALARM_SENDING_CONFIRMATION_PATTERN_LONG;
import static org.iilab.pb.common.AppConstants.DEFAULT_HAPTIC_FEEDBACK_DURATION;
import static org.iilab.pb.common.AppConstants.DEFAULT_HAPTIC_FEEDBACK_PATTERN_CONTINUSLY;
import static org.iilab.pb.common.AppConstants.DEFAULT_INITIAL_CLICKS_ALERT_TRIGGER;
import static org.iilab.pb.common.AppConstants.DEFAULT_INITIAL_TIME_FOR_ALARM_TRIGGER;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED;



public class ApplicationSettings extends Application {

    private static final int ALERT_FREQUENCY = 10 * 60000;           // 10 minute

    public static final String FIRST_RUN = "FIRST_RUN";
    public static final String HARDCODE_INSERT = "HARDCODE_INSERT";
    public static final String LAST_RUN = "LAST_RUN";
    private static final String PASS_CODE = "PASS_CODE";
    private static final String IS_ALERT_ACTIVE = "IS_ALERT_ACTIVE";
    private static final String WIZARD_STATE = "WIZARD_STATE";
    public static final String BEST_LOCATION = "BEST_LOCATION";
    public static final String SELECTED_LANGUAGE = "SELECTED_LANGUAGE";
    public static final String LAST_UPDATED_VERSION = "LAST_UPDATED_VERSION";
    public static final String LAST_UPDATED_DB_VERSION = "LAST_UPDATED_DB_VERSION";
    public static final String ALERT_DELAY = "ALERT_DELAY";
    public static final String IS_FIRST_MSG_WITH_LOCATION_TRIGGERED = "is_first_msg_with_location_triggered";
    public static final String IS_FIRST_MSG_SENT = "is_first_msg_sent";
    public static final String SUPPORTED_LANGUAGES = "SUPPORTED_LANGUAGES";
    public static final String DB_LOADED_LANGUAGES = "DB_LOADED_LANGUAGES";


//TODO for testing vibrations--remove during release
    public static final String VIBRATION_DURATION_SHORT = "shortVibration";
    public static final String VIBRATION_PAUSE_SHORT = "shortPause";
    public static final String VIBRATION_DURATION_LONG = "LongVibration";
    public static final String VIBRATION_PAUSE_LONG = "longPause";

    public static Context getAppContext() {
        return getAppContext();
    }

    // these 2 methods with first time run won't be needed any more. I'll get rid of it after further analysis.
    public static void setFirstRun(Context context, boolean isFirstRun) {
        saveBoolean(context, FIRST_RUN, isFirstRun);
    }

    public static boolean isFirstRun(Context context) {
        return sharedPreferences(context).getBoolean(FIRST_RUN, true);
    }


//    public static void setRestartedSetup(Context context, boolean isRestartedSetup) {
//        saveBoolean(context, RESTARTED_SETUP, isRestartedSetup);
//    }
//
//    public static boolean isRestartedSetup(Context context) {
//        return sharedPreferences(context).getBoolean(RESTARTED_SETUP, false);
//    }

    public static void setLocalDataInsertion(Context context, boolean flag) {
        saveBoolean(context, HARDCODE_INSERT, flag);
    }

    public static boolean getLocalDataInsertion(Context context) {
        return sharedPreferences(context).getBoolean(HARDCODE_INSERT, false);
    }

//    public static long getLastRunTimeInMillis(Context context) {
//        return sharedPreferences(context).getLong(LAST_RUN, -1);
//    }
//
//    public static void setLastRunTimeInMillis(Context context, Long time) {
//        saveLong(context, LAST_RUN, time);
//    }

    public static void savePassword(Context context, String password) {
        saveString(context, PASS_CODE, password);
    }

    public static boolean passwordMatches(Context context, String otherPassword) {
        String actualPassword = sharedPreferences(context).getString(PASS_CODE, "");
        return actualPassword.equals(otherPassword);
    }

    public static boolean isAlertActive(Context context) {
        return sharedPreferences(context).getBoolean(IS_ALERT_ACTIVE, false);
    }

    public static void setAlertActive(Context context, boolean isActive) {
        saveBoolean(context, IS_ALERT_ACTIVE, isActive);
    }


    public static int getWizardState(Context context) {
        return sharedPreferences(context).getInt(WIZARD_STATE, WIZARD_FLAG_HOME_NOT_CONFIGURED);
    }

    public static void setWizardState(Context context, int state) {
        saveInt(context, WIZARD_STATE, state);
    }

    public static Location getCurrentBestLocation(Context context) {
        String locationJson = sharedPreferences(context).getString(BEST_LOCATION, null);
        return (locationJson == null) ? null : constructLocation(locationJson);
    }

    private static Location constructLocation(String locationJson) {
        Location location = new Gson().fromJson(locationJson, Location.class);
        long timeDelta = System.currentTimeMillis() - location.getTime();
        return (timeDelta <= ALERT_FREQUENCY) ? location : null;
    }

    public static void setCurrentBestLocation(Context context, Location location) {
        saveString(context, BEST_LOCATION, new Gson().toJson(location));
    }

    private static SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static void saveLong(Context context, String key, Long value) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static void saveInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static String getSelectedLanguage(Context context) {
        return sharedPreferences(context).getString(SELECTED_LANGUAGE, "en");
    }

    public static void setSelectedLanguage(Context context, String lang) {
        saveString(context, SELECTED_LANGUAGE, lang);
    }

    public static void setSupportedLanguages(Context context, String lang) {
        saveString(context, SUPPORTED_LANGUAGES, lang);
    }

    public static String getSupportedLanguages(Context context) {
        return sharedPreferences(context).getString(SUPPORTED_LANGUAGES, null);
    }

    public static void addDBLoadedLanguage(Context context, String language) {
        saveString(context, DB_LOADED_LANGUAGES, getDBLoadedLanguages(context).concat("," + language));
    }

    public static String getDBLoadedLanguages(Context context) {
        return sharedPreferences(context).getString(DB_LOADED_LANGUAGES, "");
    }

    public static int getLastUpdatedVersion(Context context) {
        return sharedPreferences(context).getInt(LAST_UPDATED_VERSION, -1);
    }

    public static void setLastUpdatedVersion(Context context, int versionNumber) {
        saveInt(context, LAST_UPDATED_VERSION, versionNumber);
    }

    public static int getLastUpdatedDBVersion(Context context) {
        return sharedPreferences(context).getInt(LAST_UPDATED_DB_VERSION, -1);
    }

    public static void setLastUpdatedDBVersion(Context context, int versionNumber) {
        saveInt(context, LAST_UPDATED_DB_VERSION, versionNumber);
    }

    public static void setAlertDelay(Context context, int alertDelay) {
        saveInt(context, ALERT_DELAY, alertDelay);
    }

    public static int getAlertDelay(Context context) {
        return sharedPreferences(context).getInt(ALERT_DELAY, DEFAULT_ALARM_INTERVAL);
    }

    public static void setFirstMsgWithLocationTriggered(Context context, Boolean flag) {
        saveBoolean(context, IS_FIRST_MSG_WITH_LOCATION_TRIGGERED, flag);
    }

    public static Boolean isFirstMsgWithLocationTriggered(Context context) {
        return sharedPreferences(context).getBoolean(IS_FIRST_MSG_WITH_LOCATION_TRIGGERED, false);
    }

    public static void setFirstMsgSent(Context context, Boolean flag) {
        saveBoolean(context, IS_FIRST_MSG_SENT, flag);
    }

    public static Boolean isFirstMsgSent(Context context) {
        return sharedPreferences(context).getBoolean(IS_FIRST_MSG_SENT, false);
    }


    public static boolean isHardwareTriggerServiceEnabled(Context context) {
        String powerButtonTriggerStatus = sharedPreferences(context).getString(context.getString(R.string.powerButtonTriggerStatus_key), context.getString(R.string.activate_power_button_trigger));
        return (context.getString(R.string.activate_power_button_trigger).equals(powerButtonTriggerStatus) ? true : false);
    }


    public static String getInitialClicksForAlertTrigger(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.initialClicks_key), DEFAULT_INITIAL_CLICKS_ALERT_TRIGGER);
    }


    public static String getHapticFeedbackVibrationPattern(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.hapticFeedbackVibrationPattern_key), DEFAULT_HAPTIC_FEEDBACK_PATTERN_CONTINUSLY);
    }

    public static String getConfirmationWaitVibrationDuration(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.confirmationWaitTime_key), DEFAULT_HAPTIC_FEEDBACK_DURATION);
    }
    public static String getConfirmationFeedbackVibrationPattern(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.alertSendingConfirmationVibration_key), DEFAULT_ALARM_SENDING_CONFIRMATION_PATTERN_LONG);
    }

    public static String getAlarmNotConfirmedPattern(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.alertNotConfirmed_key), DEFAULT_ALARM_NOT_CONFIRMED_NONE);
    }
    public static String getInitialClicksMaxTimeLimit(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.initialTime_key), DEFAULT_INITIAL_TIME_FOR_ALARM_TRIGGER);
    }
    public static String getShortVibration(Context context) {
        return sharedPreferences(context).getString(VIBRATION_DURATION_SHORT, "400");
    }
    public static String getShortPause(Context context) {
        return sharedPreferences(context).getString(VIBRATION_PAUSE_SHORT, "200");
    }
    public static String getLongVibration(Context context) {
        return sharedPreferences(context).getString(VIBRATION_DURATION_LONG, "600");
    }
    public static String getLongPause(Context context) {
        return sharedPreferences(context).getString(VIBRATION_PAUSE_LONG, "400");
    }
}
