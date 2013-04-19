package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class CalculatorActivityTest {
    private CalculatorActivity calculatorActivity;
    private Button equalsButton;

    @Test
    public void shouldNavigateToSettingsScreenOnLongPress() {
        calculatorActivity = new CalculatorActivity();
        calculatorActivity.onCreate(null);
        equalsButton = (Button) calculatorActivity.findViewById(R.id.equals_sign);

        equalsButton.performLongClick();

        ShadowActivity shadowActivity = shadowOf(calculatorActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent.getComponent().getClassName(), equalTo(SettingsActivity.class.getName()));
    }
}
