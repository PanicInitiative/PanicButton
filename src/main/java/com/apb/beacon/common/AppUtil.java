package com.apb.beacon.common;

import android.content.Context;
import android.text.Html;
import android.widget.EditText;

public class AppUtil {
    public static void setError(Context context, EditText editText, int messageId) {
        editText.setError(
                Html.fromHtml("<font color='red'>"
                + context.getString(messageId)
                + "</font>")
        );
    }
}
