package com.apb.beacon.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Map;

public class DialerSettings {
    public static final String LAUNCH_CODE = "LAUNCH_CODE";
    private static final int MASK_LIMIT = 0;
    public static final String DEFAULT = "72642";

    private String launchCode;

    public DialerSettings(String launchCode) {
        this.launchCode = launchCode;
    }

    public static void save(Context context, DialerSettings dialerSettings) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(LAUNCH_CODE, dialerSettings.launchCode);
        editor.commit();
    }

    public static DialerSettings retrieve(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, String> allPreferences = (Map<String, String>) sharedPreferences.getAll();
        return new DialerSettings(allPreferences.get(LAUNCH_CODE));
    }

    public String LaunchCode() {
        return launchCode == null? DEFAULT : launchCode;
    }

    private String mask(String code) {
        if (code == null || code.length() < MASK_LIMIT) return code;
        int length = code.length();
        String prefix = code.substring(0, length - MASK_LIMIT).replaceAll(".", "*");
        return prefix + code.substring(length - MASK_LIMIT);
    }

    public boolean isConfigured() {
        if (launchCode == null || launchCode.isEmpty()) return false;
        if (!TextUtils.isEmpty(launchCode)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DialerSettings that = (DialerSettings) o;

        if (!launchCode.equals(that.launchCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = launchCode.hashCode();
        return result;
    }
}
