package com.amnesty.panicbutton.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class MessageLimitWatcher implements TextWatcher {
    private TextView textView;
    private int maxCount;

    public MessageLimitWatcher(TextView textView, int maxCount) {
        this.textView = textView;
        this.maxCount = maxCount;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textView.setText(String.valueOf(maxCount - s.length()));
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }
}