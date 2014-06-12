package org.iilab.pb.common;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class Intents {
    private static final String PREFIX = "org.iilab.pb";
    public static final String LOCATION_UPDATE_ACTION = PREFIX + ".LOCATION_UPDATE_ACTION";
    public static final String SEND_ALERT_ACTION = PREFIX + ".SEND_ALERT_ACTION";

    public static PendingIntent alarmPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(SEND_ALERT_ACTION), FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent locationPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(LOCATION_UPDATE_ACTION), FLAG_UPDATE_CURRENT);
    }
}
