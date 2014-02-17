package com.apb.beacon.sms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.apb.beacon.R;
import com.apb.beacon.common.MessageFragment;
import com.apb.beacon.model.SMSSettings;
import com.apb.beacon.wizard.NestedWizardFragment;
import com.apb.beacon.wizard.WizardAction;

import static com.apb.beacon.R.id.sms_message;

/**
 * Created by aoe on 12/12/13.
 */
public class SMSMessageFragment extends NestedWizardFragment {
    private EditText smsEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.wizard_training_message, container, false);
        initializeViews(inflate);
        String currentMsg = SMSSettings.retrieveMessage(context);
        if(currentMsg != null) {
            displaySettings(currentMsg);
        }
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getActivity(), "Enter your message.", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onResume(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

    private void initializeViews(View inflate) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.sms_message);
        ((MessageFragment)fragment).setActionButtonStateListener(actionButtonStateListener);
        smsEditText = (EditText) fragment.getView().findViewById(R.id.message_edit_text);

    }

    private void displaySettings(String msg) {
        smsEditText.setText(msg);
    }

    @Override
    protected int[] getFragmentIds() {
        return new int[]{sms_message};
    }

    @Override
    public String action() {
        return getString(WizardAction.NEXT.actionId());
    }

    @Override
    public boolean performAction() {
        String msg =  getSMSSettingsFromView();

        SMSSettings.saveMessage(context, msg);
        displaySettings(msg);
        return true;
    }

    private String getSMSSettingsFromView() {
        String message = smsEditText.getText().toString().trim();
        return message;
    }

    @Override
    public void onFragmentSelected() {
        if (actionButtonStateListener != null) {
            actionButtonStateListener.enableActionButton(!smsEditText.getText().toString().trim().equals(""));
        }
    }

}
