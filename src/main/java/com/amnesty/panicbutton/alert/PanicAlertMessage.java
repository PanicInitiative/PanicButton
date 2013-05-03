package com.amnesty.panicbutton.alert;

import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;

public class PanicAlertMessage {
    private String message;
    private String location;

    public PanicAlertMessage(String message, String location) {
        this.message = message;
        this.location = location;
    }

    public String getSMSText() {
        String smsText = message + location;
        if(smsText.length() > MAX_USER_DATA_SEPTETS) {
            smsText = message.substring(0, (MAX_USER_DATA_SEPTETS - location.length()) ) + location;
        }
        return smsText;
    }
}