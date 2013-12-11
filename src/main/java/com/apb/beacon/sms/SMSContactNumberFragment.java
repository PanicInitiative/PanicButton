package com.apb.beacon.sms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.common.ContactEditTexts;
import com.apb.beacon.model.SMSSettings;
import com.apb.beacon.wizard.NestedWizardFragment;
import com.apb.beacon.wizard.WizardAction;
import com.apb.beacon.wizard.WizardActivity;

import java.util.List;

import static com.apb.beacon.R.id.first_contact;
import static com.apb.beacon.R.id.second_contact;
import static com.apb.beacon.R.id.sms_message;
import static com.apb.beacon.R.id.third_contact;

/**
 * Created by aoe on 12/11/13.
 */
public class SMSContactNumberFragment extends NestedWizardFragment {
//    public static final String HEADER_TEXT_ID = "HEADER_TEXT_ID";
    private ContactEditTexts contactEditTexts;
//    private EditText smsEditText;

//    public static SMSSettingsFragment create(int headerTextId) {
//        SMSSettingsFragment smsSettingsFragment = new SMSSettingsFragment();
//        Bundle bundle = new Bundle();
////        bundle.putInt(HEADER_TEXT_ID, headerTextId);
//        smsSettingsFragment.setArguments(bundle);
//
//        return smsSettingsFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.wizard_training_contacts, container, false);
        initializeViews(inflate);
        SMSSettings currentSettings = SMSSettings.retrieve(context);
        if(currentSettings.isConfigured()) {
            displaySettings(currentSettings);
        }
        return inflate;
    }

    private void initializeViews(View inflate) {
        contactEditTexts = new ContactEditTexts(getFragmentManager(), actionButtonStateListener, getActivity());
//        Fragment fragment = getFragmentManager().findFragmentById(R.id.sms_message);
//        smsEditText = (EditText) fragment.getView().findViewById(R.id.message_edit_text);

//        TextView headerTextView = (TextView) inflate.findViewById(R.id.sms_settings_header);
//        headerTextView.setText(getString(getArguments().getInt(HEADER_TEXT_ID)));
    }

    private void displaySettings(SMSSettings settings) {
//        smsEditText.setText(settings.message());
        contactEditTexts.maskPhoneNumbers();
    }

    @Override
    protected int[] getFragmentIds() {
        return new int[]{first_contact, second_contact, third_contact};
    }

    @Override
    public String action() {
        return getString(WizardAction.NEXT.actionId());
    }

    @Override
    public boolean performAction() {
        SMSSettings newSMSSettings = getSMSSettingsFromView();

        SMSSettings.save(context, newSMSSettings);
        displaySettings(newSMSSettings);
        return true;
    }

    private SMSSettings getSMSSettingsFromView() {
//        String message = smsEditText.getText().toString();
        List<String> phoneNumbers = contactEditTexts.getPhoneNumbers();
        return new SMSSettings(phoneNumbers);
    }

    @Override
    public void onFragmentSelected() {
        if (actionButtonStateListener != null) {
            actionButtonStateListener.enableActionButton(contactEditTexts.hasAtleastOneValidPhoneNumber());
        }
    }

    public boolean hasSettingsChanged() {
        SMSSettings existingSettings = SMSSettings.retrieve(getActivity());
        return !existingSettings.equals(getSMSSettingsFromView());
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        ((WizardActivity)getActivity()).previousWithSkip();
//    }
}
