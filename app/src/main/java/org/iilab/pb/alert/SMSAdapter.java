package org.iilab.pb.alert;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import org.iilab.pb.common.ApplicationSettings;

public class SMSAdapter {
    private static final String TAG = SMSAdapter.class.getName();

    public void sendSMS(Context context, String phoneNumber, String message) {
        if(!ApplicationSettings.isFirstMsgSent(context)){
            ApplicationSettings.setFirstMsgSent(context, true);
        }
        SmsManager smsManager = getSmsManager();
        Log.d(TAG, "Sms manager is : " +  getSmsManager());
        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d(TAG, "Sms sent: " + message);
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.e(TAG, "Sending SMS failed " + exception.getMessage());
        }
    }

    SmsManager getSmsManager() {
        return SmsManager.getDefault();
    }
}