package org.iilab.pb.common;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.String.valueOf;

public class MessageLimitWatcher implements TextWatcher {
    private TextView textView;
    private String prefix;
    private int maxCount;
    private Button bAction;

    public MessageLimitWatcher(TextView textView, String prefix, int maxCount, final Button bAction) {
        this.textView = textView;
        this.prefix = prefix;
        this.maxCount = maxCount;
        this.bAction = bAction;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ((maxCount - s.length()) < AppConstants.WARNING_TRAINING_MESSAGE_MINIMUM_CHARACTER)
            textView.setTextColor(Color.RED);
        else
            textView.setTextColor(Color.BLACK);
        textView.setText(prefix + valueOf(maxCount - s.length()));
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
//        if(s.toString().trim().equals("")){
//            WizardActivity.actionButton.setEnabled(false);
//        }
//        else{
//            WizardActivity.actionButton.setEnabled(true);
//        }
        if (bAction != null) {
            bAction.setEnabled(!s.toString().trim().equals(""));
        } else {
            Log.e(">>>>>>>>>>", "actionButtonStateListener = null");
        }
    }
}