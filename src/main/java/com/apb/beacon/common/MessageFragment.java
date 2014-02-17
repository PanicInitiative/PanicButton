package com.apb.beacon.common;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.wizard.ActionButtonStateListener;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import static java.lang.String.valueOf;

public class MessageFragment extends RoboFragment {
    @InjectView(R.id.characters_left_view)
    private TextView charactersLeftView;

    @InjectView(R.id.message_fragment_header)
    private TextView messageHeaderView;

    @InjectView(R.id.message_edit_text)
    private EditText messageEditText;

    private MessageLimitWatcher messageLimitWatcher;
    private int maxCharacters;
    private String messageHeader;
    private ActionButtonStateListener actionButtonStateListener;

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray typedArray = activity.obtainStyledAttributes(attrs, R.styleable.MessageFragmentArguments);
        maxCharacters = typedArray.getInt(R.styleable.MessageFragmentArguments_max_characters, -1);
        messageHeader = typedArray.getString(R.styleable.MessageFragmentArguments_message_header);
        typedArray.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setActionButtonStateListener(ActionButtonStateListener actionButtonStateListener){
        this.actionButtonStateListener = actionButtonStateListener;

        String prefix = getString(R.string.characters_left);
        messageLimitWatcher = new MessageLimitWatcher(charactersLeftView, prefix, maxCharacters, actionButtonStateListener);
        messageEditText.addTextChangedListener(messageLimitWatcher);
        InputFilter[] filters = {new InputFilter.LengthFilter(maxCharacters)};
        messageEditText.setFilters(filters);
        messageEditText.setSelection(messageEditText.getText().length());

        charactersLeftView.setText(prefix + valueOf(maxCharacters - messageEditText.getText().length()));
        messageHeaderView.setText(messageHeader);
    }

    public String getMessage() {
        return messageEditText.getText().toString();
    }

    public void setMessage(String message) {
        if (message != null) {
            messageEditText.setText(message);
        }
    }
}
