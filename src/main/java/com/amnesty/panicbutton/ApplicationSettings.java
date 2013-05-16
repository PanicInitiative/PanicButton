package com.amnesty.panicbutton;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.amnesty.panicbutton.alert.AlertStatus;
import com.amnesty.panicbutton.model.SMSSettings;

public class ApplicationSettings {
    public static final String FIRST_RUN = "FIRST_RUN";
    private static final String PASS_CODE = "PASS_CODE";

    public static void completeFirstRun(Context context) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putBoolean(FIRST_RUN, false);
        editor.commit();
    }

    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = sharedPreferences(context);
        return sharedPreferences.getBoolean(FIRST_RUN, true);
    }

    public static AlertStatus getAlertStatus(Context context) {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        if (!smsSettings.isConfigured())
            return AlertStatus.NOT_CONFIGURED;
        return AlertStatus.STANDBY;
    }

    public static void savePassword(Context context, String password) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.putString(PASS_CODE, password);
        editor.commit();
    }

    private static SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}