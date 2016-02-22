package org.iilab.pb;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

import org.iilab.pb.trigger.HardwareTriggerService;

import static org.iilab.pb.common.AppConstants.PAGE_HOME_ALERTING;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_READY;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_TRAINING_1_5;
import static org.iilab.pb.common.AppUtil.playTrainingForRelease1_5;
import static org.iilab.pb.common.AppUtil.setError;
import static org.iilab.pb.common.ApplicationSettings.isAlertActive;
import static org.iilab.pb.common.ApplicationSettings.passwordMatches;
import static org.iilab.pb.common.ApplicationSettings.setFirstRun;
import static org.iilab.pb.common.ApplicationSettings.setTrainingDoneRelease1_5;

public class LoginActivity extends PanicButtonActivity {
    private static final String TAG = LoginActivity.class.getName();
    private EditText passwordEditText;
    private Button bAction;
    private int tryCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        passwordEditText = (EditText) findViewById(R.id.create_pin_edittext);
        passwordEditText.requestFocus();
        passwordEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                    actionOnKeyPress();
                }
                return false;
            }
        });
        bAction = (Button) findViewById(R.id.b_action);
        bAction.setText(getResources().getString(R.string.code_ok));
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionOnKeyPress();
            }
        });

    }
    private void actionOnKeyPress(){
        String password = passwordEditText.getText().toString();
        if (passwordMatches(getApplicationContext(), password)) {
            Intent i;

            // check if db update version is 1.5 and play training
            if(playTrainingForRelease1_5(getApplicationContext())) {
                i = new Intent(LoginActivity.this, WizardActivity.class);
                i.putExtra(PAGE_ID, PAGE_SETUP_TRAINING_1_5);
                setTrainingDoneRelease1_5(getApplicationContext(), true);
                setFirstRun(getApplicationContext(),false);
                //in training mode of alarm trigger, stop the send alert hardware service.
                getApplicationContext().stopService(new Intent(getApplicationContext(), HardwareTriggerService.class));

            }else {
                i = new Intent(LoginActivity.this, MainActivity.class);
                if (isAlertActive(LoginActivity.this)) {
                    i.putExtra(PAGE_ID, PAGE_HOME_ALERTING);
                } else {
                    //check permissions
                    i.putExtra(PAGE_ID, PAGE_HOME_READY);
                }
            }
            startActivity(i);

            finish();
            return;
        }
        setError(LoginActivity.this, passwordEditText, ((tryCount < 2) ? R.string.incorrect_pin : R.string.incorrect_pin_3_times));
        tryCount++;
    }
    
}