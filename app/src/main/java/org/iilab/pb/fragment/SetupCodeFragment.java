package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.MessageTextView;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

import static org.iilab.pb.common.AppConstants.DEFAULT_CONFIRMATION_MESSAGE;
import static org.iilab.pb.common.AppConstants.FROM_MAIN_ACTIVITY;
import static org.iilab.pb.common.AppConstants.FROM_WIZARD_ACTIVITY;
import static org.iilab.pb.common.AppConstants.IMAGE_INLINE;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;
public class SetupCodeFragment extends Fragment {
    private static final int ALLOWED_LENGTH_FOR_CODE = 4;
    private EditText passwordEditText,confirmPasswordEditText;
    private Activity activity;
    DisplayMetrics metrics;
    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    LinearLayout llWarning;
    Page currentPage;
    private MessageTextView codeHeaderView;
    private static final String TAG = SetupCodeFragment.class.getName();
    public static SetupCodeFragment newInstance(String pageId, int parentActivity) {
        SetupCodeFragment f = new SetupCodeFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return(f);
    }

    private boolean onEnterKeyPress(int keyCode, KeyEvent event) {
        // If the event is a key-down event on the "enter" button
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
            Log.d(TAG, "key Enter Key Pressed");
            onCodeSaving();
        }
            return false;
    }

    private void onCodeSaving() {
        Log.i(TAG, "Perform action on Enter key press or on press of Save Button");
        // Perform action on key press
        String password = passwordEditText.getText().toString();
        String confirmPwd = confirmPasswordEditText.getText().toString();
        if ((password != null) && password.equals(confirmPwd) && isPINLengthVerified()) {
            Log.d(TAG, "Code  matched and verified length ");
            ApplicationSettings.savePassword(getActivity(), passwordEditText.getText().toString());
            String pageId = currentPage.getAction().get(0).getLink();
            int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
            Intent i;

            if (parentActivity == FROM_WIZARD_ACTIVITY) {
                i = new Intent(activity, WizardActivity.class);
            } else {
                String confirmation = (currentPage.getAction().get(0).getConfirmation() == null)
                        ? DEFAULT_CONFIRMATION_MESSAGE
                        : currentPage.getAction().get(0).getConfirmation();
                Toast.makeText(activity, confirmation, Toast.LENGTH_SHORT).show();

                i = new Intent(activity, MainActivity.class);
            }
            i.putExtra(PAGE_ID, pageId);
            startActivity(i);

            if (parentActivity == FROM_MAIN_ACTIVITY) {
                activity.finish();
            }
        } else {
            Log.d(TAG, "Code not matched ");
            AppUtil.setError(activity, confirmPasswordEditText, R.string.incorrect_pin);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_code, container, false);
        codeHeaderView = (MessageTextView) view.findViewById(R.id.confirm_pin_header);
        passwordEditText = (EditText) view.findViewById(R.id.create_pin_edittext);
        confirmPasswordEditText=(EditText)view.findViewById(R.id.confirm_pin_edittext);
        passwordEditText.addTextChangedListener(passwordTextChangeListener);
        confirmPasswordEditText.addTextChangedListener(passwordTextChangeListener);
        passwordEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "key Pressed event for passwordEditText called ");
                return onEnterKeyPress(keyCode, event);
            }
        });
        passwordEditText.requestFocus();

        confirmPasswordEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "key Pressed event for confirmPasswordEditText called ");
                return onEnterKeyPress(keyCode, event);
            }
        });
        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "action button pressed");
                onCodeSaving();
            }
        });
        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) view.findViewById(R.id.fragment_warning);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            bAction.setEnabled(isPINLengthVerified());

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
            codeHeaderView.setText(codeHeaderView.getMessageHeader());
            AppUtil.updateImages(true, currentPage.getContent(), activity, metrics, tvContent, IMAGE_INLINE);
        }
    }

    private boolean isPINLengthVerified() {
        return ((passwordEditText.getText().length() == ALLOWED_LENGTH_FOR_CODE) &&
                confirmPasswordEditText.getText().length() == ALLOWED_LENGTH_FOR_CODE);
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
            bAction.setEnabled(isPINLengthVerified());
        }
    };
}
