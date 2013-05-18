package com.amnesty.panicbutton.sms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.SoftKeyboard;
import com.amnesty.panicbutton.wizard.ActionButtonStateListener;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class SMSSettingsActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    @InjectView(R.id.sms_save_button)
    private Button saveButton;
    private SMSSettingsFragment smsSettingsFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_settings_layout);

        this.smsSettingsFragment = getSMSSettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.sms_settings_layout_root, smsSettingsFragment).commit();
    }

    SMSSettingsFragment getSMSSettingsFragment() {
        return SMSSettingsFragment.create(R.string.sms_settings_header);
    }

    private void save() {
        smsSettingsFragment.performAction();
        SoftKeyboard.hide(this, this.findViewById(android.R.id.content));
        Toast.makeText(this, R.string.sms_save_successful, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (smsSettingsFragment.hasSettingsChanged() && saveButton.isEnabled()) {
            displayDialog();
            return;
        }
        super.onBackPressed();
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
            SMSSettingsActivity.this.finish();
        }
    };

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            SMSSettingsActivity.this.finish();
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