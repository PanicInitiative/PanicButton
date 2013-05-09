package com.amnesty.panicbutton.wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import com.amnesty.panicbutton.CalculatorActivity;
import com.amnesty.panicbutton.HomeActivity;
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
public class HomeActivityTest {
    private HomeActivity homeActivity;
    private ShadowActivity shadowActivity;

    @Before
    public void setUp() {
        homeActivity = new HomeActivity();
        shadowActivity = shadowOf(homeActivity);
    }

    @Test
    public void shouldDisplayWizardWhenUserHasNotFinishedTheWizard() {
        setFirstRun(true);
        homeActivity.onCreate(null);

        assertNull(shadowActivity.getNextStartedActivity());
    }

    @Test
    public void shouldStartCalculatorFacadeWhenUserCompletedWizard() {
        setFirstRun(false);
        homeActivity.onCreate(null);

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
