package com.apb.beacon.wizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apb.beacon.R;

/**
 * Created by aoe on 12/11/13.
 */
public class WizardTrainingContactLearnMoreFragment extends WizardFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_training_contacts_learn_more, container, false);

        Button bIUnderstand = (Button) view.findViewById(R.id.option);
        bIUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WizardActivity)getActivity()).performAction(null);
            }
        });
        return view;
    }
}
