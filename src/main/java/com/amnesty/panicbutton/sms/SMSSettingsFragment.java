package com.amnesty.panicbutton.sms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.wizard.NestedWizardFragment;
import com.amnesty.panicbutton.wizard.WizardAction;

import java.util.ArrayList;
import java.util.List;

import static com.amnesty.panicbutton.R.id.*;

public class SMSSettingsFragment extends NestedWizardFragment {
    public static final int PHONE_NUMBER_LIMIT = 4;
    public static final String HEADER_TEXT_ID = "HEADER_TEXT_ID";
    private EditText firstContact;
    private EditText secondContact;
    private EditText thirdContact;
    private EditText smsEditText;

    public static SMSSettingsFragment create(int headerTextId) {
        SMSSettingsFragment smsSettingsFragment = new SMSSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HEADER_TEXT_ID, headerTextId);
        smsSettingsFragment.setArguments(bundle);

        return smsSettingsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.sms_settings_fragment, container, false);
        SMSSettings currentSettings = SMSSettings.retrieve(context);
        initializeViews(inflate);
        displaySettings(currentSettings);
        return inflate;
    }

    private void displaySettings(SMSSettings settings) {
        smsEditText.setText(settings.message());
        firstContact.setText(settings.maskedPhoneNumberAt(0));
        secondContact.setText(settings.maskedPhoneNumberAt(1));
        thirdContact.setText(settings.maskedPhoneNumberAt(2));
    }

    private void initializeViews(View inflate) {
        firstContact = findEditText(R.id.first_contact, R.id.contact_edit_text);
        firstContact.addTextChangedListener(phoneNumberTextWatcher);
        secondContact = findEditText(R.id.second_contact, R.id.contact_edit_text);
        secondContact.addTextChangedListener(phoneNumberTextWatcher);
        thirdContact = findEditText(R.id.third_contact, R.id.contact_edit_text);
        thirdContact.addTextChangedListener(phoneNumberTextWatcher);
        smsEditText = findEditText(R.id.sms_message, R.id.message_edit_text);

        TextView headerTextView = (TextView) inflate.findViewById(R.id.sms_settings_header);
        headerTextView.setText(getString(getArguments().getInt(HEADER_TEXT_ID)));
    }

    private EditText findEditText(int fragmentId, int viewId) {
        Fragment fragment = getFragmentManager().findFragmentById(fragmentId);
        return (EditText) fragment.getView().findViewById(viewId);
    }

    @Override
    protected int[] getFragmentIds() {
        return new int[]{first_contact, second_contact, third_contact, sms_message};
    }

    @Override
    public String action() {
        return getString(WizardAction.SAVE.actionId());
    }

    @Override
    public void performAction() {
        String message = smsEditText.getText().toString();
        SMSSettings currentSMSSettings = SMSSettings.retrieve(context);

        List<String> phoneNumbers = getPhoneNumbersFromView(currentSMSSettings);
        SMSSettings newSMSSettings = new SMSSettings(phoneNumbers, message);

        SMSSettings.save(context, newSMSSettings);
        displaySettings(newSMSSettings);
    }

    private List<String> getPhoneNumbersFromView(SMSSettings currentSMSSettings) {
        currentSMSSettings.maskedPhoneNumberAt(0);

        List<String> phoneNumbers = new ArrayList<String>();
        phoneNumbers.add(getPhoneNumber(currentSMSSettings, 0, firstContact.getText().toString()));
        phoneNumbers.add(getPhoneNumber(currentSMSSettings, 1, secondContact.getText().toString()));
        phoneNumbers.add(getPhoneNumber(currentSMSSettings, 2, thirdContact.getText().toString()));

        return phoneNumbers;
    }

    private String getPhoneNumber(SMSSettings currentSMSSettings, int index, String contactNumberInView) {
        if (currentSMSSettings.maskedPhoneNumberAt(index).equals(contactNumberInView)) {
            return currentSMSSettings.phoneNumberAt(index);
        }
        return contactNumberInView;
    }

    private TextWatcher phoneNumberTextWatcher = new TextWatcher() {
        private String currentText;

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            currentText = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String newText = editable.toString();
            if (actionButtonStateListener != null && !newText.equals(currentText)) {
                currentText = newText;
                actionButtonStateListener.onActionStateChanged(hasAtleastOneValidPhoneNumber());
            }
        }
    };

    private boolean hasAtleastOneValidPhoneNumber() {
        return firstContact.getText().length() > PHONE_NUMBER_LIMIT ||
                secondContact.getText().length() > PHONE_NUMBER_LIMIT ||
                thirdContact.getText().length() > PHONE_NUMBER_LIMIT;
    }

    @Override
    public void onFragmentSelected() {
        if (actionButtonStateListener != null) {
            actionButtonStateListener.onActionStateChanged(hasAtleastOneValidPhoneNumber());
        }
    }
}
