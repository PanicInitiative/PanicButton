package com.apb.panicbutton.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import static java.lang.String.valueOf;

public class MessageLimitWatcher implements TextWatcher {
    private TextView textView;
    private String prefix;
    private int maxCount;

    public MessageLimitWatcher(TextView textView, String prefix, int maxCount) {
        this.textView = textView;
        this.prefix = prefix;
        this.maxCount = maxCount;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textView.setText(prefix + valueOf(maxCount - s.length()));
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }
}