package org.iilab.pb.fragment;

import android.os.Debug;
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
        View view = inflater.inflate(R.layout.calculator_layout, container, false);
        registerButtonEvents(view);
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


    private void registerButtonEvents(View view) {
        for (int buttonId : buttonIds) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnClickListener(clickListener);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            MultiClickEvent multiClickEvent = (MultiClickEvent) view.getTag();
            if (multiClickEvent == null) {
                multiClickEvent = resetEvent(view);
            }

            if (id != lastClickId) multiClickEvent.reset();
            lastClickId = id;
            multiClickEvent.registerClick(System.currentTimeMillis());
            if(multiClickEvent.canStartVibration()) {
                int vibratingDuration = AppConstants.HAPTIC_FEEDBACK_DURATION;
                Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(vibratingDuration);

                CharSequence text = ((Button) view).getText();
                Toast.makeText(activity, "Quickly press the button '" + text + "' again to send alerts.", Toast.LENGTH_LONG).show();
            }
            else if(multiClickEvent.isActivated()){
                resetEvent(view);

                String pageId = currentPage.getSuccessId();

                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                activity.startActivity(i);
                activity.finish();
            }
        }
    };

    private MultiClickEvent resetEvent(View view) {
        MultiClickEvent multiClickEvent = new MultiClickEvent();
        view.setTag(multiClickEvent);
        return multiClickEvent;
    }
}
