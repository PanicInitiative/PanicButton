package com.amnesty.panicbutton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amnesty.panicbutton.trigger.HardwareTriggerService;
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
}