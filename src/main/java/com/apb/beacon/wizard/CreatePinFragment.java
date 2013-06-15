package com.apb.beacon.wizard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.common.AppUtil;

import java.util.regex.Pattern;

public class CreatePinFragment extends WizardFragment {
    private static final int EXACT_CHARACTERS = 4;
    private EditText passwordEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.wizard_password_screen, container, false);
        passwordEditText = (EditText) inflatedView.findViewById(R.id.create_pin_edittext);
        passwordEditText.addTextChangedListener(passwordTextChangeListener);

        return inflatedView;
    }

    @Override
    public void onFragmentSelected() {
        actionButtonStateListener.enableActionButton(isComplete());
    }

    @Override
    public String action() {
        return getString(WizardAction.SAVE.actionId());
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
