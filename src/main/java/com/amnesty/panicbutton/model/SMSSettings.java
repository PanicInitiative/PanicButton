package com.amnesty.panicbutton.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SMSSettings {
    public static final String PHONE_NUMBER = "PHONE_NUMBER_";
    public static final String SMS_MESSAGE = "SMS_MESSAGE";

    private List<String> phoneNumbers = new ArrayList<String>();
    private String message="";

    public SMSSettings(List<String> phoneNumbers, String message) {
        this.phoneNumbers = phoneNumbers;
        this.message = message;
    }

    public static void save(Context context, SMSSettings smsSettings) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        int i = 0;
        for (String phoneNumber : smsSettings.phoneNumbers) {
            editor.putString(PHONE_NUMBER + i++, phoneNumber) ;
        }
        editor.putString(SMS_MESSAGE, smsSettings.message);
        editor.commit();
    }

    public static SMSSettings retrieve(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, String> allPreferences = (Map<String, String>) sharedPreferences.getAll();
        List<String> retrievedPhoneNumbers = retrievePhoneNumbers(allPreferences);
        return new SMSSettings(retrievedPhoneNumbers, allPreferences.get(SMS_MESSAGE));
    }

    private static List<String> retrievePhoneNumbers(Map<String, String> allPreferences) {
        List<String> retrievedPhoneNumbers = new ArrayList<String>();
        List<String> sortedKeys = new ArrayList<String>(allPreferences.keySet());
        Collections.sort(sortedKeys);
        for (String preferenceKey : sortedKeys) {
            if(preferenceKey.startsWith(PHONE_NUMBER)) {
                retrievedPhoneNumbers.add(allPreferences.get(preferenceKey));
            }
        }
        return retrievedPhoneNumbers;
    }

    public String getPhoneNumber(int index) {
        return phoneNumbers.size() > index ? phoneNumbers.get(index) : "";
    }

    public String getMessage() {
        return message;
    }
}