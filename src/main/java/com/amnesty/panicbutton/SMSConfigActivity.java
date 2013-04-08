package com.amnesty.panicbutton;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SMSConfigActivity extends RoboActivity {
    private static String TAG = SMSConfigActivity.class.getSimpleName();
    private static final int PICK_CONTACT_REQUEST_ID = 100;
    private static final int MAX_CHARACTER_COUNT = 100;

    private String currentContactTag;
    @InjectView(R.id.sms_message)
    private EditText smsMessageEditText;
    @InjectView(R.id.characters_left)
    private TextView charactersLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_config);

        initSmsEditText();
    }

    private void initSmsEditText() {
        smsMessageEditText.addTextChangedListener(smsMessageWatcher);
        smsMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARACTER_COUNT)});
        charactersLeft.setText(String.valueOf(MAX_CHARACTER_COUNT - smsMessageEditText.getText().length()));
    }

    public void launchContactPicker(View view) {
        currentContactTag = (String) view.getTag();
        Intent contactPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST_ID);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == PICK_CONTACT_REQUEST_ID) && (resultCode == RESULT_OK)) {
            String phoneNumber = getPhoneNumber(data.getData());
            EditText contact = (EditText) findViewById(android.R.id.content).findViewWithTag(currentContactTag);
            contact.setText(phoneNumber);
        }
    }

    private final TextWatcher smsMessageWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            charactersLeft.setText(String.valueOf(MAX_CHARACTER_COUNT - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private String getPhoneNumber(Uri contactData) {
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = managedQuery(contactData, projection, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

}