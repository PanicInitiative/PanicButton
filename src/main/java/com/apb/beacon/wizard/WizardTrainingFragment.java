package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apb.beacon.AppConstants;
import com.apb.beacon.R;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.LocalCachePage;

/**
 * Created by aoe on 12/10/13.
 */
public class WizardTrainingFragment extends WizardFragment{

    private Activity activity;
    protected ActionButtonTextListener actionButtonTextListener;

    TextView tvTitle, tvContentBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_training_screen, container, false);

        tvTitle = (TextView) view.findViewById(R.id.title);
        tvContentBody = (TextView) view.findViewById(R.id.content_body);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        setActionButtonTextListener(activity);
    }

    private void setActionButtonTextListener(Activity activity){
        if (activity instanceof ActionButtonTextListener)
            this.actionButtonTextListener = (ActionButtonTextListener) activity;
    }

    @Override
    public void onResume() {
        super.onResume();

        showPageContentToUI();
    }


    private void showPageContentToUI() {
        PBDatabase dbInstance = new PBDatabase(activity);
        dbInstance.open();
        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING);
        dbInstance.close();

        tvTitle.setText(page.getPageTitle());
        tvContentBody.setText(page.getPageContent());
    }

    @Override
    public String action() {
        PBDatabase dbInstance = new PBDatabase(activity);
        dbInstance.open();
        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING);
        dbInstance.close();
        return page.getPageAction();
    }

}
