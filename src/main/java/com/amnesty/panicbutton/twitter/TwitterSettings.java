package com.amnesty.panicbutton.twitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

public class TwitterSettings {
    public static final String TWITTER_COUNTRY = "TWITTER_COUNTRY";
    public static final String TWITTER_SERVICE_PROVIDER = "TWITTER_SERVICE_PROVIDER";
    public static final String TWITTER_SHORT_CODE = "TWITTER_SHORT_CODE";
    public static final String TWITTER_MESSAGE = "TWITTER_MESSAGE";
    private ShortCodeSettings shortCodeSettings;
    private String message;


    public TwitterSettings(ShortCodeSettings shortCodeSettings, String message) {
        this.shortCodeSettings = shortCodeSettings;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ShortCodeSettings getShortCodeSettings() {
        return shortCodeSettings;
    }

    public static void save(Context context, TwitterSettings twitterSettings) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        ShortCodeSettings shortCodeSettings = twitterSettings.getShortCodeSettings();
        editor.putString(TWITTER_COUNTRY, shortCodeSettings.getCountry());
        editor.putString(TWITTER_SERVICE_PROVIDER, shortCodeSettings.getServiceProvider());
        editor.putString(TWITTER_SHORT_CODE, shortCodeSettings.getShortCode());
        editor.putString(TWITTER_MESSAGE, twitterSettings.message);
        editor.commit();
    }

    public static TwitterSettings retrieve(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, String> allPreferences = (Map<String, String>) sharedPreferences.getAll();
        return new TwitterSettings(shortCodeSettings(allPreferences), allPreferences.get(TWITTER_MESSAGE));
    }

    private static ShortCodeSettings shortCodeSettings(Map<String, String> allPreferences) {
        ShortCodeSettings shortCodeSettings = new ShortCodeSettings(allPreferences.get(TWITTER_COUNTRY));
        shortCodeSettings.setServiceProvider(allPreferences.get(TWITTER_SERVICE_PROVIDER));
        shortCodeSettings.setShortCode(allPreferences.get(TWITTER_SHORT_CODE));
        return shortCodeSettings;
    }
}
