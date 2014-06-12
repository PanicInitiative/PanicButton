package org.iilab.pb.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iilab.pb.R;
import org.iilab.pb.model.SMSSettings;


import static org.iilab.pb.R.id.first_contact;
import static org.iilab.pb.R.id.second_contact;
import static org.iilab.pb.R.id.third_contact;
import static org.iilab.pb.common.AppConstants.PHONE_NUMBER_LIMIT;

public class ContactEditTexts {
    private List<EditText> contacts = new ArrayList<EditText>();
    private Context context;

    public ContactEditTexts(FragmentManager fragmentManager, final Button bActionButton,
                            Context context) {
        this.context = context;

        TextWatcher phoneNumberTextWatcher = phoneNumberWatcher(bActionButton);
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

    public void maskPhoneNumbers() {
        SMSSettings currentSettings = SMSSettings.retrieve(context);
        if (currentSettings.isConfigured()) {
            for (int i = 0, contactsSize = contacts.size(); i < contactsSize; i++) {
                EditText contact = contacts.get(i);
                contact.setText(currentSettings.maskedPhoneNumberAt(i));
            }
        }
    }

    public boolean hasAtleastOneValidPhoneNumber() {
        String[] cNumbers = new String[contacts.size()];
        int count = 0;

        for (EditText contact : contacts) {
            String cNumber = contact.getText().toString();
            cNumber = cNumber.replaceAll("[- ]", "");
            cNumbers[count++] = cNumber;
        }

        for (int i = 0; i < count; i++) {
            if (cNumbers[i] == null || cNumbers[i].equals(""))
                continue;
            for (int j = i + 1; j < count; j++) {
                if (cNumbers[i].equals(cNumbers[j])) {
                    return false;
                }
            }
        }

        for (int i = 0; i < count; i++) {
            if (cNumbers[i].length() > PHONE_NUMBER_LIMIT) return true;
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


    private TextWatcher phoneNumberWatcher(final Button bActionButton) {
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
                Log.e("??????", "text changed");
                String newText = editable.toString();
                if (bActionButton != null && !newText.equals(currentText)) {
                    currentText = newText;
                    bActionButton.setEnabled(hasAtleastOneValidPhoneNumber());
                }
            }
        };
    }
}