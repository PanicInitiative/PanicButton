package com.apb.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.apb.beacon.common.AppUtil;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.login_screen_layout)
public class LoginActivity extends RoboActivity {
    @InjectView(R.id.password_edit_text)
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        goBack(null);
    }

    public void goBack(View view) {
        this.finish();
        Intent intent = new Intent(this, CalculatorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onEnterPressed(View view) {
        String password = passwordEditText.getText().toString();
        if (ApplicationSettings.passwordMatches(getApplicationContext(), password)) {
            startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
            return;
        }
        AppUtil.setError(this, passwordEditText, R.string.incorrect_password);
    }
}