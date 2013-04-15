package com.amnesty.panicbutton.shadow;

import android.app.PendingIntent;
import android.telephony.SmsManager;
import android.text.TextUtils;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.shadows.ShadowSmsManager;

import java.util.ArrayList;
import java.util.List;

@Implements(SmsManager.class)
public class ShadowCustomSmsManager extends ShadowSmsManager {
    private List<TextSmsParams> textSmsParamsList = new ArrayList<TextSmsParams>();

    @Override
    @Implementation
    public void sendTextMessage(String destinationAddress, String scAddress, String text,
                                PendingIntent sentIntent, PendingIntent deliveryIntent) {

        if (TextUtils.isEmpty(destinationAddress))
            throw new IllegalArgumentException("Invalid destinationAddress");

        if (TextUtils.isEmpty(text))
            throw new IllegalArgumentException("Invalid message body");

        textSmsParamsList.add(new TextSmsParams(destinationAddress,scAddress,text,sentIntent,deliveryIntent));
    }

    public List<TextSmsParams> getAllSentTextMessageParams() {
        return textSmsParamsList;
    }

    @Override
    public TextSmsParams getLastSentTextMessageParams() {
        return textSmsParamsList.get(textSmsParamsList.size() - 1);
    }
}