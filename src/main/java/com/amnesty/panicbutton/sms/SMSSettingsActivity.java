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
        applyCurrentSettings(currentSettings);
    }

    private void initializeViews() {
        firstContact = (EditText) findViewInFragmentById(R.id.first_contact, R.id.contact_edit_text);
        secondContact = (EditText) findViewInFragmentById(R.id.second_contact, R.id.contact_edit_text);
        thirdContact = (EditText) findViewInFragmentById(R.id.third_contact, R.id.contact_edit_text);
        smsEditText = (EditText) findViewInFragmentById(R.id.sms_message, R.id.message_edit_text);
    }

    private void applyCurrentSettings(SMSSettings currentSettings) {
        smsEditText.setText(currentSettings.getMessage());
        firstContact.setText(currentSettings.getPhoneNumber(0));
        secondContact.setText(currentSettings.getPhoneNumber(1));
        thirdContact.setText(currentSettings.getPhoneNumber(2));
    }

    public void save(View view) {
        String message = smsEditText.getText().toString();
        List<String> phoneNumbers = new ArrayList<String>();
        phoneNumbers.add(firstContact.getText().toString());
        phoneNumbers.add(secondContact.getText().toString());
        phoneNumbers.add(thirdContact.getText().toString());

        SMSSettings smsSettings = new SMSSettings(phoneNumbers, message);
        SMSSettings.save(getApplicationContext(), smsSettings);
        Toast.makeText(getApplicationContext(), R.string.successfully_saved, Toast.LENGTH_LONG).show();
    }

    private View findViewInFragmentById(int fragmentId, int viewId) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(fragmentId);
        return fragment.getView().findViewById(viewId);
    }
}