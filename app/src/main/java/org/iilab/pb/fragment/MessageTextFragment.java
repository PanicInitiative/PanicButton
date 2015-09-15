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
import android.widget.RelativeLayout;

import org.iilab.pb.R;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.MessageLimitWatcher;
import org.iilab.pb.common.MyTextView;
import org.iilab.pb.model.SMSSettings;

import static java.lang.String.valueOf;

public class MessageTextFragment extends Fragment {

    private MyTextView charactersLeftView, stopAlertCharView;
    private MyTextView messageHeaderView,stopAlertMsgView;
    private EditText messageEditText,stopAlertEditText;
    private RelativeLayout rv_stopAlertMsgEditTextContainer,rv_stopAlertHeaderContainer;

    private MessageLimitWatcher messageLimitWatcher,stopAlertmessageLimitWatcher;
    private Button bAction;
    boolean isParentMainActivity;
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

        int parentActivity = getArguments().getInt(AppConstants.PARENT_ACTIVITY);
        if (parentActivity == AppConstants.FROM_MAIN_ACTIVITY) {
            isParentMainActivity=true;
            rv_stopAlertHeaderContainer =(RelativeLayout)view.findViewById(R.id.stop_message_header_container);
            rv_stopAlertMsgEditTextContainer=(RelativeLayout)view.findViewById(R.id.stop_message_container);
            rv_stopAlertHeaderContainer.setVisibility(View.VISIBLE);
            rv_stopAlertMsgEditTextContainer.setVisibility(View.VISIBLE);
            stopAlertCharView = (MyTextView) view.findViewById(R.id.stop_alert_characters_left_view);
            stopAlertMsgView = (MyTextView) view.findViewById(R.id.stop_alert_message_fragment_header);
            stopAlertEditText = (EditText) view.findViewById(R.id.stop_alert_message_edit_text);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String currentEmergencyMsg = SMSSettings.retrieveMessage(getActivity());
        if (currentEmergencyMsg != null) {
            displayEmergencyMsg(currentEmergencyMsg);
        }
        setActionButtonStateListener(bAction);
        bAction.setEnabled(!(messageEditText.getText().toString().trim().equals("")));
        messageEditText.setImeOptions(EditorInfo.IME_FLAG_NAVIGATE_NEXT);
        messageEditText.selectAll();
        if(isParentMainActivity) {
            String currentStopAlertMsg = SMSSettings.retrieveStopAlertMessage(getActivity());
            if (currentStopAlertMsg != null) {
                displayStopAlertMsg(currentStopAlertMsg);
            }
            bAction.setEnabled(!(stopAlertEditText.getText().toString().trim().equals("")));
            stopAlertEditText.setImeOptions(EditorInfo.IME_FLAG_NAVIGATE_NEXT);
            stopAlertEditText.selectAll();
        }

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

        if(isParentMainActivity) {
            int stopAlertMaxCharacters = stopAlertCharView.getMaxCharacters();
            stopAlertmessageLimitWatcher = new MessageLimitWatcher(stopAlertCharView, prefix, stopAlertMaxCharacters, bAction);
            stopAlertEditText.addTextChangedListener(stopAlertmessageLimitWatcher);
            InputFilter[] filterStopAlert = {new InputFilter.LengthFilter(maxCharacters)};
            stopAlertEditText.setFilters(filterStopAlert);
            stopAlertEditText.setSelection(stopAlertEditText.getText().length());
            stopAlertCharView.setText(prefix + valueOf(maxCharacters - stopAlertEditText.getText().length()));
            stopAlertMsgView.setText(stopAlertMsgView.getStopAlertMsgHeader());
        }
    }

    public void displayEmergencyMsg(String emergencyMessage) {
        messageEditText.setText(emergencyMessage);
    }

    public void displayStopAlertMsg(String stopAlertMessage) {
            stopAlertEditText.setText(stopAlertMessage);
    }
    public String getEmergencyMsgFromView() {
        String message = messageEditText.getText().toString().trim();
        return message;
    }

    public String getStopAlertMsgFromView() {
        String message = stopAlertEditText.getText().toString().trim();
        return message;
    }
}
