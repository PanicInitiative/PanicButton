package com.amnesty.panicbutton.common;

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
import com.amnesty.panicbutton.R;
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
        String prefix = getString(R.string.characters_left);

        messageLimitWatcher = new MessageLimitWatcher(charactersLeftView, prefix, maxCharacters);
        messageEditText.addTextChangedListener(messageLimitWatcher);
        InputFilter[] filters = {new InputFilter.LengthFilter(maxCharacters)};
        messageEditText.setFilters(filters);

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
