package com.apb.beacon.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.apb.beacon.HomeActivity;
import com.apb.beacon.model.DialerSettings;

/**
 * Created by sohjsolwin on 8/10/13.
 */
public class DialBroadcastReceiver extends BroadcastReceiver {
    static String BLANK = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        String code = BLANK;
        DialerSettings currentSettings = DialerSettings.retrieve(context);
        code = currentSettings.LaunchCode();


        if ((!code.equals(BLANK) && code != BLANK ) && intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            if (number.equals(code) || number == code) {

                setResultData(null);

                Intent home = new Intent(context, HomeActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(home);

            }
        }
    }
}
