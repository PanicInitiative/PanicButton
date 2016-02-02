package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.adapter.PageItemAdapter;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.model.PageItem;
import org.iilab.pb.model.SMSSettings;

import static org.iilab.pb.common.AppConstants.DEFAULT_CONFIRMATION_MESSAGE;
import static org.iilab.pb.common.AppConstants.FROM_MAIN_ACTIVITY;
import static org.iilab.pb.common.AppConstants.FROM_WIZARD_ACTIVITY;
import static org.iilab.pb.common.AppConstants.IMAGE_INLINE;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;
import static org.iilab.pb.common.AppUtil.updateImages;
import static org.iilab.pb.common.ApplicationSettings.getSelectedLanguage;


/**
 * Created by aoe on 12/12/13.
 */
public class SetupMessageFragment extends Fragment {

    private static final String TAG = SetupMessageFragment.class.getName();
    private Activity activity;
    DisplayMetrics metrics;
    int parentActivity;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    ListView lvItems;
    LinearLayout llWarning;
    Page currentPage;
    PageItemAdapter pageItemAdapter;

    public static SetupMessageFragment newInstance(String pageId, int parentActivity) {
        SetupMessageFragment f = new SetupMessageFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_type_interactive_message, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);
        parentActivity = getArguments().getInt(PARENT_ACTIVITY);
        MessageTextFragment childFragment = (MessageTextFragment) getChildFragmentManager().findFragmentById(R.id.sms_message);
        if (childFragment == null) {
            childFragment = new MessageTextFragment();
            Bundle args = new Bundle();
            args.putInt(PARENT_ACTIVITY, parentActivity);
            childFragment.setArguments(args);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.sms_message, childFragment);
            transaction.commit();
        }

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "action button pressed");

                MessageTextFragment childFragment = (MessageTextFragment) getChildFragmentManager().findFragmentById(R.id.sms_message);
                String emergencyMessage = childFragment.getEmergencyMsgFromView();
                SMSSettings.saveMessage(activity, emergencyMessage);
                childFragment.displayEmergencyMsg(emergencyMessage);
                String stopAlertMessage=getString(R.string.stop_alert_message);
                if (parentActivity == FROM_MAIN_ACTIVITY) {
                    stopAlertMessage=childFragment.getStopAlertMsgFromView();
                    Log.d(TAG, "stop alert message is " + stopAlertMessage);
                    childFragment.displayStopAlertMsg(stopAlertMessage);
                }
                SMSSettings.saveStopAlertMessage(activity, stopAlertMessage);
                String pageId = currentPage.getAction().get(0).getLink();
//                 parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if (parentActivity == FROM_WIZARD_ACTIVITY) {
                    i = new Intent(activity, WizardActivity.class);
                } else {
//                	showToast("Message saved.", 1000, activity);
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
            }
        });
        lvItems = (ListView) view.findViewById(R.id.fragment_item_list);

        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning = (TextView) view.findViewById(R.id.fragment_warning);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageItem selectedItem = (PageItem) parent.getItemAtPosition(position);

                String pageId = selectedItem.getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if (parentActivity == FROM_WIZARD_ACTIVITY) {
                    i = new Intent(activity, WizardActivity.class);
                } else {
                    i = new Intent(activity, MainActivity.class);
                }
                i.putExtra(PAGE_ID, pageId);
                startActivity(i);

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
            String selectedLang = getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));

            if (currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());

            if (currentPage.getWarning() == null)
                llWarning.setVisibility(View.GONE);
            else
                tvWarning.setText(currentPage.getWarning());

            bAction.setText(currentPage.getAction().get(0).getTitle());

            pageItemAdapter = new PageItemAdapter(activity, null);
            lvItems.setAdapter(pageItemAdapter);
            pageItemAdapter.setData(currentPage.getItems());

            updateImages(true, currentPage.getContent(), activity, metrics, tvContent, IMAGE_INLINE);

        }
    }

}
