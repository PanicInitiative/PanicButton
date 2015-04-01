package org.iilab.pb.fragment;

import android.widget.Toast;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.trigger.MultiClickEvent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by aoe on 1/16/14.
 */
public class WizardAlarmTestDisguiseFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    private int[] buttonIds = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply, R.id.divide, R.id.decimal_point, R.id.char_c};

    private int lastClickId = -1;

    Page currentPage;

    public static WizardAlarmTestDisguiseFragment newInstance(String pageId) {
        WizardAlarmTestDisguiseFragment f = new WizardAlarmTestDisguiseFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(">>>>>", "onCreateView before inflate");
        View view = inflater.inflate(R.layout.calculator_layout, container, false);
        Log.e(">>>>>", "onCreateView before registerButtonEvents");
        registerButtonEvents(view);
        Log.e(">>>>>", "onCreateView after registerButtonEvents");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause WizardAlarmTestDisguiseFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume WizardAlarmTestDisguiseFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(">>>>>", "onDestroyView WizardAlarmTestDisguiseFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(">>>>>", "onDestroy WizardAlarmTestDisguiseFragment");
    }


    private void registerButtonEvents(View view) {
        for (int buttonId : buttonIds) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnClickListener(clickListener);
        }
    }

    private void unregisterButtonEvents(Activity activity) {
        for (int buttonId : buttonIds) {
            Button button = (Button) activity.findViewById(buttonId);
            button.setOnClickListener(null);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
        	Log.e(">>>>>", "onClick id " + id);
        	Log.e(">>>>>", "onClick lastClickId " + lastClickId);

            MultiClickEvent multiClickEvent = (MultiClickEvent) view.getTag();
            if (multiClickEvent == null) {
            	Log.e(">>>>>", "multiClickEvent reset");
                multiClickEvent = resetEvent(view);
            }

            if (id != lastClickId) multiClickEvent.reset();
            lastClickId = id;
            multiClickEvent.registerClick(System.currentTimeMillis());

            if(multiClickEvent.skipCurrentClick()){
            	Log.e(">>>>>", "multiClickEvent skip");
                multiClickEvent.resetSkipCurrentClickFlag();
                return;
            }
            if(multiClickEvent.canStartVibration()) {
                vibrate(AppConstants.HAPTIC_FEEDBACK_DURATION);

                CharSequence text = ((Button) view).getText();
                Toast.makeText(activity, "Press the button '" + text + "' once the vibration ends to trigger alerts", Toast.LENGTH_LONG).show();
            }
            else if(multiClickEvent.isActivated()){
            	Log.e(">>>>>", "multiClickEvent isActivated");
                vibrate(AppConstants.ALERT_CONFIRMATION_VIBRATION_DURATION);

                resetEvent(view);
                //unregisterButtonEvents(activity);

                String pageId = currentPage.getSuccessId();
                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                activity.startActivity(i);
                activity.finish();
            }
        }
    };

    private void vibrate(int vibrationDuration) {
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationDuration);
    }

    private MultiClickEvent resetEvent(View view) {
        MultiClickEvent multiClickEvent = new MultiClickEvent();
        view.setTag(multiClickEvent);
        return multiClickEvent;
    }
}
