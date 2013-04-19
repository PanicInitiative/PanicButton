package com.amnesty.panicbutton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.calculator_layout)
public class CalculatorActivity extends RoboActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button equalsButton = (Button) findViewById(R.id.equals_sign);
        equalsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(CalculatorActivity.this, SettingsActivity.class));
                return true;
            }
        });
    }
}