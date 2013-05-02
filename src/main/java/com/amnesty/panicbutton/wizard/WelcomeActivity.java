package com.amnesty.panicbutton.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.amnesty.panicbutton.CalculatorActivity;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static com.amnesty.panicbutton.wizard.ApplicationSettings.isFirstRun;

@ContentView(R.layout.welcome_screen)
public class WelcomeActivity extends RoboActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isFirstRun(this)) {
            startActivity(new Intent(this, CalculatorActivity.class));
        }
    }

    public void startWizard(View view) {
        startActivity(new Intent(this, WizardActivity.class));
    }
}