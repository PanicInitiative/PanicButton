package org.iilab.pb;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import org.iilab.pb.alert.PanicAlert;
import org.iilab.pb.common.AppConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class CalculatorActivityTest {
	private CalculatorActivity calculatorActivity;
	private Button equalsButton, zeroButton;
	private ShadowActivity shadowActivity;
	@Mock
	private PanicAlert mockPanicAlert;

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
		zeroButton = (Button) calculatorActivity.findViewById(R.id.zero);
		shadowActivity = shadowOf(calculatorActivity);
	}

	@Test
	public void shouldNavigateToLoginScreenOnButtonLongPress() {
		equalsButton.performLongClick();

		Intent startedIntent = shadowActivity.getNextStartedActivity();
//		assertThat(startedIntent.getComponent().getClassName(), equalTo(LoginActivity.class.getName()));
	}

	@Test
	public void shouldVibrateOnPressingButtonRepeatedlyFourTimes() {
		for (int i = 0; i < 4; i++) {
			equalsButton.performClick();
		}
		verify(mockPanicAlert).vibrate();
	}

	@Test
	public void shouldActivateAlertOnPressingButtonFifthTime() {
        for (int i = 0; i < 4; i++) {
            equalsButton.performClick();
        }
        try {
            Thread.sleep(AppConstants.HAPTIC_FEEDBACK_DURATION + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        equalsButton.performClick();

        verify(mockPanicAlert).vibrate();
        verify(mockPanicAlert).activate();
	}

	@Test
	public void shouldVibrateOnPressingButtonRepeatedlyFourTimesAfterOtherButtons() {
		zeroButton.performClick();
		for (int i = 0; i < 4; i++) {
			equalsButton.performClick();
		}
		verify(mockPanicAlert).vibrate();
	}

	@Test
	public void shouldNotVibrateOnPressingButtonRepeatedlyLessThanFiveTimes() {
		for (int i = 0; i < 3; i++) {
			equalsButton.performClick();
		}
		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void shouldNotVibrateOnPressingButtonRepeatedlyLessThanFiveTimesAfterOtherButtons() {
		zeroButton.performClick();
		for (int i = 0; i < 3; i++) {
			equalsButton.performClick();
		}
		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void shouldNotVibrateOnPressingButtonRepeatedlyMixedWithOtherButtons() {
		for (int i = 0; i < 4; i++) {
			equalsButton.performClick();
			zeroButton.performClick();
		}
		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void testPressingCalculatorButtonsAffectsDisplay() {
		// 123 + 456 - 78 * 90 = 45090
		int[] ids = { R.id.one, R.id.two, R.id.three, R.id.plus, R.id.four,
				R.id.five, R.id.six, R.id.minus, R.id.seven, R.id.eight,
				R.id.multiply, R.id.nine, R.id.zero, R.id.equals_sign
		};
		for(int id : ids) {
			Button button = (Button) calculatorActivity.findViewById(id);
			button.performClick();
		}
		TextView display = (TextView) calculatorActivity.findViewById(R.id.display);
		assertEquals("45090", display.getText());
	}

/*	@Test
	public void shouldStartHardwareTriggerServiceOnApplicationCreation() {
		Intent startedIntent = shadowActivity.getNextStartedService();
		ShadowIntent shadowIntent = shadowOf(startedIntent);
		assertNotNull(startedIntent);
		assertEquals(HardwareTriggerService.class, shadowIntent.getIntentClass());
	}
*/
	@Test
	public void shouldCreateNewPanicAlert() {
		assertNotNull(new CalculatorActivity().getPanicAlert());
	}
}
