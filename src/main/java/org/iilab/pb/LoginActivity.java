package org.iilab.pb;

import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends PanicButtonActivity {

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
                	String password = passwordEditText.getText().toString();
                    if (ApplicationSettings.passwordMatches(getApplicationContext(), password)) {

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                        i = AppUtil.clearBackStack(i);
                        if (ApplicationSettings.isAlertActive(LoginActivity.this)) {
                            i.putExtra("page_id", "home-alerting");
                        } else {
                            i.putExtra("page_id", "home-ready");
                        }
                        startActivity(i);

                        finish();
                        return true;
                    }
                    AppUtil.setError(LoginActivity.this, passwordEditText, ((tryCount < 2) ? R.string.incorrect_pin : R.string.incorrect_pin_3_times));
                    tryCount++;
                }
                return false;
            }
        });
        bAction = (Button) findViewById(R.id.b_action);
        bAction.setText(getResources().getString(R.string.code_ok));
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                if (ApplicationSettings.passwordMatches(getApplicationContext(), password)) {

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    i = AppUtil.clearBackStack(i);
                    if (ApplicationSettings.isAlertActive(LoginActivity.this)) {
                        i.putExtra("page_id", "home-alerting");
                    } else {
                        i.putExtra("page_id", "home-ready");
                    }
                    startActivity(i);

                    finish();
                    return;
                }
                AppUtil.setError(LoginActivity.this, passwordEditText, ((tryCount < 2) ? R.string.incorrect_pin : R.string.incorrect_pin_3_times));
                tryCount++;
            }
        });
        
        
    }
    
}