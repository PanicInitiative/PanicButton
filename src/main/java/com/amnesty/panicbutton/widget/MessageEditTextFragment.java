package com.amnesty.panicbutton.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.sms.MessageLimitWatcher;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import static com.amnesty.panicbutton.AppConstants.MAX_CHARACTER_COUNT;

public class MessageEditTextFragment extends RoboFragment {
    @InjectView(R.id.characters_left_view)
    private TextView charactersLeftView;

    @InjectView(R.id.message_edit_text)
    private EditText messageEditText;

    private MessageLimitWatcher messageLimitWatcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageLimitWatcher = new MessageLimitWatcher(charactersLeftView, MAX_CHARACTER_COUNT);
        messageEditText.addTextChangedListener(messageLimitWatcher);
        InputFilter[] filters = {new InputFilter.LengthFilter(MAX_CHARACTER_COUNT)};
        messageEditText.setFilters(filters);

        charactersLeftView.setText(String.valueOf(MAX_CHARACTER_COUNT - messageEditText.getText().length()));
    }
}
