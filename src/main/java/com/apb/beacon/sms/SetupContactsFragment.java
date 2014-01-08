package com.apb.beacon.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.common.ContactEditTexts;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageItem;
import com.apb.beacon.model.SMSSettings;
import com.apb.beacon.wizard.WizardAction;
import com.apb.beacon.wizard.WizardActivity;
import com.apb.beacon.wizard.WizardFragment;

import java.util.List;

/**
 * Created by aoe on 12/11/13.
 */
public class SetupContactsFragment extends WizardFragment {
//    public static final String HEADER_TEXT_ID = "HEADER_TEXT_ID";
    private ContactEditTexts contactEditTexts;
//    private EditText smsEditText;

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    ListView lvItems;
    LinearLayout llWarning;

    Page currentPage;
    PageItemAdapter pageItemAdapter;

    public static SetupContactsFragment newInstance(String pageId) {
        SetupContactsFragment f = new SetupContactsFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
    }

//    public static SMSSettingsFragment create(int headerTextId) {
//        SMSSettingsFragment smsSettingsFragment = new SMSSettingsFragment();
//        Bundle bundle = new Bundle();
////        bundle.putInt(HEADER_TEXT_ID, headerTextId);
//        smsSettingsFragment.setArguments(bundle);
//
//        return smsSettingsFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_interactive_contacts, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>>>", "action button pressed");
                SMSSettings newSMSSettings = getSMSSettingsFromView();

                SMSSettings.save(context, newSMSSettings);
                displaySettings(newSMSSettings);

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

//        initializeViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            contactEditTexts = new ContactEditTexts(getFragmentManager(), bAction, activity);

            SMSSettings currentSettings = SMSSettings.retrieve(context);
            if(currentSettings.isConfigured()) {
                displaySettings(currentSettings);
            }
            bAction.setEnabled(contactEditTexts.hasAtleastOneValidPhoneNumber());

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
//            Log.e(">>>>>>>>", "item count = " + currentPage.getItems().size());
            pageItemAdapter.setData(currentPage.getItems());

        }
    }

    private void initializeViews(View inflate) {
//        contactEditTexts = new ContactEditTexts(getFragmentManager(), actionButtonStateListener, getActivity());
//        Fragment fragment = getFragmentManager().findFragmentById(R.id.sms_message);
//        smsEditText = (EditText) fragment.getView().findViewById(R.id.message_edit_text);

//        TextView headerTextView = (TextView) inflate.findViewById(R.id.sms_settings_header);
//        headerTextView.setText(getString(getArguments().getInt(HEADER_TEXT_ID)));
    }

    private void displaySettings(SMSSettings settings) {
//        smsEditText.setText(settings.message());
        contactEditTexts.maskPhoneNumbers();
    }

//    @Override
//    protected int[] getFragmentIds() {
//        return new int[]{first_contact, second_contact, third_contact};
//    }

    @Override
    public String action() {
        return getString(WizardAction.NEXT.actionId());
    }

    @Override
    public boolean performAction() {
        SMSSettings newSMSSettings = getSMSSettingsFromView();

        SMSSettings.save(context, newSMSSettings);
        displaySettings(newSMSSettings);
        return true;
    }

    private SMSSettings getSMSSettingsFromView() {
//        String message = smsEditText.getText().toString();
        List<String> phoneNumbers = contactEditTexts.getPhoneNumbers();
        return new SMSSettings(phoneNumbers);
    }

//    @Override
//    public void onFragmentSelected() {
//        Log.e(">>>>>>", "in onFragmentSelected");
////        if (actionButtonStateListener != null) {
////            actionButtonStateListener.enableActionButton(contactEditTexts.hasAtleastOneValidPhoneNumber());
////        }
//    }

    public boolean hasSettingsChanged() {
        SMSSettings existingSettings = SMSSettings.retrieve(getActivity());
        return !existingSettings.equals(getSMSSettingsFromView());
    }
}
