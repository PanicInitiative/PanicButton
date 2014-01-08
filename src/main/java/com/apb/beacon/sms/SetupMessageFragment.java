package com.apb.beacon.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.common.MessageFragment;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageItem;
import com.apb.beacon.model.SMSSettings;
import com.apb.beacon.wizard.WizardAction;
import com.apb.beacon.wizard.WizardActivity;
import com.apb.beacon.wizard.WizardFragment;

/**
 * Created by aoe on 12/12/13.
 */
public class SetupMessageFragment extends WizardFragment {
    private EditText smsEditText;

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    ListView lvItems;
    LinearLayout llWarning;

    Page currentPage;
    PageItemAdapter pageItemAdapter;

    public static SetupMessageFragment newInstance(String pageId) {
        SetupMessageFragment f = new SetupMessageFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wizard_interactive_message, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>>>", "action button pressed");
                String msg =  getSMSSettingsFromView();

                SMSSettings.saveMessage(activity, msg);
                displaySettings(msg);

                String pageId = currentPage.getAction().get(0).getLink();
                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
            }
        });


        lvItems = (ListView) view.findViewById(R.id.fragment_item_list);

        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) view.findViewById(R.id.fragment_warning);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageItem selectedItem = (PageItem) parent.getItemAtPosition(position);

                String pageId = selectedItem.getLink();
                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);

            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.sms_message);
            ((MessageFragment)fragment).setActionButtonStateListener(bAction);
            smsEditText = (EditText) fragment.getView().findViewById(R.id.message_edit_text);

            String currentMsg = SMSSettings.retrieveMessage(activity);
            if(currentMsg != null) {
                displaySettings(currentMsg);
            }
            bAction.setEnabled(!smsEditText.getText().toString().trim().equals(""));

            String pageId = getArguments().getString(PAGE_ID);
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());
            tvContent.setText(Html.fromHtml(currentPage.getContent()));

            if(currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());

            if(currentPage.getWarning() == null)
                llWarning.setVisibility(View.GONE);
            else
                tvWarning.setText(currentPage.getWarning());

            bAction.setText(currentPage.getAction().get(0).getTitle());

            pageItemAdapter = new PageItemAdapter(activity, null);
            lvItems.setAdapter(pageItemAdapter);
            pageItemAdapter.setData(currentPage.getItems());

        }
    }


    private void displaySettings(String msg) {
        smsEditText.setText(msg);
    }


    @Override
    public String action() {
        return getString(WizardAction.NEXT.actionId());
    }

    @Override
    public boolean performAction() {
        String msg =  getSMSSettingsFromView();

        SMSSettings.saveMessage(context, msg);
        displaySettings(msg);
        return true;
    }

    private String getSMSSettingsFromView() {
        String message = smsEditText.getText().toString().trim();
        return message;
    }

    @Override
    public void onFragmentSelected() {
//        if (actionButtonStateListener != null) {
//            actionButtonStateListener.enableActionButton(!smsEditText.getText().toString().trim().equals(""));
//        }
    }

}
