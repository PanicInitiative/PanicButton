package com.amnesty.panicbutton.alert;

import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;

public class PanicAlertMessage {
    public static final int TWITTER_MAX_LENGTH = 140;
    private String message;
    private String location;

    public PanicAlertMessage(String message, String location) {
        this.message = message;
        this.location = location;
    }

    public String getSMSText() {        
        return trimMessage(MAX_USER_DATA_SEPTETS);
    }

    public String getTwitterText() {
        return trimMessage(TWITTER_MAX_LENGTH);
    }

    private String trimMessage(int maxLength) {
        String smsText = message + location;
        if(smsText.length() > maxLength) {
            smsText = message.substring(0, (maxLength - location.length()) ) + location;
        }
        return smsText;
    }
}