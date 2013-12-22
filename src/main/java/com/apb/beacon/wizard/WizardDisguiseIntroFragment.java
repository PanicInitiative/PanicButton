package com.apb.beacon.wizard;

import android.app.Activity;
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
 * Created by aoe on 12/20/13.
 */
public class WizardDisguiseIntroFragment extends WizardFragment {

    private Activity activity;
    protected ActionButtonTextListener actionButtonTextListener;

    TextView tvTitle, tvContentBody;
    Button bOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_disguise_intro, container, false);

        tvTitle = (TextView) view.findViewById(R.id.title);
        tvContentBody = (TextView) view.findViewById(R.id.content_body);

        bOption = (Button) view.findViewById(R.id.option);
        bOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WizardActivity)getActivity()).performAction(null);
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        setActionButtonTextListener(activity);
        showPageContentToUI();
    }

    private void setActionButtonTextListener(Activity activity){
        if (activity instanceof ActionButtonTextListener)
            this.actionButtonTextListener = (ActionButtonTextListener) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void showPageContentToUI() {
        PBDatabase dbInstance = new PBDatabase(activity);
        dbInstance.open();
        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_DISGUISE_INTRO);
        dbInstance.close();

        tvTitle.setText(page.getPageTitle());
        tvContentBody.setText(page.getPageContent());

        if(page.getPageOption() == null){
            bOption.setVisibility(View.GONE);
        } else{
            bOption.setVisibility(View.VISIBLE);
            bOption.setText(page.getPageOption());
        }
    }

    @Override
    public String action() {
        PBDatabase dbInstance = new PBDatabase(activity);
        dbInstance.open();
        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_DISGUISE_INTRO);
        dbInstance.close();
        return page.getPageAction();
    }
}
