package com.apb.beacon.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.LocalCachePage;

public class CreatePinFragment extends WizardFragment {

    private static final int EXACT_CHARACTERS = 4;

    private EditText passwordEditText;
    TextView tvTitle, tvContentBody;

    private Activity activity;
    protected ActionButtonTextListener actionButtonTextListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_traning_pin, container, false);
        passwordEditText = (EditText) view.findViewById(R.id.create_pin_edittext);
        passwordEditText.addTextChangedListener(passwordTextChangeListener);

        tvTitle = (TextView) view.findViewById(R.id.title);
        tvContentBody = (TextView) view.findViewById(R.id.content_body);

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
        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN);
        dbInstance.close();

        tvTitle.setText(page.getPageTitle());
        tvContentBody.setText(page.getPageContent());
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
            actionButtonStateListener.enableActionButton(isComplete());
        }
    };
}
