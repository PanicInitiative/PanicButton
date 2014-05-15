package com.apb.beacon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.WizardActivity;
import com.apb.beacon.common.AppConstants;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;

import java.util.HashMap;

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

