package com.amnesty.panicbutton.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.model.SMSSettings;
import com.amnesty.panicbutton.wizard.ActionButtonStateListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.amnesty.panicbutton.R.id.*;

public class ContactEditTexts {
    public static final int PHONE_NUMBER_LIMIT = 4;
    private List<EditText> contacts = new ArrayList<EditText>();
    private Context context;

    public ContactEditTexts(FragmentManager fragmentManager, final ActionButtonStateListener actionButtonStateListener,
                            Context context) {
        this.context = context;

        TextWatcher phoneNumberTextWatcher = phoneNumberWatcher(actionButtonStateListener);
        List<Integer> ids = Arrays.asList(first_contact, second_contact, third_contact);
        for (Integer id : ids) {
            EditText editText = findEditText(id, fragmentManager);
            editText.addTextChangedListener(phoneNumberTextWatcher);
            contacts.add(editText);
        }
    }

    private EditText findEditText(int fragmentId, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentById(fragmentId);
        return (EditText) fragment.getView().findViewById(R.id.contact_edit_text);
    }

    public void displaySettings() {
        SMSSettings currentSettings = SMSSettings.retrieve(context);
        if (currentSettings.isConfigured()) {
            for (int i = 0, contactsSize = contacts.size(); i < contactsSize; i++) {
                EditText contact = contacts.get(i);
                contact.setText(currentSettings.maskedPhoneNumberAt(i));
            }
        }
    }

    public boolean hasAtleastOneValidPhoneNumber() {
        for (EditText contact : contacts) {
            if (contact.getText().length() > PHONE_NUMBER_LIMIT) return true;
        }
        return false;
    }

    public List<String> getPhoneNumbers() {
        SMSSettings currentSMSSettings = SMSSettings.retrieve(context);
        List<String> phoneNumbers = new ArrayList<String>();
        for (int i = 0, contactsSize = contacts.size(); i < contactsSize; i++) {
            phoneNumbers.add(getPhoneNumber(currentSMSSettings, i, contacts.get(i).getText().toString()));
        }
        return phoneNumbers;
    }

    private String getPhoneNumber(SMSSettings currentSMSSettings, int index, String contactNumberInView) {
        if (currentSMSSettings.maskedPhoneNumberAt(index).equals(contactNumberInView)) {
            return currentSMSSettings.phoneNumberAt(index);
        }
        return contactNumberInView;
    }


    private TextWatcher phoneNumberWatcher(final ActionButtonStateListener actionButtonStateListener) {
        return new TextWatcher() {
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
    }
}