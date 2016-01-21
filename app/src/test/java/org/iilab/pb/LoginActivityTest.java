package org.iilab.pb;

import android.widget.Button;
import android.widget.EditText;

import org.iilab.pb.common.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
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
//      loginActivity.onCreate(null);

        pin = "1234";
        ApplicationSettings.savePassword(RuntimeEnvironment.application, pin);

        shadowActivity = Shadows.shadowOf(loginActivity);
    }

    @Test
    public void testTest() {
        assert(true);
    }

}
