package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.common.GifDecoderView;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.trigger.HardwareTriggerReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.iilab.pb.common.AppConstants.ALARM_5_PRESS_PLUS_CONFIRMATION;
import static org.iilab.pb.common.AppConstants.ALARM_CUSTOM;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppUtil.getCustomTrainingToastMessage;
import static org.iilab.pb.common.AppUtil.vibrateForConfirmationOfAlertTriggered;
import static org.iilab.pb.common.ApplicationSettings.getSelectedLanguage;
import static org.iilab.pb.common.ApplicationSettings.getTriggerPattern;

/**
 * Created by aoe on 1/9/14.
 */
public class WizardAlarmTestHardwareFragment extends Fragment {

    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;
    PowerManager.WakeLock wakeLock;

    private GifDecoderView gifView;

    Page currentPage;
    private static final String TAG = SimpleFragment.class.getName();

    public static WizardAlarmTestHardwareFragment newInstance(String pageId) {
        WizardAlarmTestHardwareFragment f = new WizardAlarmTestHardwareFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_test_hardware, container, false);
        gifView = (GifDecoderView) view.findViewById(R.id.gif_view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            activity.registerReceiver(wizardHardwareReceiver, filter);

            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            if (currentPage.getContent() == null)
                gifView.setVisibility(View.GONE);
            else {
                Log.d("nixxx ", "" + getTriggerPattern(getActivity()));
                Log.d(TAG, "nixxx comming in else case " + getTriggerPattern(getActivity()));

                //check the trigger pattern and accordingly load image.7 repeated press set source is default picked from jsons
                // 5 press + confirmation set source to new gif(locale specific)
                //custom  gif view empty-- show static image with toast of no of clicks and confirmation
                String sourceStr = currentPage.getContent();

                if (ALARM_5_PRESS_PLUS_CONFIRMATION.equals(getTriggerPattern(getActivity()))) {
                    sourceStr = getString(R.string.resource5PressPlusConfirmation);
                }

                if (ALARM_CUSTOM.equals(getTriggerPattern(getActivity()))) {
                    sourceStr = getString(R.string.resourceCustomImage);
                    ((WizardActivity) getActivity()).customAlarmToastMessage(getCustomTrainingToastMessage(getActivity()));
                }
                Log.d(TAG, "the .gif image source name is " + sourceStr);
                Html.fromHtml(sourceStr, new Html.ImageGetter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Drawable getDrawable(final String source) {
                        try {

                            Drawable drawable = Drawable.createFromStream(activity.getAssets().open(source.substring(1, source.length())), null);
                            InputStream is = activity.getAssets().open(source.substring(1, source.length()));
                            gifView.playGif(is, metrics);

                            mImageCache.put(source, drawable);
                            return drawable;
                        } catch (IOException e) {
                            Log.e(TAG, "Failed to load gif image from asset");
                            e.printStackTrace();
                        }
                        return null;
                    }
                }, new MyTagHandler());

                Log.d(TAG, "content = " + currentPage.getContent());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gifView.stopGif();
        Log.d(TAG, "onPause WizardAlarmTestHardwareFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume WizardAlarmTestHardwareFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        activity.unregisterReceiver(wizardHardwareReceiver);
    }

    @Override
    public void onDestroyView() {
        gifView.clear();
        gifView = null;
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }


    private BroadcastReceiver wizardHardwareReceiver = new HardwareTriggerReceiver(activity) {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Log.d(TAG, "in onReceive in WizardAlarmTest");
            ((WizardActivity) getActivity()).hideToastMessageInInteractiveFragment();

            getActivity().onUserInteraction();

            if (multiClickEvent.canStartVibration()) {

                //confirmation message needs to be changed
                ((WizardActivity) getActivity()).confirmationPressToastMessage();
            }
        }

        @Override
        protected void onActivation(Context context) {
            Log.d(TAG, "in onActivation of wizardHWReceiver");
            //add vibration code in test fragments
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrateForConfirmationOfAlertTriggered(context);
//            vibrator.vibrate(ALERT_CONFIRMATION_VIBRATION_DURATION);

            String pageId = currentPage.getSuccessId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra(PAGE_ID, pageId);
            activity.startActivity(i);
            activity.finish();
        }


    };
}
