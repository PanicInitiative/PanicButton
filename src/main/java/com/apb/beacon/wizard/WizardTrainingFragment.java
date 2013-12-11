package com.apb.beacon.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apb.beacon.R;

/**
 * Created by aoe on 12/10/13.
 */
public class WizardTrainingFragment extends WizardFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_training_screen, container, false);

        Button takeMeToTraining = (Button) view.findViewById(R.id.take_me_to_training);
        takeMeToTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WizardActivity)getActivity()).performAction(null);
            }
        });
        return view;
    }

}
