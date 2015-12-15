package org.iilab.pb.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.iilab.pb.R;
import org.iilab.pb.model.Page;

import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;

public class AdvancedSettingsFragment extends PreferenceFragmentCompat {

    private Activity activity;
    DisplayMetrics metrics;
    int parentActivity;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    LinearLayout llWarning;
    Page currentPage;
    private static final String TAG = AdvancedSettingsFragment.class.getName();

    public static AdvancedSettingsFragment newInstance(String pageId, int parentActivity) {
        AdvancedSettingsFragment f = new AdvancedSettingsFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
//        addPreferencesFromResource(R.xml.preferences);
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_type_interactive_advanced_settings, container, false);
//
//        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
//        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
//        tvContent = (TextView) view.findViewById(R.id.fragment_contents);
//        parentActivity = getArguments().getInt(PARENT_ACTIVITY);
////        MessageTextFragment childFragment = (MessageTextFragment) getChildFragmentManager().findFragmentById(R.id.sms_message);
////        if (childFragment == null) {
////            childFragment = new MessageTextFragment();
////            Bundle args = new Bundle();
////            args.putInt(AppConstants.PARENT_ACTIVITY, parentActivity);
////            childFragment.setArguments(args);
////            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
////            transaction.replace(R.id.sms_message, childFragment);
////            transaction.commit();
////        }
//
//        bAction = (Button) view.findViewById(R.id.fragment_action);
//        bAction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "action button pressed");
//
////                MessageTextFragment childFragment = (MessageTextFragment) getChildFragmentManager().findFragmentById(R.id.sms_message);
////                String emergencyMessage = childFragment.getEmergencyMsgFromView();
////                SMSSettings.saveMessage(activity, emergencyMessage);
////                childFragment.displayEmergencyMsg(emergencyMessage);
////                if (parentActivity == AppConstants.FROM_MAIN_ACTIVITY) {
////                    String stopAlertMessage=childFragment.getStopAlertMsgFromView();
////                    SMSSettings.saveStopAlertMessage(activity, stopAlertMessage);
////                    childFragment.displayStopAlertMsg(stopAlertMessage);
////                }
//
//                String pageId = currentPage.getAction().get(0).getLink();
//                Intent i;
//
//                if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
//                    i = new Intent(activity, WizardActivity.class);
//                } else {
////                	AppUtil.showToast("Message saved.", 1000, activity);
//                    String confirmation = (currentPage.getAction().get(0).getConfirmation() == null)
//                            ? AppConstants.DEFAULT_CONFIRMATION_MESSAGE
//                            : currentPage.getAction().get(0).getConfirmation();
//                    Toast.makeText(activity, confirmation, Toast.LENGTH_SHORT).show();
//
//                    i = new Intent(activity, MainActivity.class);
//                }
//                i.putExtra(PAGE_ID, pageId);
//                startActivity(i);
//
//                if (parentActivity == AppConstants.FROM_MAIN_ACTIVITY) {
//                    activity.finish();
//                }
//            }
//        });
//
//        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
//        tvWarning = (TextView) view.findViewById(R.id.fragment_warning);
//
//        return view;
//    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        activity = getActivity();
//        if (activity != null) {
//            metrics = new DisplayMetrics();
//            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//            String pageId = getArguments().getString(PAGE_ID);
//            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);
//
//            PBDatabase dbInstance = new PBDatabase(activity);
//            dbInstance.open();
//            currentPage = dbInstance.retrievePage(pageId, selectedLang);
//            dbInstance.close();
//
//            tvTitle.setText(currentPage.getTitle());
//
//            if (currentPage.getContent() == null)
//                tvContent.setVisibility(View.GONE);
//            else
//                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
//
//            if (currentPage.getIntroduction() == null)
//                tvIntro.setVisibility(View.GONE);
//            else
//                tvIntro.setText(currentPage.getIntroduction());
//
//            if (currentPage.getWarning() == null)
//                llWarning.setVisibility(View.GONE);
//            else
//                tvWarning.setText(currentPage.getWarning());
//
//            bAction.setText(currentPage.getAction().get(0).getTitle());
//
//
//            AppUtil.updateImages(true, currentPage.getContent(), activity, metrics, tvContent, AppConstants.IMAGE_INLINE);
//
//        }
//    }

}
