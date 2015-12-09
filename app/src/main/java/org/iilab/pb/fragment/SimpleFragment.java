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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.HashMap;

import static org.iilab.pb.common.AppConstants.*;

/**
 * Created by aoe on 1/3/14.
 */
public class SimpleFragment extends Fragment {

    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
    NestedListView lvItems;
    NestedListView lvActions;
    LinearLayout llWarning, llStatus;
    Button bActionStopAlert;

    Page currentPage;
    PageItemAdapter pageItemAdapter;
    PageActionAdapter pageActionAdapter;
    boolean isPageStatusAvailable;
    private CheckBox powerTriggerCheckBox;
    private static final String TAG = SimpleFragment.class.getName();

    public static SimpleFragment newInstance(String pageId, int parentActivity) {
        SimpleFragment f = new SimpleFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_simple, container, false);
        Log.i(TAG, "onCreateView called");

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        llStatus = (LinearLayout) view.findViewById(R.id.ll_fragment_status);
        tvStatus = (TextView) view.findViewById(R.id.fragment_status);

        /*
        special case for page id = "home-alerting"
        This button will be visible only when we are in home-alerting page.
         */
        bActionStopAlert = (Button) view.findViewById(R.id.b_actionStopAlert);
        bActionStopAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "on click of \"Stop Alert\" button");
                stopAlert();
                String pageId = currentPage.getAction().get(0).getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;
                // This part needs to be changed.
                if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY || pageId.equalsIgnoreCase(PAGE_HOME_NOT_CONFIGURED)) {
                    Log.e(TAG, "This case should never be reached as parent activity can not be WizardActivity");
                    i = new Intent(activity, WizardActivity.class);
                    i = AppUtil.clearBackStack(i);
                } else {
                    i = new Intent(activity, MainActivity.class);
                }
                Log.d(TAG, "Target page-id is " + pageId);
                i.putExtra(PAGE_ID, pageId);
                startActivity(i);
                activity.finish();


            }
        });

        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageId = currentPage.getStatus().get(0).getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
                    i = new Intent(activity, WizardActivity.class);
                } else {
                    i = new Intent(activity, MainActivity.class);
                }
                Log.d(TAG, "Target page-id from status click "+pageId);
                i.putExtra(PAGE_ID, pageId);
                startActivity(i);
            }
        });

        lvItems = (NestedListView) view.findViewById(R.id.fragment_item_list);
        lvActions = (NestedListView) view.findViewById(R.id.fragment_action_list);

        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning = (TextView) view.findViewById(R.id.fragment_warning);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageItem selectedItem = (PageItem) parent.getItemAtPosition(position);
                String pageId = selectedItem.getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
                    i = new Intent(activity, WizardActivity.class);
                } else {
                    i = new Intent(activity, MainActivity.class);
                }
                Log.d(TAG, "Target page-id from itemList is "+pageId);
                i.putExtra(PAGE_ID, pageId);
                startActivity(i);
            }
        });
        powerTriggerCheckBox = (CheckBox) view.findViewById(R.id.powerTrigger);
        powerTriggerCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add code to disable service
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated called");

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);
            int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
            Log.d(TAG, "current page id is : "+pageId);
            Log.d(TAG, "Parent activity is : 1=wizard, 2 = Main "+ parentActivity);
            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if (currentPage.getStatus() == null || currentPage.getStatus().size() == 0) {
                isPageStatusAvailable = false;
                llStatus.setVisibility(View.GONE);
            } else {
                isPageStatusAvailable = true;
                String color = currentPage.getStatus().get(0).getColor();
                if (color.equals(COLOR_RED))
                    tvStatus.setTextColor(Color.RED);
                else
                    tvStatus.setTextColor(Color.GREEN);
                tvStatus.setText(currentPage.getStatus().get(0).getTitle());
            }

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else {
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
                tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());

            if (currentPage.getWarning() == null)
                llWarning.setVisibility(View.GONE);
            else
                tvWarning.setText(currentPage.getWarning());

            if (pageId.equals(PAGE_HOME_READY) || pageId.equals(PAGE_HOME_ALERTING)) {
                isPageStatusAvailable = false;
            }

            /**
             * if page status is "Home-Ready", we can enable the checkbox to disable the Power button Alarm
             */
            if (pageId.equals(PAGE_HOME_READY)){
                powerTriggerCheckBox.setVisibility(View.VISIBLE);
            }else{
                powerTriggerCheckBox.setVisibility(View.GONE);
            }
            pageActionAdapter = new PageActionAdapter(activity, null, isPageStatusAvailable, parentActivity);

            if (pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS) || pageId.equals(PAGE_SETUP_ALARM_TEST_DISGUISE_SUCCESS)) {
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
            } else if (pageId.equals(PAGE_HOME_ALERTING)) {
                lvActions.setVisibility(View.INVISIBLE);
                bActionStopAlert.setVisibility(View.VISIBLE);

                bActionStopAlert.setText(currentPage.getAction().get(0).getTitle());
            } else {
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
        Log.d(TAG, currentPage.getId());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, currentPage.getId());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, currentPage.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, currentPage.getId());
    }

}
