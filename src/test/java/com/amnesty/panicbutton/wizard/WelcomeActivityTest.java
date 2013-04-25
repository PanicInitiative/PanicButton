package com.amnesty.panicbutton.wizard;

import android.content.Intent;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WelcomeActivityTest {
    @Test
    public void shouldStartTheWizardOnPressingPanicButtonTextView() {
        WelcomeActivity welcomeActivity = new WelcomeActivity();
        welcomeActivity.onCreate(null);
        TextView panicButtonText = (TextView) welcomeActivity.findViewById(R.id.panic_button_text);
        ShadowActivity shadowActivity = shadowOf(welcomeActivity);

        panicButtonText.performClick();

        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(WizardActivity.class.getName(), nextStartedActivity.getComponent().getClassName());
    }
}
