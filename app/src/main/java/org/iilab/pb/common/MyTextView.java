package org.iilab.pb.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import org.iilab.pb.R;

/**
 * This is a customized text view holding the values of styled attributes of message fragment
 */
public class MyTextView extends TextView {
    public String getMessageHeader() {
        return mMessageHeader;
    }

    public void setMessageHeader(String mMessageHeader) {
        this.mMessageHeader = mMessageHeader;
    }

    public int getMaxCharacters() {
        return mMaxCharacters;
    }

    public void setMaxCharacters(int mMaxCharacters) {
        this.mMaxCharacters = mMaxCharacters;
    }

    private String mMessageHeader;
    private int mMaxCharacters;
    private String mStopAlertHeader;

    public MyTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public String getStopAlertMsgHeader() {
        return mStopAlertHeader;
    }

    public void setStopAlertHeader(String stopAlertHeader) {
        this.mStopAlertHeader = stopAlertHeader;
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.MessageFragmentArguments);
        mMessageHeader = arr.getString(R.styleable.MessageFragmentArguments_message_header);
        mMaxCharacters = arr.getInteger(R.styleable.MessageFragmentArguments_max_characters, 0);
        mStopAlertHeader= arr.getString(R.styleable.MessageFragmentArguments_stop_alert_message_header);
        arr.recycle();

    }

}
