package com.apb.beacon.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.apb.beacon.ApplicationSettings;

import java.util.Map;

public class GuardianSettings {
    public static final String BLANK = "";

    private Long time;

    public GuardianSettings(Long time) {
        this.time = time;
    }

    public static void save(Context context, GuardianSettings guardianSettings) {
        ApplicationSettings.saveGuardianTime(context, guardianSettings.GuardianTime());
    }

    public static GuardianSettings retrieve(Context context) {
        return new GuardianSettings(ApplicationSettings.getGuardianTime(context));
    }

    public Long GuardianTime() {
        return time == null? ApplicationSettings.DEFAULT_GUARDIAN_TIME : time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuardianSettings that = (GuardianSettings) o;

        if (!time.equals(that.time)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = time.hashCode();
        return result;
    }
}
