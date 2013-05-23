package com.apb.panicbutton.alert;

import android.content.Context;
import android.location.Location;
import com.apb.panicbutton.location.LocationFormatter;
import com.apb.panicbutton.model.SMSSettings;
import com.apb.panicbutton.sms.SMSAdapter;
import com.apb.panicbutton.twitter.ShortCodeSettings;
import com.apb.panicbutton.twitter.TwitterSettings;

import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;
import static com.apb.panicbutton.twitter.TwitterSettings.retrieve;

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
        if (TwitterSettings.isEnabled(context)) {
            tweet(retrieve(context));
        }
    }

    private void sendSMS() {
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        SMSAdapter smsAdapter = getSMSAdapter();
        String message = getSMSText(smsSettings.trimmedMessage());

        for (String phoneNumber : smsSettings.validPhoneNumbers()) {
            smsAdapter.sendSMS(phoneNumber, message);
        }
    }

    private void tweet(TwitterSettings twitterSettings) {
        SMSAdapter smsAdapter = getSMSAdapter();
        ShortCodeSettings shortCodeSettings = twitterSettings.getShortCodeSettings();
        String message = getTwitterText(twitterSettings.getMessage());
        smsAdapter.sendSMS(shortCodeSettings.getShortCode(), message);
    }

    private String getSMSText(String message) {
        return trimMessage(message, MAX_USER_DATA_SEPTETS);
    }

    private String getTwitterText(String message) {
        return trimMessage(message, TWITTER_MAX_LENGTH);
    }

    private String trimMessage(String message, int maxLength) {
        String locationString = new LocationFormatter(location).format();
        String smsText = message + locationString;
        if(smsText.length() > maxLength) {
            smsText = message.substring(0, (maxLength - locationString.length()) ) + locationString;
        }
        return smsText;
    }

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }
}