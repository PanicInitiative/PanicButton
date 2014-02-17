package com.apb.beacon.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.R;

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
                getActivity().overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
                getActivity().finish();
            }
        });
        return view;
    }
}
