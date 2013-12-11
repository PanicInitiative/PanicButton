package com.apb.beacon.wizard;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apb.beacon.R;


/**
 * Created by aoe on 12/11/13.
 */
public class WizardTrainingContactInfoFragment extends WizardFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_training_contacts_intro, container, false);

        TextView tvLearnMore = (TextView) view.findViewById(R.id.b_learn_more);
        tvLearnMore.setPaintFlags(tvLearnMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLearnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((WizardActivity)getActivity()).performAction(null);
            }
        });

        Button bIUnderstand = (Button) view.findViewById(R.id.i_understand);
        bIUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WizardActivity)getActivity()).performActionWithSkip();
            }
        });
        return view;
    }
}
