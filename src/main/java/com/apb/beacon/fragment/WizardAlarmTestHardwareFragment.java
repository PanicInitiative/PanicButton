package com.apb.beacon.fragment;

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

import com.apb.beacon.common.AppConstants;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.WizardActivity;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.common.ImageDownloader;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.trigger.HardwareTriggerReceiver;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by aoe on 1/9/14.
 */
public class WizardAlarmTestHardwareFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    private Handler inactiveHandler = new Handler();
    private Handler failHandler = new Handler();

    DisplayMetrics metrics;

    TextView tvContent;

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

        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
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

            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
            failHandler.postDelayed(runnableFailed, Integer.parseInt(currentPage.getTimers().getFail()) * 1000);

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else {
                Log.e(">>>>>", "content = " + currentPage.getContent());
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
                updateImages(true, currentPage.getContent());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
        inactiveHandler.removeCallbacks(runnableInteractive);
        failHandler.removeCallbacks(runnableFailed);
        activity.unregisterReceiver(wizardHardwareReceiver);
    }

    private void updateImages(final boolean downloadImages, final String textHtml) {
        if (textHtml == null) return;
        Spanned spanned = Html.fromHtml(textHtml,
                new Html.ImageGetter() {
                    @SuppressWarnings("unchecked")
					@Override
                    public Drawable getDrawable(final String source) {
                        if(!AppUtil.hasInternet(activity) && downloadImages){
                            try {
                                Log.e(">>>>>>>>>>>", "Source = " + source);
                                Drawable drawable = Drawable.createFromStream(activity.getAssets().open(source.substring(1, source.length())), null);

                                /*int width, height;
                                int originalWidthScaled = (int) (drawable.getIntrinsicWidth() * metrics.density * 2.25);
                                int originalHeightScaled = (int) (drawable.getIntrinsicHeight() * metrics.density * 2.25);
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
                                }*/
                                
                              //densityRatio = 2.25 here because on top @KHOBAIB the values are 2.25 (line 147-148)
                                drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, 2.25);
                                mImageCache.put(source, drawable);
                                updateImages(false, textHtml);
                                return drawable;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;

                        }
                        Log.e(">>>>>>", "image src = " + source);
                        Drawable drawable = mImageCache.get(source);
                        if (drawable != null) {
                            return drawable;
                        } else if (downloadImages) {
                            new ImageDownloader(new ImageDownloader.ImageDownloadListener() {
                                @Override
                                public void onImageDownloadComplete(byte[] bitmapData) {
                                    try {
										Drawable drawable = new BitmapDrawable(getResources(),
										        BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));

										/*int width, height;
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
										}*/
										
//										TODO: @Khobaib please validate the Refactored code
										//densityRatio = 0.75 here because on top @KHOBAIB the values are 0.75 (line 360-361)
										drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, 0.75);
										mImageCache.put(source, drawable);
										updateImages(false, textHtml);
									} catch (Exception e) {
										e.printStackTrace();
										Log.e(">>>>>>>>>>>>>>","Failed to download image");
									}
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
            Log.e("????", "in onReceive in WizardAlarmTest");
            ((WizardActivity) getActivity()).hideToastMessageInInteractiveFragment();

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
