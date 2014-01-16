package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.apb.beacon.AppConstants;
import com.apb.beacon.R;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.trigger.MultiClickEvent;

/**
 * Created by aoe on 1/16/14.
 */
public class AlarmTestDisguiseFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    private int[] buttonIds = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply, R.id.divide};

    private Handler inactiveHandler = new Handler();
    private Handler failHandler = new Handler();

    private int lastClickId = -1;

    Page currentPage;

    public static AlarmTestDisguiseFragment newInstance(String pageId) {
        AlarmTestDisguiseFragment f = new AlarmTestDisguiseFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
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
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
            failHandler.postDelayed(runnableFailed, Integer.parseInt(currentPage.getTimers().getFail()) * 1000);

            if(currentPage.getIntroduction() != null){
                Toast.makeText(activity, Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()), Toast.LENGTH_LONG).show();
            }
        }
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

            inactiveHandler.removeCallbacks(runnableInteractive);
            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);

            MultiClickEvent multiClickEvent = (MultiClickEvent) view.getTag();
            if (multiClickEvent == null) {
                multiClickEvent = resetEvent(view);
            }

            if(id != lastClickId) multiClickEvent.reset();
            lastClickId = id;
            multiClickEvent.registerClick(System.currentTimeMillis());
            if (multiClickEvent.isActivated()) {
                Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(AppConstants.HAPTIC_FEEDBACK_DURATION);
                resetEvent(view);

                inactiveHandler.removeCallbacks(runnableInteractive);
                failHandler.removeCallbacks(runnableFailed);

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


    private Runnable runnableInteractive = new Runnable() {
        public void run() {

            failHandler.removeCallbacks(runnableFailed);

            String pageId = currentPage.getFailedId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra("page_id", pageId);
            activity.startActivity(i);
            activity.finish();
        }
    };


    private Runnable runnableFailed = new Runnable() {
        public void run() {

            inactiveHandler.removeCallbacks(runnableInteractive);

            String pageId = currentPage.getFailedId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra("page_id", pageId);
            activity.startActivity(i);
            activity.finish();
        }
    };
}
