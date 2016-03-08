package org.iilab.pb;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.iilab.pb.common.AppConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboVibrator;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class CalculatorActivityTest {


	private CalculatorActivity calculatorActivity;
	private Button equalsButton, zeroButton;
	private RoboVibrator shadowVibrator;

	private ShadowActivity shadowActivity;

	@Before
	public void setUp() {

		ShadowLog.stream = System.out;
//		calculatorActivity = new CalculatorActivity() {
//			@Override
//			PanicAlert getPanicAlert() {
//				return mockPanicAlert;
//			}
//		};
//		calculatorActivity.onCreate(new Bundle());

//		ActivityController<CalculatorActivity> activityController = Robolectric.buildActivity(CalculatorActivity.class);
		Context context = RuntimeEnvironment.application;
		calculatorActivity= Robolectric.setupActivity(CalculatorActivity.class);
		shadowVibrator = (RoboVibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//		initMocks(this);

		zeroButton = (Button) calculatorActivity.findViewById(R.id.zero);
		shadowActivity = Shadows.shadowOf(calculatorActivity);
		equalsButton = (Button) shadowActivity.findViewById(R.id.equals_sign);



	}

	@Test
	public void shouldNavigateToLoginScreenOnButtonLongPress() {
		equalsButton.performLongClick();

		Intent startedIntent = shadowActivity.peekNextStartedActivity();
//		assertThat(startedIntent.getComponent().getClassName(), equalTo(LoginActivity.class.getName()));
//		assertEquals(LoginActivity.class.getCanonicalName(), startedIntent.getComponent().getClassName());
	}

	@Test
	public void shouldVibrateOnPressingButtonRepeatedlyFourTimes() {
		for (int i = 0; i < 5; i++) {
			equalsButton.performClick();
		}

//		assertEquals(2000, shadowVibrator.getMilliseconds());
//		verify(mockPanicAlert).vibrateForHapticFeedback();
	}

	@Test
	public void shouldActivateAlertOnPressingButtonFifthTime() {
        for (int i = 0; i < 5; i++) {
            equalsButton.performClick();
        }
        try {
            Thread.sleep(Integer.parseInt(AppConstants.DEFAULT_HAPTIC_FEEDBACK_DURATION) + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        equalsButton.performClick();
//		assertEquals(500, shadowVibrator.getMilliseconds());
//        verify(mockPanicAlert).vibrateForHapticFeedback();
//        verify(mockPanicAlert).activate();
	}
//
	@Test
	public void shouldVibrateOnPressingButtonRepeatedlyFourTimesAfterOtherButtons() {
		zeroButton.performClick();
		for (int i = 0; i < 5; i++) {
			equalsButton.performClick();
		}
//		assertEquals(2000, shadowVibrator.getMilliseconds());
//		verify(mockPanicAlert).vibrateForHapticFeedback();
	}
//
	@Test
	public void shouldNotVibrateOnPressingButtonRepeatedlyLessThanFiveTimes() {
		for (int i = 0; i < 3; i++) {
			equalsButton.performClick();
		}
		assertEquals(0, shadowVibrator.getMilliseconds());
//		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void shouldNotVibrateOnPressingButtonRepeatedlyLessThanFiveTimesAfterOtherButtons() {
		zeroButton.performClick();
		for (int i = 0; i < 3; i++) {
			equalsButton.performClick();
		}
		assertEquals(0, shadowVibrator.getMilliseconds());
//		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void shouldNotVibrateOnPressingButtonRepeatedlyMixedWithOtherButtons() {
		for (int i = 0; i < 4; i++) {
			equalsButton.performClick();
			zeroButton.performClick();
		}
		assertEquals(0, shadowVibrator.getMilliseconds());
//		verifyNoMoreInteractions(mockPanicAlert);
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
//	@Test
	public void shouldCreateNewPanicAlert() {
		assertNotNull(calculatorActivity.getPanicAlert());
	}
}
