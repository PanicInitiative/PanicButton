package com.apb.beacon.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.Html;
import android.widget.EditText;

import java.util.List;

public class AppUtil {
    public static void setError(Context context, EditText editText, int messageId) {
        editText.setError(
                Html.fromHtml("<font color='red'>"
                        + context.getString(messageId)
                        + "</font>")
        );
    }

    public static void close(Context context) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        setDefaultPAckage(context, startMain);

        context.startActivity(startMain);
    }

    private static void setDefaultPAckage(Context context, Intent startMain) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(homeIntent, 0);
        if (activities.size() > 0) {
            String className = activities.get(0).activityInfo.packageName;
            startMain.setPackage(className);
        }
    }
}
