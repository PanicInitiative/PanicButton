package org.iilab.pb.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import org.iilab.pb.R;
import org.iilab.pb.common.MessageLimitWatcher;
import org.iilab.pb.common.MyTextView;
import org.iilab.pb.model.SMSSettings;

import static java.lang.String.valueOf;

public class MessageTextFragment extends Fragment {

    private MyTextView charactersLeftView;
    private MyTextView messageHeaderView;
    private EditText messageEditText;

    private MessageLimitWatcher messageLimitWatcher;
    private Button bAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.message_fragment, container, false);

        charactersLeftView = (MyTextView) view.findViewById(R.id.characters_left_view);
        messageHeaderView = (MyTextView) view.findViewById(R.id.message_fragment_header);
        messageEditText = (EditText) view.findViewById(R.id.message_edit_text);
        messageEditText.requestFocus();
        View parentView = getParentFragment().getView();
        if (parentView != null) {
            bAction = (Button) parentView.findViewById(R.id.fragment_action);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String currentEmergencyMsg = SMSSettings.retrieveMessage(getActivity());
        if (currentEmergencyMsg != null) {
            displaySettings(currentEmergencyMsg);
        }
        setActionButtonStateListener(bAction);
        bAction.setEnabled(!(messageEditText.getText().toString().trim().equals("")));
        messageEditText.setImeOptions(EditorInfo.IME_FLAG_NAVIGATE_NEXT);
        messageEditText.selectAll();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setActionButtonStateListener(Button bAction) {
        this.bAction = bAction;
        int maxCharacters = charactersLeftView.getMaxCharacters();
        String prefix = getString(R.string.characters_left);
        messageLimitWatcher = new MessageLimitWatcher(charactersLeftView, prefix, maxCharacters, bAction);
        messageEditText.addTextChangedListener(messageLimitWatcher);
        InputFilter[] filters = {new InputFilter.LengthFilter(maxCharacters)};
        messageEditText.setFilters(filters);
        messageEditText.setSelection(messageEditText.getText().length());

        charactersLeftView.setText(prefix + valueOf(maxCharacters - messageEditText.getText().length()));
        messageHeaderView.setText(messageHeaderView.getMessageHeader());
    }

    public String getMessage() {
        return messageEditText.getText().toString();
    }

    public void setMessage(String message) {
        if (message != null) {
            messageEditText.setText(message);
        }
    }

    public void displaySettings(String msg) {
        messageEditText.setText(msg);
    }


    public String getSMSSettingsFromView() {
        String message = messageEditText.getText().toString().trim();
        return message;
    }
}
