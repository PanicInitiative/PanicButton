package com.apb.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apb.beacon.common.AppUtil;
import com.apb.beacon.wizard.WizardActivity;

import roboguice.inject.ContentView;

@ContentView(R.layout.login_screen_layout)
public class LoginActivity extends PanicButtonActivity {

    private EditText passwordEditText;
    private Button bGo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passwordEditText = (EditText) findViewById(R.id.create_pin_edittext);

        bGo = (Button) findViewById(R.id.b_action);
        bGo.setText("Go");
        bGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                if (ApplicationSettings.passwordMatches(getApplicationContext(), password)) {

                    Intent i = new Intent(LoginActivity.this, WizardActivity.class);
                    i.putExtra("page_id",  "home-ready");
                    startActivity(i);
//            startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
                    return;
                }
                AppUtil.setError(LoginActivity.this, passwordEditText, R.string.incorrect_pin);
            }
        });
    }

//    public void goBack(View view) {
//        this.finish();
//    }
//
//    public void onEnterPressed(View view) {
//        String password = passwordEditText.getText().toString();
//        if (ApplicationSettings.passwordMatches(getApplicationContext(), password)) {
//
//            Intent i = new Intent(LoginActivity.this, WizardActivity.class);
//            i.putExtra("page_id",  "home-ready");
//            startActivity(i);
////            startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
//            return;
//        }
//        AppUtil.setError(this, passwordEditText, R.string.incorrect_pin);
//    }
}