package com.apb.beacon.guardian;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.model.GuardianSettings;
import com.apb.beacon.wizard.NestedWizardFragment;
import com.apb.beacon.wizard.WizardAction;

import static com.apb.beacon.R.id.guardian_time;


public class GuardianSettingsFragment extends NestedWizardFragment {
    public static final String HEADER_TEXT_ID = "HEADER_TEXT_ID";
    private EditText guardianEditText;

    public static GuardianSettingsFragment create(int headerTextId) {
        GuardianSettingsFragment guardianSettingsFragment = new GuardianSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HEADER_TEXT_ID, headerTextId);
        guardianSettingsFragment.setArguments(bundle);

        return guardianSettingsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.guardian_settings_fragment, container, false);
        initializeViews(inflate);
        GuardianSettings currentSettings = GuardianSettings.retrieve(context);
        //if(currentSettings.isConfigured()) {
        displaySettings(currentSettings);
        //}
        return inflate;
    }

    private void initializeViews(View inflate) {
        guardianEditText = (EditText) inflate.findViewById(R.id.guardian_time);

        TextView headerTextView = (TextView) inflate.findViewById(R.id.guardian_settings_header);
        headerTextView.setText(getString(getArguments().getInt(HEADER_TEXT_ID)));
    }

    private void displaySettings(GuardianSettings settings) {
        guardianEditText.setText(settings.GuardianTime().toString());
    }

    @Override
    protected int[] getFragmentIds() {
        return new int[]{guardian_time};
    }

    @Override
    public String action() {
        return getString(WizardAction.SAVE.actionId());
    }

    @Override
    public boolean performAction() {
        GuardianSettings newGuardianSettings = getGuardianSettingsFromView();

        GuardianSettings.save(context, newGuardianSettings);
        displaySettings(newGuardianSettings);
        return true;
    }

    private GuardianSettings getGuardianSettingsFromView() {
        Long time = Long.parseLong(guardianEditText.getText().toString());
        return new GuardianSettings(time);
    }

    @Override
    public void onFragmentSelected() {

    }

    public boolean hasSettingsChanged() {
        GuardianSettings existingSettings = GuardianSettings.retrieve(getActivity());
        return !existingSettings.equals(getGuardianSettingsFromView());
    }
}
