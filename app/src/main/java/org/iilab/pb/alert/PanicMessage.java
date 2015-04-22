package org.iilab.pb.alert;

import android.content.Context;
import android.location.Location;

import org.iilab.pb.R;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.location.LocationFormatter;
import org.iilab.pb.model.SMSSettings;

import java.util.List;

import static android.telephony.SmsMessage.MAX_USER_DATA_SEPTETS;

public class PanicMessage {
//    public static final int TWITTER_MAX_LENGTH = 140;
    private Context context;
    private Location location;

    public PanicMessage(Context context) {
        this.context = context;
    }

    public void sendAlertMessage(Location location) {
        this.location = location;

        SMSSettings smsSettings = SMSSettings.retrieve(context);

        String message = getSMSText(smsSettings.trimmedMessage());
        List<String> phoneNumbers = getPhoneNumbers();

        sendMessage(message, phoneNumbers);
    }

    public void sendStopAlertMessage() {
        String message = context.getResources().getString(R.string.stop_alert_message);

        sendMessage(message, getPhoneNumbers());
    }

    private List<String> getPhoneNumbers(){
        SMSSettings smsSettings = SMSSettings.retrieve(context);
        return smsSettings.validPhoneNumbers();
    }

    private void sendMessage(String message, List<String> phoneNumbers){
        SMSAdapter smsAdapter = getSMSAdapter();
        for (String phoneNumber : phoneNumbers) {
            smsAdapter.sendSMS(context, phoneNumber, message);
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
        boolean isFirstMsgSent = ApplicationSettings.isFirstMsgSent(context);

        String smsText = "";
        smsText = message;
        
        if(locationString.equals("")){
        	locationString = context.getResources().getString(R.string.location_not_found);
        }

        smsText = smsText + " - " + locationString;
        
        if(smsText.length() > maxLength) {
            smsText = message.substring(0, (maxLength - locationString.length()) ) + locationString;
        }
        return smsText;
    }

    SMSAdapter getSMSAdapter() {
        return new SMSAdapter();
    }
}