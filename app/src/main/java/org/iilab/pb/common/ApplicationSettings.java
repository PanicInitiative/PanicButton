package org.iilab.pb.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.iilab.pb.R;

import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_NONE;
import static org.iilab.pb.common.AppConstants.DEFAULT_ALARM_INTERVAL;
import static org.iilab.pb.common.AppConstants.DEFAULT_HAPTIC_FEEDBACK_DURATION;
import static org.iilab.pb.common.AppConstants.FRESH_INSTALL_APP_RELEASE_NO;
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
    public static final String APP_UPDATED = "APP_UPDATED";
    public static final String CONFIRMATION_FEEDBACK = "CONFIRMATION_FEEDBACK";
    //fla specific to 1.5 release
    public static final String TRAINING_DONE = "TRAINING_DONE";

    public static Context getAppContext() {
        return getAppContext();
    }

    /*following methods are used to check whether the app is fresh install or an update.Its used to play explicitly 1.5 release major changes training sequence
    if the app is not a fresh install*/
    public static void setFirstRun(Context context, boolean isFirstRun) {
        saveBoolean(context, FIRST_RUN, isFirstRun);
    }

    public static boolean isFirstRun(Context context) {
        return sharedPreferences(context).getBoolean(FIRST_RUN, false);
    }

//    public static void setLocalDataInsertion(Context context, boolean flag) {
//        saveBoolean(context, HARDCODE_INSERT, flag);
//    }
//
//    public static boolean getLocalDataInsertion(Context context) {
//        return sharedPreferences(context).getBoolean(HARDCODE_INSERT, false);
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

    public static void setPBSupportedLanguages(Context context, String lang) {
        saveString(context, SUPPORTED_LANGUAGES, lang);
    }

    public static String getSupportedLanguages(Context context) {
        return sharedPreferences(context).getString(SUPPORTED_LANGUAGES, null);
    }

    public static void addDBLoadedLanguage(Context context, String language) {
        if(!getDBLoadedLanguages(context).contains(language))
            saveString(context, DB_LOADED_LANGUAGES, getDBLoadedLanguages(context).concat("," + language));
    }

    public static String getDBLoadedLanguages(Context context) {
        return sharedPreferences(context).getString(DB_LOADED_LANGUAGES, "");
    }

    public static int getLastUpdatedVersion(Context context) {
        return sharedPreferences(context).getInt(LAST_UPDATED_VERSION, FRESH_INSTALL_APP_RELEASE_NO);
    }

    public static void setLastUpdatedVersion(Context context, int versionNumber) {
        saveInt(context, LAST_UPDATED_VERSION, versionNumber);
    }

//    public static int getLastUpdatedDBVersion(Context context) {
//        return sharedPreferences(context).getInt(LAST_UPDATED_DB_VERSION, -1);
//    }
//
//    public static void setLastUpdatedDBVersion(Context context, int versionNumber) {
//        saveInt(context, LAST_UPDATED_DB_VERSION, versionNumber);
//    }

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


    public static void setInitialClicksForAlertTrigger(Context context, String initialPresses) {
        saveString(context,context.getString(R.string.initialPressesKey),initialPresses);
    }
    public static String getInitialClicksForAlertTrigger(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.initialPressesKey), context.getString(R.string.initialPressesDefault));
    }


    public static String getHapticFeedbackVibrationPattern(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.hapticFeedbackVibrationPatternKey), context.getString(R.string.hapticFeedbackDefaultPattern));
    }

    public static String getConfirmationWaitVibrationDuration(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.confirmationWaitTimeKey), DEFAULT_HAPTIC_FEEDBACK_DURATION);
    }

    public static void setConfirmationFeedbackVibrationPattern(Context context, String confirmationFeedbackPattern) {
        saveString(context,context.getString(R.string.alertSendingConfirmationVibrationKey),confirmationFeedbackPattern);
    }
    public static String getConfirmationFeedbackVibrationPattern(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.alertSendingConfirmationVibrationKey), ALARM_SENDING_CONFIRMATION_PATTERN_NONE);
    }

    public static String getAlarmNotConfirmedPattern(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.alertNotConfirmedKey), context.getString(R.string.alertNotConfirmedDefault));
    }
    public static String getInitialClicksMaxTimeLimit(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.initialTimeKey), context.getString(R.string.initialTimeDefault));
    }
    public static String getTriggerSettings(Context context) {
        return sharedPreferences(context).getString(context.getString(R.string.triggerSettingsKey), context.getString(R.string.initialPressesDefault));
    }
    public static void setTrainingDoneRelease1_5(Context context, Boolean flag) {
        saveBoolean(context, TRAINING_DONE, flag);
    }

    public static Boolean isTrainingDoneRelease1_5(Context context) {
        return sharedPreferences(context).getBoolean(TRAINING_DONE, false);
    }

    public static void setAppUpdated(Context context, boolean isAppUpdated) {
        saveBoolean(context, APP_UPDATED, isAppUpdated);
    }

    public static boolean isAppUpdated(Context context) {
        return sharedPreferences(context).getBoolean(APP_UPDATED, false);
    }
    public static void setConfirmationFeedback(Context context, Boolean confirmationFeedback) {
        saveBoolean(context, CONFIRMATION_FEEDBACK, confirmationFeedback);
    }
    public static boolean isConfirmationFeedback(Context context) {
        return sharedPreferences(context).getBoolean(CONFIRMATION_FEEDBACK, false);
    }

}
