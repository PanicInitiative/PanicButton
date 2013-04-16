package com.amnesty.panicbutton.sms;

import android.telephony.SmsManager;

public class SMSAdapter {
    public void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}