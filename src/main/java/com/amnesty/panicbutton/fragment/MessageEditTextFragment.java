package com.amnesty.panicbutton.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import static com.amnesty.panicbutton.AppConstants.MAX_CHARACTER_COUNT;
import static java.lang.String.valueOf;

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
        String prefix = getString(R.string.characters_left);

        messageLimitWatcher = new MessageLimitWatcher(charactersLeftView, prefix, MAX_CHARACTER_COUNT);
        messageEditText.addTextChangedListener(messageLimitWatcher);
        InputFilter[] filters = {new InputFilter.LengthFilter(MAX_CHARACTER_COUNT)};
        messageEditText.setFilters(filters);

        charactersLeftView.setText(prefix + valueOf(MAX_CHARACTER_COUNT - messageEditText.getText().length()));
    }
}
