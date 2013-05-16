package com.amnesty.panicbutton.wizard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.amnesty.panicbutton.R;

import java.util.regex.Pattern;

public class CreatePasswordFragment extends WizardFragment {
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[ ]).{8,})";
    private static final int MIN_CHARACTERS = 8;
    private static Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    private EditText passwordEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.wizard_password_screen, container, false);
        passwordEditText = (EditText) inflatedView.findViewById(R.id.create_password_edittext);
        passwordEditText.addTextChangedListener(passwordTextChangeListener);

        return inflatedView;
    }

    @Override
    public void onFragmentSelected() {
        actionButtonStateListener.onActionStateChanged(hasMinimumCharacters());
    }

    @Override
    public String action() {
        return getString(WizardAction.SAVE.actionId());
    }

    @Override
    public boolean performAction() {
        if(isValidPassword()) {
            ApplicationSettings.savePassword(getActivity(), passwordEditText.getText().toString());
            return true;
        }
        passwordEditText.setError(getString(R.string.invalid_password));
        return false;
    }

    private boolean isValidPassword() {
        String password = passwordEditText.getText().toString();
        return pattern.matcher(password).matches();
    }

    private boolean hasMinimumCharacters() {
        return passwordEditText.getText().length() >= MIN_CHARACTERS;
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
            actionButtonStateListener.onActionStateChanged(hasMinimumCharacters());
        }
    };
}
