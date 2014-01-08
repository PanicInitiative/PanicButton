package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.LocalCachePage;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageItem;

public class SetupCodeFragment extends WizardFragment {

    private static final int EXACT_CHARACTERS = 4;

    private EditText passwordEditText;

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    ListView lvItems;
    LinearLayout llWarning;

    Page currentPage;
    PageItemAdapter pageItemAdapter;

    public static SetupCodeFragment newInstance(String pageId) {
        SetupCodeFragment f = new SetupCodeFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_interactive_code, container, false);
        passwordEditText = (EditText) view.findViewById(R.id.create_pin_edittext);
        passwordEditText.addTextChangedListener(passwordTextChangeListener);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>>>", "action button pressed");
                ApplicationSettings.savePassword(getActivity(), passwordEditText.getText().toString());

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
    public void onFragmentSelected() {
        actionButtonStateListener.enableActionButton(isComplete());
    }

    @Override
    public String action() {
        PBDatabase dbInstance = new PBDatabase(activity);
        dbInstance.open();
        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN);
        dbInstance.close();
        return page.getPageAction();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        if (activity != null) {
            bAction.setEnabled(isComplete());

            String pageId = getArguments().getString(PAGE_ID);
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if(currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else
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


    @Override
    public boolean performAction() {
        ApplicationSettings.savePassword(getActivity(), passwordEditText.getText().toString());
        return true;
    }

    private boolean isComplete() {
        return passwordEditText.getText().length() == EXACT_CHARACTERS;
    }

    @Override
    public void onBackPressed() {
        passwordEditText.setError(null);
    }

    private TextWatcher passwordTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable text) {
            bAction.setEnabled(isComplete());
        }
    };
}
