package com.apb.beacon.dialer;

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

public class DialerSettingsActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    @InjectView(R.id.dialer_save_button)
    private Button saveButton;
    private DialerSettingsFragment dialerSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialer_settings_layout);

        this.dialerSettingsFragment = getDialerSettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dialer_settings_layout_root, dialerSettingsFragment).commit();
    }

    DialerSettingsFragment getDialerSettingsFragment() {
        return DialerSettingsFragment.create(R.string.dialer_settings_header);
    }

    private void save() {
        dialerSettingsFragment.performAction();
        SoftKeyboard.hide(this, this.findViewById(android.R.id.content));
        Toast.makeText(this, R.string.dialer_save_successful, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (dialerSettingsFragment.hasSettingsChanged() && saveButton.isEnabled()) {
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
            DialerSettingsActivity.this.finish();
        }
    };

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            DialerSettingsActivity.this.finish();
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
