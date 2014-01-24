package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apb.beacon.AppConstants;
import com.apb.beacon.R;
import com.apb.beacon.common.ImageDownloader;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.trigger.HardwareTriggerReceiver;

import java.util.HashMap;

/**
 * Created by aoe on 1/9/14.
 */
public class AlarmTestHardwareFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    private Handler inactiveHandler = new Handler();
    private Handler failHandler = new Handler();

    DisplayMetrics metrics;

    TextView tvContent;

    Page currentPage;
//    PageItemAdapter pageItemAdapter;
//    PageActionAdapter pageActionAdapter;

    public static AlarmTestHardwareFragment newInstance(String pageId) {
        AlarmTestHardwareFragment f = new AlarmTestHardwareFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_interactive_test_hardware, container, false);

        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            activity.registerReceiver(wizardHardwareReceiver, filter);

            String pageId = getArguments().getString(PAGE_ID);
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
            failHandler.postDelayed(runnableFailed, Integer.parseInt(currentPage.getTimers().getFail()) * 1000);

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else {
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
                updateImages(true, currentPage.getContent());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("????", "onDestroy");
        inactiveHandler.removeCallbacks(runnableInteractive);
        failHandler.removeCallbacks(runnableFailed);
        activity.unregisterReceiver(wizardHardwareReceiver);
    }

    private void updateImages(final boolean downloadImages, final String textHtml) {
        if (textHtml == null) return;
        Spanned spanned = Html.fromHtml(textHtml,
                new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(final String source) {
                        Log.e(">>>>>>", "image src = " + source);
//                        if(!source.startsWith("http")){
//                            source = "http://teampanicbutton.github.io/" + source;
//                        }
                        Drawable drawable = mImageCache.get(source);
                        if (drawable != null) {
                            return drawable;
                        } else if (downloadImages) {
                            new ImageDownloader(new ImageDownloader.ImageDownloadListener() {
                                @Override
                                public void onImageDownloadComplete(byte[] bitmapData) {
                                    Drawable drawable = new BitmapDrawable(getResources(),
                                            BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));

                                    int width, height;
                                    int originalWidthScaled = (int) (drawable.getIntrinsicWidth() * metrics.density * 0.75);
                                    int originalHeightScaled = (int) (drawable.getIntrinsicHeight() * metrics.density * 0.75);
                                    if (originalWidthScaled > metrics.widthPixels) {
                                        height = drawable.getIntrinsicHeight() * metrics.widthPixels / drawable.getIntrinsicWidth();
                                        width = metrics.widthPixels;
                                    } else {
                                        height = originalHeightScaled;
                                        width = originalWidthScaled;
                                    }
                                    try {
                                        drawable.setBounds(0, 0, width, height);
                                        Log.e(">>>>>>>>>>>>>>", "image width = " + width + " & height = " + height);
                                    } catch (Exception ex) {
                                    }
                                    mImageCache.put(source, drawable);
                                    updateImages(false, textHtml);
                                }

                                @Override
                                public void onImageDownloadFailed(Exception ex) {
                                }
                            }).execute(source);
                        }
                        return null;
                    }
                }, new MyTagHandler());
        tvContent.setText(spanned);
    }


    private BroadcastReceiver wizardHardwareReceiver = new HardwareTriggerReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Log.e("????", "onReceive in subclass also");
            inactiveHandler.removeCallbacks(runnableInteractive);
            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
        }

        @Override
        protected void onActivation(Context context) {
            Log.e(">>>>>>>", "in onActivation of wizardHWReceiver");
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(AppConstants.HAPTIC_FEEDBACK_DURATION);

            inactiveHandler.removeCallbacks(runnableInteractive);
            failHandler.removeCallbacks(runnableFailed);

            String pageId = currentPage.getSuccessId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra("page_id", pageId);
            activity.startActivity(i);
            activity.finish();
        }

    };


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
