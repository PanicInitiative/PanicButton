package com.apb.beacon.dialer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.model.DialerSettings;
import com.apb.beacon.wizard.NestedWizardFragment;
import com.apb.beacon.wizard.WizardAction;

import static com.apb.beacon.R.id.*;


public class DialerSettingsFragment extends NestedWizardFragment {
    public static final String HEADER_TEXT_ID = "HEADER_TEXT_ID";
    private EditText dialerEditText;

    public static DialerSettingsFragment create(int headerTextId) {
        DialerSettingsFragment dialerSettingsFragment = new DialerSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HEADER_TEXT_ID, headerTextId);
        dialerSettingsFragment.setArguments(bundle);

        return dialerSettingsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.dialer_settings_fragment, container, false);
        initializeViews(inflate);
        DialerSettings currentSettings = DialerSettings.retrieve(context);
        //if(currentSettings.isConfigured()) {
        displaySettings(currentSettings);
        //}
        return inflate;
    }

    private void initializeViews(View inflate) {
        dialerEditText = (EditText) inflate.findViewById(R.id.first_code);

        TextView headerTextView = (TextView) inflate.findViewById(R.id.dialer_settings_header);
        headerTextView.setText(getString(getArguments().getInt(HEADER_TEXT_ID)));
    }

    private void displaySettings(DialerSettings settings) {
        dialerEditText.setText(settings.LaunchCode());
    }

    @Override
    protected int[] getFragmentIds() {
        return new int[]{first_code};
    }

    @Override
    public String action() {
        return getString(WizardAction.SAVE.actionId());
    }

    @Override
    public boolean performAction() {
        DialerSettings newDialerSettings = getDialerSettingsFromView();

        DialerSettings.save(context, newDialerSettings);
        displaySettings(newDialerSettings);
        return true;
    }

    private DialerSettings getDialerSettingsFromView() {
        String code = dialerEditText.getText().toString();
        return new DialerSettings(code);
    }

    @Override
    public void onFragmentSelected() {

    }

    public boolean hasSettingsChanged() {
        DialerSettings existingSettings = DialerSettings.retrieve(getActivity());
        return !existingSettings.equals(getDialerSettingsFromView());
    }
}
