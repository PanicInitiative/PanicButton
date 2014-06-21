package org.iilab.pb.common;

import org.iilab.pb.R;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;
import static android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
import static android.view.View.OnClickListener;

public class ContactPickerFragment extends Fragment {
    private static final int PICK_CONTACT_REQUEST_ID = 100;

    private ImageButton contactPickerButton;
    private EditText phoneNumberEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_picker_fragment, container, false);

        contactPickerButton = (ImageButton) view.findViewById(R.id.contact_picker_button);
        phoneNumberEditText = (EditText) view.findViewById(R.id.contact_edit_text);

        return view;
    }

//    private OnClickListener contactPickerListener = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            launchContactPicker(view);
//        }
//    };

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        contactPickerButton.setOnClickListener(contactPickerListener);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contactPickerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	 int wizardState = ApplicationSettings.getWizardState(getActivity());
//            	 if(wizardState != AppConstants.WIZARD_FLAG_HOME_READY){
            		 AppConstants.IS_BACK_BUTTON_PRESSED = true;
            		 AppConstants.IS_BACK_BUTTON_PRESSED = true;
//            	 }
                launchContactPicker(v);
            }
        });
    }

    public void launchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(ACTION_GET_CONTENT);
        contactPickerIntent.setType(CONTENT_ITEM_TYPE);
        startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == PICK_CONTACT_REQUEST_ID) && (resultCode == RESULT_OK)) {
            phoneNumberEditText.setText(getPhoneNumber(data.getData()));
        }
    }

    private String getPhoneNumber(Uri contactData) {
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getCursor(contactData, projection);
        cursor.moveToNext();
        return cursor.getString(0);
    }

    Cursor getCursor(Uri contactData, String[] projection) {
        return getActivity().managedQuery(contactData, projection, null, null, null);
    }
}