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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindLongPress();
        startService(new Intent(this, HardwareTriggerService.class));
    }

    private void bindLongPress() {
        Button equalsButton = (Button) findViewById(R.id.equals_sign);
        equalsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(CalculatorActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
                return true;
            }
        });
    }

    public void onButtonClick(View view) {
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

    private MultiClickEvent resetEvent(View view) {
        MultiClickEvent multiClickEvent = new MultiClickEvent();
        view.setTag(multiClickEvent);
        return multiClickEvent;
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(this);
    }
}