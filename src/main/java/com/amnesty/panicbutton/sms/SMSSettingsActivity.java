package com.amnesty.panicbutton.sms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.model.SMSSettings;
import roboguice.activity.RoboFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class SMSSettingsActivity extends RoboFragmentActivity {

    private EditText firstContact;
    private EditText secondContact;
    private EditText thirdContact;
    private EditText smsEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_settings_layout);

        SMSSettings currentSettings = SMSSettings.retrieve(getApplicationContext());
        initializeViews();
        displaySettings(currentSettings);
    }

    private void initializeViews() {
        firstContact = (EditText) findViewInFragmentById(R.id.first_contact, R.id.contact_edit_text);
        secondContact = (EditText) findViewInFragmentById(R.id.second_contact, R.id.contact_edit_text);
        thirdContact = (EditText) findViewInFragmentById(R.id.third_contact, R.id.contact_edit_text);
        smsEditText = (EditText) findViewInFragmentById(R.id.sms_message, R.id.message_edit_text);
    }

    public void save(View view) {
        String message = smsEditText.getText().toString();
        SMSSettings currentSMSSettings = SMSSettings.retrieve(getApplication());

        List<String> phoneNumbers = getPhoneNumbersFromView(currentSMSSettings);
        SMSSettings newSMSSettings = new SMSSettings(phoneNumbers, message);

        SMSSettings.save(getApplicationContext(), newSMSSettings);
        Toast.makeText(getApplicationContext(), R.string.successfully_saved, Toast.LENGTH_LONG).show();
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

    private void displaySettings(SMSSettings settings) {
        smsEditText.setText(settings.message());
        firstContact.setText(settings.maskedPhoneNumberAt(0));
        secondContact.setText(settings.maskedPhoneNumberAt(1));
        thirdContact.setText(settings.maskedPhoneNumberAt(2));
    }

    private View findViewInFragmentById(int fragmentId, int viewId) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(fragmentId);
        return fragment.getView().findViewById(viewId);
    }
}