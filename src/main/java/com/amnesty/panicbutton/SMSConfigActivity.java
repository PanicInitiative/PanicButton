package com.amnesty.panicbutton;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import roboguice.activity.RoboActivity;

public class SMSConfigActivity extends RoboActivity {
    private static String TAG = SMSConfigActivity.class.getSimpleName();
    private static final int PICK_CONTACT_REQUEST = 100;
    private String currentContactTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.sms_config);
    }

    public void launchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//        currentContactTag = (String)view.getTag();
        startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if((requestCode == PICK_CONTACT_REQUEST) && (resultCode == RESULT_OK)){
//            String phoneNumber = getPhoneNumber(data.getData());
//            EditText contact = (EditText) findViewWithTag(currentContactTag);
//            contact.setText(phoneNumber);
//        }
    }

    private String getPhoneNumber(Uri contactData){
        String[] projection = {
            ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor cursor = managedQuery(contactData, projection,null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "";
    }

}