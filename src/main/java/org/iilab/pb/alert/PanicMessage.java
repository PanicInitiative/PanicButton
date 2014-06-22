package org.iilab.pb.alert;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.location.LocationFormatter;
import org.iilab.pb.model.SMSSettings;

import android.content.Context;
import android.location.Location;


import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;

public class PanicMessage {
    public static final int TWITTER_MAX_LENGTH = 140;
    private Context context;
    private Location location;

    public PanicMessage(Context context) {
        this.context = context;
    }

    public void send(Location location) {
        this.location = location;
        sendSMS();
    }

    private void sendSMS() {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        SMSAdapter smsAdapter = getSMSAdapter();
        String message = getSMSText(smsSettings.trimmedMessage());

        for (String phoneNumber : smsSettings.validPhoneNumbers()) {
            smsAdapter.sendSMS(phoneNumber, message);
        }
    }

    private String getSMSText(String message) {
        return trimMessage(message, MAX_USER_DATA_SEPTETS);
    }

//    private String getTwitterText(String message) {
//        return trimMessage(message, TWITTER_MAX_LENGTH);
//    }

    private String trimMessage(String message, int maxLength) {
        String locationString = new LocationFormatter(location).format(context);
        String smsText = message + locationString;
        if (locationString == "") {
        	if (ApplicationSettings.getFirstMsgWithLocationTriggered(context)) {
        		smsText = AppConstants.CUSTOM_ALERT_MSG_WHEN_LOCATION_NOT_FOUND;
        	}
        }
        if(smsText.length() > maxLength) {
            smsText = message.substring(0, (maxLength - locationString.length()) ) + locationString;
        }
        return smsText;
    }

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }
}