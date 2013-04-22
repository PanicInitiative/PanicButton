package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import com.amnesty.panicbutton.trigger.HardwareTriggerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class CalculatorActivityTest {
    private CalculatorActivity calculatorActivity;
    private Button equalsButton;

    @Before
    public void setUp() {
        calculatorActivity = new CalculatorActivity();
        calculatorActivity.onCreate(null);
    }

    @Test
    public void shouldNavigateToSettingsScreenOnLongPress() {
        equalsButton = (Button) calculatorActivity.findViewById(R.id.equals_sign);
        equalsButton.performLongClick();

        ShadowActivity shadowActivity = shadowOf(calculatorActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent.getComponent().getClassName(), equalTo(SettingsActivity.class.getName()));
    }

    @Test
    public void shouldStartHardwareTriggerServiceOnApplicationCreation() {
        ShadowActivity shadowActivity = shadowOf(calculatorActivity);
        Intent startedIntent = shadowActivity.getNextStartedService();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertNotNull(startedIntent);
        assertEquals(HardwareTriggerService.class, shadowIntent.getIntentClass());
    }
}
