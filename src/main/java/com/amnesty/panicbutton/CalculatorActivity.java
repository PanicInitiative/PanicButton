package com.amnesty.panicbutton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amnesty.panicbutton.alert.PanicAlert;
import com.amnesty.panicbutton.trigger.HardwareTriggerService;
import com.amnesty.panicbutton.trigger.MultiClickEvent;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.calculator_layout)
public class CalculatorActivity extends RoboActivity {
    private int[] buttons = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
                             R.id.nine, R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply, R.id.divide};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerButtonEvents();
        startService(new Intent(this, HardwareTriggerService.class));
    }

    private void registerButtonEvents() {
        for(int button : buttons) {
            Button equalsButton = (Button) findViewById(button);
            equalsButton.setOnLongClickListener(longClickListener);
            equalsButton.setOnClickListener(clickListener);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MultiClickEvent multiClickEvent = (MultiClickEvent) view.getTag();
            if (multiClickEvent == null) {
                multiClickEvent = resetEvent(view);
            }
            multiClickEvent.registerClick(System.currentTimeMillis());
            if (multiClickEvent.isActivated()) {
                getPanicAlert().start();
                resetEvent(view);
            }
        }
    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            startActivity(new Intent(CalculatorActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.show_from_top, R.anim.hide_to_bottom);
            return true;
        }
    };

    private MultiClickEvent resetEvent(View view) {
        MultiClickEvent multiClickEvent = new MultiClickEvent();
        view.setTag(multiClickEvent);
        return multiClickEvent;
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(this);
    }
}