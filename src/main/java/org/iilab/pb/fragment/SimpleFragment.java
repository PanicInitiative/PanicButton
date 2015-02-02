package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.HashMap;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.adapter.PageActionAdapter;
import org.iilab.pb.adapter.PageActionFakeAdapter;
import org.iilab.pb.adapter.PageItemAdapter;
import org.iilab.pb.alert.PanicAlert;
import org.iilab.pb.alert.PanicMessage;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.common.NestedListView;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.model.PageItem;


/**
 * Created by aoe on 1/3/14.
 */
public class SimpleFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private static final String PARENT_ACTIVITY = "parent_activity";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
    NestedListView lvItems;
    NestedListView lvActions;
    LinearLayout llWarning, llStatus;
    Button bAction;

    Page currentPage;
    PageItemAdapter pageItemAdapter;
    PageActionAdapter pageActionAdapter;
    boolean isPageStatusAvailable;

    public static SimpleFragment newInstance(String pageId, int parentActivity) {
        SimpleFragment f = new SimpleFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return(f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_simple, container, false);
        Log.e(">>>>>", "SimpleFragment.onCreateView");

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        llStatus = (LinearLayout) view.findViewById(R.id.ll_fragment_status);
        tvStatus = (TextView) view.findViewById(R.id.fragment_status);

        /*
        special case for page id = "home-alerting"
        This button will be visible only when we are in home-alerting page.
         */
        bAction = (Button) view.findViewById(R.id.b_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlert();
                String pageId = currentPage.getAction().get(0).getLink();

                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                // This part needs to be changed.
                if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY || pageId.equalsIgnoreCase("home-not-configured")){
                    i = new Intent(activity, WizardActivity.class);
                    i = AppUtil.clearBackStack(i);
                } else{
                    i = new Intent(activity, MainActivity.class);
                }

//                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
                activity.finish();


            }
        });

        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppConstants.IS_ACTION_ITEM_PRESSED = true;

                String pageId = currentPage.getStatus().get(0).getLink();
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

        lvItems = (NestedListView) view.findViewById(R.id.fragment_item_list);
        lvActions = (NestedListView) view.findViewById(R.id.fragment_action_list);

        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) view.findViewById(R.id.fragment_warning);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AppConstants.IS_ACTION_ITEM_PRESSED = true;

                PageItem selectedItem = (PageItem) parent.getItemAtPosition(position);

//                AppConstants.PAGE_FROM_NOT_IMPLEMENTED = true;

                String pageId = selectedItem.getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
                    i = new Intent(activity, WizardActivity.class);
                } else{
                    i = new Intent(activity, MainActivity.class);
                }
                i.putExtra("page_id", pageId);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(">>>>>", "SimpleFragment.onActivityCreated");

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);
            int parentActivity = getArguments().getInt(PARENT_ACTIVITY);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if(currentPage.getStatus() == null || currentPage.getStatus().size() == 0){
                isPageStatusAvailable = false;
                llStatus.setVisibility(View.GONE);
            } else{
                isPageStatusAvailable = true;
                String color = currentPage.getStatus().get(0).getColor();
                if(color.equals("red"))
                    tvStatus.setTextColor(Color.RED);
                else
                    tvStatus.setTextColor(Color.GREEN);
                tvStatus.setText(currentPage.getStatus().get(0).getTitle());
            }

            if(currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else {
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
                tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if(currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());

            if(currentPage.getWarning() == null)
                llWarning.setVisibility(View.GONE);
            else
                tvWarning.setText(currentPage.getWarning());

            if(pageId.equals("home-ready") || pageId.equals("home-alerting")){
                isPageStatusAvailable = false;
            }

            pageActionAdapter = new PageActionAdapter(activity, null, isPageStatusAvailable, parentActivity);

            if (pageId.equals("setup-alarm-test-hardware-success") || pageId.equals("setup-alarm-test-disguise-success")) {
                PageActionFakeAdapter pageActionFakeAdapter = new PageActionFakeAdapter(activity, null);
                lvActions.setAdapter(pageActionFakeAdapter);
                pageActionFakeAdapter.setData(currentPage.getAction());

                Handler actionHandler = new Handler();
                actionHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvActions.setAdapter(pageActionAdapter);
                        pageActionAdapter.setData(currentPage.getAction());
                    }
                }, 1000);
            }
            else if(pageId.equals("home-alerting")){
                lvActions.setVisibility(View.INVISIBLE);
                bAction.setVisibility(View.VISIBLE);

                bAction.setText(currentPage.getAction().get(0).getTitle());
            }
            else {
                lvActions.setAdapter(pageActionAdapter);
                pageActionAdapter.setData(currentPage.getAction());
            }


            pageItemAdapter = new PageItemAdapter(activity, null);
            lvItems.setAdapter(pageItemAdapter);

            pageItemAdapter.setData(currentPage.getItems());

            tvTitle.setFocusableInTouchMode(true);
            tvTitle.requestFocus();

            AppUtil.updateImages(true, currentPage.getContent(), activity, metrics, tvContent, AppConstants.IMAGE_INLINE);
        }
    }

    private void stopAlert() {
        PanicAlert panicAlert = getPanicAlert();
        panicAlert.deActivate();

        new PanicMessage(activity.getApplicationContext()).sendStopAlertMessage();
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(activity);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("SimpleFragment.onPause", currentPage.getId());
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e("SimpleFragment.onStop", currentPage.getId());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("SimpleFragment.onStart", currentPage.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SimpleFragment.onResume", currentPage.getId());
    }

}
