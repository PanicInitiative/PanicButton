package com.apb.beacon.sms;

import android.telephony.SmsManager;
import android.util.Log;

public class SMSAdapter {
    private static final String LOG_TAG = SMSAdapter.class.getName();

    public void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = getSmsManager();
        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.i(LOG_TAG, "Sms sent: " + message);
        } catch (Exception exception) {
            Log.e(LOG_TAG, "Sending SMS failed " + exception.getMessage());
        }
    }

    SmsManager getSmsManager() {
        return SmsManager.getDefault();
    }
}