package org.iilab.pb.common;

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
import android.widget.RelativeLayout;

import org.iilab.pb.R;

import static android.app.Activity.RESULT_OK;
import static android.view.View.OnClickListener;

public class ContactPickerFragment extends Fragment {
    private static final int PICK_CONTACT_REQUEST_ID = 100;

    private ImageButton contactPickerButton;
    private EditText phoneNumberEditText;
    private static  int FRAGMENT_ID ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_picker_fragment, container, false);

        contactPickerButton = (ImageButton) view.findViewById(R.id.contact_picker_button);
        phoneNumberEditText = (EditText) view.findViewById(R.id.contact_edit_text);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contactPickerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	 int wizardState = ApplicationSettings.getWizardState(getActivity());
//            	 if(wizardState != AppConstants.WIZARD_FLAG_HOME_READY){
            		 AppConstants.IS_BACK_BUTTON_PRESSED = true;
//            	 }
                launchContactPicker(v);
            }
        });
    }

    public void launchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        FRAGMENT_ID=((RelativeLayout)view.getParent()).getId();
        getParentFragment().startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == PICK_CONTACT_REQUEST_ID) && (resultCode == RESULT_OK)&& getId()==FRAGMENT_ID) {
            phoneNumberEditText.setText(getPhoneNumber(data.getData()));
        }
    }

    private String getPhoneNumber(Uri contactData) {
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        String phoneNumber = "";
        Cursor cursor = getCursor(contactData, projection);
        if (cursor.moveToNext()) {
            phoneNumber = cursor.getString(0);
        }
        return phoneNumber;
    }

    Cursor getCursor(Uri contactData, String[] projection) {
        return getActivity().getContentResolver().query(contactData, projection, null, null, null);
    }
}