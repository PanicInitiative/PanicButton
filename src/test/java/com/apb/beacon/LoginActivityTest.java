package com.apb.beacon;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    private EditText pinEditText;
    private Button enterButton;
    private Button backButton;

    private LoginActivity loginActivity;
    private ShadowActivity shadowActivity;
    private String pin;

    @Before
    public void setUp() {
        loginActivity = new LoginActivity();
        loginActivity.onCreate(null);

        pin = "1234";
        ApplicationSettings.savePassword(Robolectric.application, pin);

        pinEditText = (EditText) loginActivity.findViewById(R.id.pin_edit_text);
        enterButton = (Button) loginActivity.findViewById(R.id.enter_button);
        backButton = (Button) loginActivity.findViewById(R.id.login_back_button);
        shadowActivity = shadowOf(loginActivity);
    }

    @Test
    public void shouldNavigateToSettingsScreenOnPressingNextAfterEnteringCorrectPin() {
        pinEditText.setText(pin);
        enterButton.performClick();
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent.getComponent().getClassName(), equalTo(SettingsActivity.class.getName()));
    }

    @Test
    public void shouldShowErrorOnPressingNextAfterEnteringInCorrectPin() {
        pinEditText.setText("9999");
        enterButton.performClick();
        assertTrue(pinEditText.getError().toString().contains("Please enter the correct pin"));
    }

    @Test
    public void shouldGoBackToSettingOnClickingBack() {
        backButton.performClick();
        assertTrue(shadowOf(loginActivity).isFinishing());
    }
}
