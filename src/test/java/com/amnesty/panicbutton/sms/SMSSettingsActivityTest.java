package com.amnesty.panicbutton.sms;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.model.SMSSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsActivityTest {
    private SMSSettingsActivity smsSettingsActivity;

    private Button saveButton;
    private EditText smsEditText;
    private EditText firstContactEditText;
    private EditText secondContactEditText;
    private EditText thirdContactEditText;

    private String alreadySavedMessage;
    private List<String> alreadySavedPhoneNumbers;

    @Before
    public void setup() {
        setupExistingSettings();

        smsSettingsActivity = new SMSSettingsActivity();
        smsSettingsActivity.onCreate(null);

        saveButton = (Button) smsSettingsActivity.findViewById(R.id.save_button);

        Fragment smsFragment = smsSettingsActivity.getSupportFragmentManager().findFragmentById(R.id.sms_message);
        Fragment firstContact = smsSettingsActivity.getSupportFragmentManager().findFragmentById(R.id.first_contact);
        Fragment secondContact = smsSettingsActivity.getSupportFragmentManager().findFragmentById(R.id.second_contact);
        Fragment thirdContact = smsSettingsActivity.getSupportFragmentManager().findFragmentById(R.id.third_contact);

        smsEditText = (EditText) smsFragment.getView().findViewById(R.id.message_edit_text);
        firstContactEditText = (EditText) firstContact.getView().findViewById(R.id.contact_edit_text);
        secondContactEditText = (EditText) secondContact.getView().findViewById(R.id.contact_edit_text);
        thirdContactEditText = (EditText) thirdContact.getView().findViewById(R.id.contact_edit_text);
    }

    private void setupExistingSettings() {
        alreadySavedMessage = "Already saved message";
        alreadySavedPhoneNumbers = Arrays.asList("123245697", "345665-5656", "45234234345");
        SMSSettings smsSettings = new SMSSettings(alreadySavedPhoneNumbers, alreadySavedMessage);
        SMSSettings.save(Robolectric.application, smsSettings);
    }

    @Test
    public void shouldLoadTheSMSSettingsLayoutOnCreateWithExistingSettings() {
        assertEquals(R.id.sms_settings_layout_root, shadowOf(smsSettingsActivity).getContentView().getId());
        assertEquals(alreadySavedMessage, smsEditText.getText().toString());
        assertEquals(alreadySavedPhoneNumbers.get(0), firstContactEditText.getText().toString());
        assertEquals(alreadySavedPhoneNumbers.get(1), secondContactEditText.getText().toString());
        assertEquals(alreadySavedPhoneNumbers.get(2), thirdContactEditText.getText().toString());
    }

    @Test
    public void shouldSaveSMSSettingsOnSaveClick() throws Exception {
        String expectedMessage = "Help! I am in trouble";
        String number1 = "123-456-789";
        String number2 = "9874641321";
        String number3 = "4564523423";

        smsEditText.setText(expectedMessage);
        firstContactEditText.setText(number1);
        secondContactEditText.setText(number2);
        thirdContactEditText.setText(number3);

        saveButton.performClick();

        SMSSettings retrievedSMSSettings = SMSSettings.retrieve(Robolectric.application);

        assertEquals(expectedMessage, retrievedSMSSettings.getMessage());
        assertEquals(number1, retrievedSMSSettings.getPhoneNumber(0));
        assertEquals(number2, retrievedSMSSettings.getPhoneNumber(1));
        assertEquals(number3, retrievedSMSSettings.getPhoneNumber(2));
    }
}
