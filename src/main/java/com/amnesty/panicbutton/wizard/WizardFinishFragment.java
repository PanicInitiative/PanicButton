package com.amnesty.panicbutton.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.amnesty.panicbutton.CalculatorActivity;
import com.amnesty.panicbutton.R;

public class WizardFinishFragment extends WizardFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_finish_screen, container, false);
        Button finishButton = (Button) view.findViewById(R.id.finish_wizard_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationSettings.completeFirstRun(context);
                startActivity(new Intent(context, CalculatorActivity.class));
            }
        });
        return view;
    }
}
