package org.iilab.pb;

import android.widget.Button;
import android.widget.EditText;


import org.iilab.pb.LoginActivity;
import org.iilab.pb.common.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

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

        shadowActivity = shadowOf(loginActivity);
    }

    @Test
    public void testTest() {
        assert(true);
    }

}
