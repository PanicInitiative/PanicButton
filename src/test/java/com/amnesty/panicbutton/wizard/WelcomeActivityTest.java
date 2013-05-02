package com.amnesty.panicbutton.wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import com.amnesty.panicbutton.CalculatorActivity;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class WelcomeActivityTest {
    private WelcomeActivity welcomeActivity;
    private ShadowActivity shadowActivity;

    @Before
    public void setUp() {
        welcomeActivity = new WelcomeActivity();
        shadowActivity = shadowOf(welcomeActivity);
    }

    @Test
    public void shouldStartTheWizardOnPressingPanicButtonTextView() {
        welcomeActivity.onCreate(null);
        TextView panicButtonText = (TextView) welcomeActivity.findViewById(R.id.panic_button_text);

        panicButtonText.performClick();

        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        assertEquals(WizardActivity.class.getName(), nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void shouldDisplayWizardWhenUserHasNotFinishedTheWizard() {
        setFirstRun(true);
        welcomeActivity.onCreate(null);

        assertNull(shadowActivity.getNextStartedActivity());
    }

    @Test
    public void shouldStartCalculatorFacadeWhenUserCompletedWizard() {
        setFirstRun(false);
        welcomeActivity.onCreate(null);

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertEquals(CalculatorActivity.class.getName(), startedIntent.getComponent().getClassName());
    }

    @Test
    public void shouldReturnAnInstance(){
        assertNotNull(new ApplicationSettings());
    }

    private void setFirstRun(boolean flag) {
        SharedPreferences sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("FIRST_RUN", flag);
        editor.commit();
    }
}
