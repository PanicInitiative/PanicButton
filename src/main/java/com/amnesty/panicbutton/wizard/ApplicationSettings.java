package com.amnesty.panicbutton.wizard;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.amnesty.panicbutton.alert.AlertStatus;
import com.amnesty.panicbutton.model.SMSSettings;

public class ApplicationSettings {
    public static final String FIRST_RUN = "FIRST_RUN";

    public static void completeFirstRun(Context context) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putBoolean(FIRST_RUN, false);
        editor.commit();
    }

    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = sharedPreferences(context);
        return sharedPreferences.getBoolean(FIRST_RUN, true);
    }

    private static SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AlertStatus getAlertStatus(Context context) {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        if (!smsSettings.isConfigured())
            return AlertStatus.NOT_CONFIGURED;
        return AlertStatus.STANDBY;
    }
}