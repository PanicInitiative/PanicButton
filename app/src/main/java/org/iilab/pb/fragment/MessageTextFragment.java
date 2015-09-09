package org.iilab.pb.fragment;

import org.iilab.pb.R;
import org.iilab.pb.common.MessageLimitWatcher;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import static java.lang.String.valueOf;

public class MessageTextFragment extends Fragment {

    private TextView charactersLeftView;
    private TextView messageHeaderView;
    private EditText messageEditText;

    private MessageLimitWatcher messageLimitWatcher;
    private int maxCharacters;
    private String messageHeader;
    private Button bAction;

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

        View view = inflater.inflate(R.layout.message_fragment, container, false);

        charactersLeftView = (TextView) view.findViewById(R.id.characters_left_view);
        messageHeaderView = (TextView) view.findViewById(R.id.message_fragment_header);
        messageEditText = (EditText) view.findViewById(R.id.message_edit_text);
        messageEditText.requestFocus();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setActionButtonStateListener(Button bAction){
        this.bAction = bAction;

        String prefix = getString(R.string.characters_left);
        messageLimitWatcher = new MessageLimitWatcher(charactersLeftView, prefix, maxCharacters, bAction);
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
