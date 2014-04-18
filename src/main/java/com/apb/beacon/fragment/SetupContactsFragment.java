package com.apb.beacon.fragment;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.common.AppConstants;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.MainActivity;
import com.apb.beacon.R;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.common.ContactEditTexts;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageItem;
import com.apb.beacon.model.SMSSettings;
import com.apb.beacon.WizardActivity;

import java.util.List;

/**
 * Created by aoe on 12/11/13.
 */
public class SetupContactsFragment extends Fragment {
//    public static final String HEADER_TEXT_ID = "HEADER_TEXT_ID";
    private ContactEditTexts contactEditTexts;
//    private EditText smsEditText;

    private static final String PAGE_ID = "page_id";
    private static final String PARENT_ACTIVITY = "parent_activity";
    private Activity activity;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    ListView lvItems;
    LinearLayout llWarning;

    Page currentPage;
    PageItemAdapter pageItemAdapter;

    public static SetupContactsFragment newInstance(String pageId, int parentActivity) {
        SetupContactsFragment f = new SetupContactsFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
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
        View view = inflater.inflate(R.layout.fragment_type_interactive_contacts, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>>>", "action button pressed");
                SMSSettings newSMSSettings = getSMSSettingsFromView();

                SMSSettings.saveContacts(activity, newSMSSettings);
                displaySettings(newSMSSettings);

                String pageId = currentPage.getAction().get(0).getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
                    i = new Intent(activity, WizardActivity.class);
                } else{
                	AppUtil.showToast("Contacts saved.", 1000, activity);
                    i = new Intent(activity, MainActivity.class);
                }

//                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);

                if(parentActivity == AppConstants.FROM_MAIN_ACTIVITY){
                    activity.finish();
                }
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
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
                    i = new Intent(activity, WizardActivity.class);
                } else{
                    i = new Intent(activity, MainActivity.class);
                }
//                Intent i = new Intent(activity, WizardActivity.class);
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

            SMSSettings currentSettings = SMSSettings.retrieve(activity);
            if(currentSettings.isConfigured()) {
                displaySettings(currentSettings);
            }
            bAction.setEnabled(contactEditTexts.hasAtleastOneValidPhoneNumber());

            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if(currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));

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
