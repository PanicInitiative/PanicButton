package org.iilab.pb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.iilab.pb.calculator.CalculatorImpl;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.trigger.MultiClickEvent;

public class CalculatorActivity extends PanicButtonActivity {
	private static final int[] buttons = {R.id.one, R.id.two, R.id.three,
		R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
		R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply,
		R.id.divide, R.id.decimal_point, R.id.char_c};

	private CalculatorImpl calculator;
	private int lastClickId = -1;

    boolean mHasPerformedLongPress;
    Runnable mPendingCheckForLongPress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);
		registerButtonEvents();
//		startService(new Intent(this, HardwareTriggerService.class));

		calculator = new CalculatorImpl();
		
        ApplicationSettings.setWizardState(this, AppConstants.WIZARD_FLAG_HOME_READY);
	}

	private void registerButtonEvents() {
		for(int button : buttons) {
			Button equalsButton = (Button) findViewById(button);
            equalsButton.setOnTouchListener(touchListener);
//			equalsButton.setOnLongClickListener(longClickListener);
			equalsButton.setOnClickListener(clickListener);
		}
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			int id = view.getId();
			switch(id) {
			case R.id.one:
				handleButtonPress(CalculatorImpl.Button.ONE);
				break;
			case R.id.two:
				handleButtonPress(CalculatorImpl.Button.TWO);
				break;
			case R.id.three:
				handleButtonPress(CalculatorImpl.Button.THREE);
				break;
			case R.id.four:
				handleButtonPress(CalculatorImpl.Button.FOUR);
				break;
			case R.id.five:
				handleButtonPress(CalculatorImpl.Button.FIVE);
				break;
			case R.id.six:
				handleButtonPress(CalculatorImpl.Button.SIX);
				break;
			case R.id.seven:
				handleButtonPress(CalculatorImpl.Button.SEVEN);
				break;
			case R.id.eight:
				handleButtonPress(CalculatorImpl.Button.EIGHT);
				break;
			case R.id.nine:
				handleButtonPress(CalculatorImpl.Button.NINE);
				break;
			case R.id.zero:
				handleButtonPress(CalculatorImpl.Button.ZERO);
				break;
			case R.id.equals_sign:
				handleButtonPress(CalculatorImpl.Button.EQUALS);
				break;
			case R.id.plus:
				handleButtonPress(CalculatorImpl.Button.PLUS);
				break;
			case R.id.minus:
				handleButtonPress(CalculatorImpl.Button.MINUS);
				break;
			case R.id.multiply:
				handleButtonPress(CalculatorImpl.Button.MULTIPLY);
				break;
			case R.id.divide:
				handleButtonPress(CalculatorImpl.Button.DIVIDE);
				break;
			case R.id.char_c:
				handleButtonPress(CalculatorImpl.Button.CHAR_C);
				break;
			case R.id.decimal_point:
				handleButtonPress(CalculatorImpl.Button.DECIMAL_POINT);
				break;
			}
			MultiClickEvent multiClickEvent = (MultiClickEvent) view.getTag();
			if (multiClickEvent == null) {
				multiClickEvent = resetEvent(view);
			}
			// Don't activate multi-click if different buttons are clicked
			if(id != lastClickId) multiClickEvent.reset();
			lastClickId = id;
			multiClickEvent.registerClick(System.currentTimeMillis());

			if(multiClickEvent.skipCurrentClick()){
				multiClickEvent.resetSkipCurrentClickFlag();
				return;
			}
			if(multiClickEvent.canStartVibration()){
				getPanicAlert().vibrate();
				CharSequence text = ((Button) view).getText();
				//Toast.makeText(getApplicationContext(), "Press the button '" + text + "' once the vibration ends to trigger alerts", Toast.LENGTH_LONG).show();
			}
			else if (multiClickEvent.isActivated()) {
				CalculatorActivity.this.finish();
                getPanicAlert().activate();
				resetEvent(view);
			}
		}
	};

    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:

                    if (!mHasPerformedLongPress) {
                        // This is a tap, so remove the longpress check
                        if (mPendingCheckForLongPress != null) {
                            v.removeCallbacks(mPendingCheckForLongPress);
                        }
                        // v.performClick();
                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                    if (mPendingCheckForLongPress == null) {
                        mPendingCheckForLongPress = new Runnable() {
                            public void run() {
                                startActivity(new Intent(CalculatorActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.show_from_top, R.anim.hide_to_bottom);
                            }
                        };
                    }


                    mHasPerformedLongPress = false;
                    v.postDelayed(mPendingCheckForLongPress, 3000);

                    break;
                case MotionEvent.ACTION_MOVE:
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();

                    // Be lenient about moving outside of buttons
                    int slop = ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
                    if ((x < 0 - slop) || (x >= v.getWidth() + slop) ||
                            (y < 0 - slop) || (y >= v.getHeight() + slop)) {

                        if (mPendingCheckForLongPress != null) {
                            v.removeCallbacks(mPendingCheckForLongPress);
                        }
                    }
                    break;
                default:
                    return false;
            }

            return false;
        }

    };

//	private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
//		@Override
//		public boolean onLongClick(View view) {
//			startActivity(new Intent(CalculatorActivity.this, LoginActivity.class));
//			overridePendingTransition(R.anim.show_from_top, R.anim.hide_to_bottom);
//			return true;
//		}
//	};

	private MultiClickEvent resetEvent(View view) {
		MultiClickEvent multiClickEvent = new MultiClickEvent();
		view.setTag(multiClickEvent);
		return multiClickEvent;
	}

	private void handleButtonPress(CalculatorImpl.Button button) {
		TextView display = (TextView) findViewById(R.id.display);
		String displayText = calculator.handleButtonPress(button);
		display.setText(displayText);
	}
	
	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	AppUtil.unbindDrawables(getWindow().getDecorView().findViewById(android.R.id.content));
        System.gc();
    }
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
//		finish();
		Log.d("CDA", "onBackPressed Called");
		   startActivity(AppUtil.behaveAsHomeButton());
	}
}