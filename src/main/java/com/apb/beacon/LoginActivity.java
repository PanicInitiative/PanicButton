package com.apb.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.apb.beacon.common.AppUtil;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.login_screen_layout)
public class LoginActivity extends PanicButtonActivity {
    @InjectView(R.id.pin_edit_text)
    private EditText pinEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void goBack(View view) {
        this.finish();
    }

    public void onEnterPressed(View view) {
        String password = pinEditText.getText().toString();
        if (ApplicationSettings.passwordMatches(getApplicationContext(), password)) {
            startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
            return;
        }
        AppUtil.setError(this, pinEditText, R.string.incorrect_pin);
    }
}