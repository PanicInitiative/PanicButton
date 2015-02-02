package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.HashMap;

import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

/**
 * Created by aoe on 1/18/14.
 */
public class WizardTestDisguiseCodeFragment extends Fragment {

    private EditText passwordEditText;
    private TextView tvContent;
    private Button bAction;

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    Page currentPage;

    public static WizardTestDisguiseCodeFragment newInstance(String pageId) {
        WizardTestDisguiseCodeFragment f = new WizardTestDisguiseCodeFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_disguise_test_code, container, false);

        passwordEditText = (EditText) view.findViewById(R.id.create_pin_edittext);
        passwordEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                      // Perform action on key press

	                String password = passwordEditText.getText().toString();
	                if (ApplicationSettings.passwordMatches(activity, password)) {
	
	                    String pageId = null;
	                    if (currentPage.getAction().size() > 0)
	                        pageId = currentPage.getAction().get(0).getLink();
	                    else
	                        pageId = currentPage.getSuccessId();
	
	                    Intent i = new Intent(activity, WizardActivity.class);
	                    i.putExtra("page_id", pageId);
	                    activity.startActivity(i);
	                    activity.finish();
	                    return true;
	                }
	                AppUtil.setError(activity, passwordEditText, R.string.incorrect_pin);
                }
                return false;
            }
        });
        passwordEditText.requestFocus();

        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.b_action);
        bAction.setText("Go");
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                if (ApplicationSettings.passwordMatches(activity, password)) {

                    String pageId = null;
                    if (currentPage.getAction().size() > 0)
                        pageId = currentPage.getAction().get(0).getLink();
                    else
                        pageId = currentPage.getSuccessId();

                    Intent i = new Intent(activity, WizardActivity.class);
                    i.putExtra("page_id", pageId);
                    activity.startActivity(i);
                    activity.finish();
                    return;
                }
                AppUtil.setError(activity, passwordEditText, R.string.incorrect_pin);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            bAction.setText(currentPage.getAction().get(0).getTitle());

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else {
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
                AppUtil.updateImages(true, currentPage.getContent(), activity, metrics, tvContent, AppConstants.IMAGE_INLINE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause WizardTestDisguiseCodeFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume WizardTestDisguiseCodeFragment");
    }
}

