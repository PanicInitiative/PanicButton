package com.amnesty.panicbutton.sms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboActivity;

public class SMSSettingsActivity extends RoboActivity {
    private static final int PICK_CONTACT_REQUEST_ID = 100;
    private String currentContactTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_settings_layout);
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

    private String getPhoneNumber(Uri contactData) {
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = managedQuery(contactData, projection, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}