package com.apb.beacon;

import android.content.Intent;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.apb.beacon.alert.AlertStatus;
import com.apb.beacon.alert.PanicAlert;
import com.apb.beacon.test.support.PanicButtonRobolectricTestRunner;
import com.apb.beacon.trigger.HardwareTriggerService;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.shadows.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(PanicButtonRobolectricTestRunner.class)
public class CalculatorActivityTest {
    private CalculatorActivity calculatorActivity;
    private Button equalsButton;
    private ShadowActivity shadowActivity;
    @Mock
    private PanicAlert mockPanicAlert;
    private ShadowViewGroup alertStatusStrip;

    @Before
    public void setUp() {
        initMocks(this);
        calculatorActivity = new CalculatorActivity() {
            @Override
            PanicAlert getPanicAlert() {
                return mockPanicAlert;
            }
        };
        calculatorActivity.onCreate(null);
        equalsButton = (Button) calculatorActivity.findViewById(R.id.equals_sign);
        shadowActivity = shadowOf(calculatorActivity);
        RelativeLayout alertStatusStripOrig = (RelativeLayout) calculatorActivity.findViewById(R.id.alert_status_strip);
        alertStatusStrip = shadowOf(alertStatusStripOrig);
    }

    @Test
    public void shouldNavigateToLoginScreenOnEqualSignLongPress() {
        equalsButton.performLongClick();

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent.getComponent().getClassName(), equalTo(LoginActivity.class.getName()));
    }

    @Test
    public void shouldActivateAlertOnPressingEqualSignRepeatedlyFiveTimes() {
        for (int i = 0; i < 5; i++) {
            equalsButton.performClick();
        }
        verify(mockPanicAlert).activate();
        assertTrue(shadowActivity.isFinishing());
    }

    @Test
    public void shouldNotActivateAlertOnPressingEqualSignRepeatedlyLesserThanFiveTimes() {
        for (int i = 0; i < 3; i++) {
            equalsButton.performClick();
        }
        verifyNoMoreInteractions(mockPanicAlert);
    }

    @Test
    public void shouldStartHardwareTriggerServiceOnApplicationCreation() {
        Intent startedIntent = shadowActivity.getNextStartedService();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertNotNull(startedIntent);
        assertEquals(HardwareTriggerService.class, shadowIntent.getIntentClass());
    }

    @Test
    public void shouldCreateNewPanicAlert() {
        assertNotNull(new CalculatorActivity().getPanicAlert());
    }

    @Test
    public void shouldShowRedAlertStripWhenAlertStatusIsActive(){
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.ACTIVE);
        calculatorActivity.onResume();
        assertThat(alertStatusStrip.getBackgroundColor(), Is.is(Robolectric.application.getResources().getColor(R.color.active_color)));
    }

    @Test
    public void shouldShowAmberAlertStripWhenAlertStatusIsStandby(){
        when(mockPanicAlert.getAlertStatus()).thenReturn(AlertStatus.STANDBY);
        calculatorActivity.onResume();
        assertThat(alertStatusStrip.getBackgroundColor(), Is.is(Robolectric.application.getResources().getColor(R.color.standby_color)));
    }
}
