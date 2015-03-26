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
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.GifDecoderView;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.trigger.HardwareTriggerReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by aoe on 1/9/14.
 */
public class WizardAlarmTestHardwareFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;
    PowerManager.WakeLock wakeLock;

//    TextView tvContent;
    private GifDecoderView gifView;

    Page currentPage;

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

//        tvContent = (TextView) view.findViewById(R.id.fragment_contents);
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
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            if (currentPage.getContent() == null)
                gifView.setVisibility(View.GONE);
            else {
                Html.fromHtml(currentPage.getContent(), new Html.ImageGetter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Drawable getDrawable(final String source) {
                        try {
                            Log.e(">>>>>>>>>>>", "Source = " + source);
                            Drawable drawable = Drawable.createFromStream(activity.getAssets().open(source.substring(1, source.length())), null);

                            InputStream is = activity.getAssets().open(source.substring(1, source.length()));
                            gifView.playGif(is, metrics);

//                            drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, AppConstants.IMAGE_SCALABILITY_FACTOR * metrics.scaledDensity, imageScaleFlag);
                            mImageCache.put(source, drawable);
//                            updateImages(false, textHtml, context, metrics, tvContent, imageScaleFlag);
                            return drawable;
                        } catch (IOException e) {
                            Log.e(">>>>>>>>>>>>>>", "Failed to load gif image from asset");
                            e.printStackTrace();
                        }
                        return null;
                    }
                }, new MyTagHandler());

                Log.e(">>>>>", "content = " + currentPage.getContent());
//                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
//                AppUtil.updateImages(true, currentPage.getContent(), activity, metrics, tvContent, AppConstants.IMAGE_FULL_WIDTH);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        gifView.stopGif();
        Log.e(">>>>>", "onPause WizardAlarmTestHardwareFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume WizardAlarmTestHardwareFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("????", "onDestroy");
        activity.unregisterReceiver(wizardHardwareReceiver);
    }
    
    @Override
    public void onDestroyView() {
    	gifView.clear();
    	gifView = null;
        super.onDestroyView();
        Log.e("????", "onDestroyView");
    }


    private BroadcastReceiver wizardHardwareReceiver = new HardwareTriggerReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Log.e("????", "in onReceive in WizardAlarmTest");
            ((WizardActivity) getActivity()).hideToastMessageInInteractiveFragment();

            getActivity().onUserInteraction();

            if(multiClickEvent.canStartVibration()){
                ((WizardActivity) getActivity()).confirmationToastMessage();
            }
        }

        @Override
        protected void onActivation(Context context) {
            Log.e(">>>>>>>", "in onActivation of wizardHWReceiver");

            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(AppConstants.ALERT_CONFIRMATION_VIBRATION_DURATION);

            String pageId = currentPage.getSuccessId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra("page_id", pageId);
            activity.startActivity(i);
            activity.finish();
        }

    };
}
