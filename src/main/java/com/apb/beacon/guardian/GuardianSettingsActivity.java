package com.apb.beacon.guardian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apb.beacon.R;
import com.apb.beacon.SettingsActivity;
import com.apb.beacon.SoftKeyboard;
import com.apb.beacon.wizard.ActionButtonStateListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class GuardianSettingsActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    @InjectView(R.id.guardian_save_button)
    private Button saveButton;
    private GuardianSettingsFragment guardianSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardian_settings_layout);

        this.guardianSettingsFragment = getGuardianSettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.guardian_settings_layout_root, guardianSettingsFragment).commit();
    }

    GuardianSettingsFragment getGuardianSettingsFragment() {
        return GuardianSettingsFragment.create(R.string.guardian_settings_header);
    }

    private void save() {
        guardianSettingsFragment.performAction();
        SoftKeyboard.hide(this, this.findViewById(android.R.id.content));
        Toast.makeText(this, R.string.guardian_save_successful, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (guardianSettingsFragment.hasSettingsChanged() && saveButton.isEnabled()) {
            displayDialog();
            return;
        }
        super.onBackPressed();
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void displayDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.settings_changed_alert_title)
                .setMessage(R.string.settings_changed_alert_message)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, saveListener)
                .setNegativeButton(R.string.no, cancelListener);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private DialogInterface.OnClickListener saveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            save();
            GuardianSettingsActivity.this.finish();
        }
    };

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            GuardianSettingsActivity.this.finish();
        }
    };

    public void onPreviousPressed(View view) {
        onBackPressed();
    }

    public void onSave(View view) {
        save();
    }

    @Override
    public void enableActionButton(boolean isEnabled) {
        saveButton.setEnabled(isEnabled);
    }
}
