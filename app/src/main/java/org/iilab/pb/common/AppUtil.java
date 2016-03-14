package org.iilab.pb.common;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.iilab.pb.R;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_LONG;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_REPEATED_SHORT;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_SOS;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_THREESHORT_PAUSE_THREESHORT;
import static org.iilab.pb.common.AppConstants.ALERT_CONFIRMATION_VIBRATION_DURATION;
import static org.iilab.pb.common.AppConstants.APP_RELEASE_VERSION_1_5;
import static org.iilab.pb.common.AppConstants.ONE_SECOND;
import static org.iilab.pb.common.AppConstants.VIBRATION_DURATION_LONG;
import static org.iilab.pb.common.AppConstants.VIBRATION_DURATION_SHORT;
import static org.iilab.pb.common.AppConstants.VIBRATION_PAUSE_LONG;
import static org.iilab.pb.common.AppConstants.VIBRATION_PAUSE_SHORT;
import static org.iilab.pb.common.AppConstants.VIBRATION_PAUSE_VERY_LONG;
import static org.iilab.pb.common.ApplicationSettings.getDBLoadedLanguages;
import static org.iilab.pb.common.ApplicationSettings.getInitialClicksForAlertTrigger;
import static org.iilab.pb.common.ApplicationSettings.getLastUpdatedVersion;
import static org.iilab.pb.common.ApplicationSettings.isAlarmConfirmationRequired;
import static org.iilab.pb.common.ApplicationSettings.isFirstRun;
import static org.iilab.pb.common.ApplicationSettings.isTrainingDoneRelease1_5;

public class AppUtil {
    private static final String TAG = AppUtil.class.getName();
    public static HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();

    public static void setError(Context context, EditText editText, int messageId) {
        editText.setError(
                Html.fromHtml("<font color='red'>"
                        + context.getString(messageId)
                        + "</font>")
        );
    }

    public static void close(Context context) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        setDefaultPackage(context, startMain);

        context.startActivity(startMain);
    }

    private static void setDefaultPackage(Context context, Intent startMain) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(homeIntent, 0);
        if (activities.size() > 0) {
            String className = activities.get(0).activityInfo.packageName;
            startMain.setPackage(className);
        }
    }


//    public static boolean hasInternet(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null){
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null){
//                for (int i = 0; i < info.length; i++){
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public static String capitalizeString(String str){
        if(str == null || str.equals("") || str.length() == 0)
            return str;
        return (str.substring(0, 1).toUpperCase() + str.substring(1, str.length()));
    }


    private static Calendar clearTimes(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        return c;
    }


    public static Boolean isToday(long millis) {
        Calendar today = Calendar.getInstance();
        today = clearTimes(today);

        if(millis > today.getTimeInMillis())
            return true;
        return false;
    }
    
    
    //set display metrics of drawable for downloaded image
	public static Drawable setDownloadedImageMetrices(Drawable drawable,
			DisplayMetrics metrics, double densityRatio, int imageScaleFlag) {

        Log.d(TAG," setDownloadedImageMetrices"+ "density ratio = " + densityRatio + " and metrics =" + metrics);
		int width, height;
		int originalWidthScaled = (int) (drawable.getIntrinsicWidth()* metrics.density * densityRatio);
		int originalHeightScaled = (int) (drawable.getIntrinsicHeight() * metrics.density * densityRatio);
        Log.d(TAG," setDownloadedImageMetrices"+ "originalWidthScaled = " + originalWidthScaled + " and originalHeightScaled = " + originalHeightScaled);

        if (imageScaleFlag == AppConstants.IMAGE_FULL_WIDTH) {
            height = drawable.getIntrinsicHeight() * metrics.widthPixels / drawable.getIntrinsicWidth();
            width = metrics.widthPixels;
        } else {

            if (originalWidthScaled > metrics.widthPixels) {
                height = drawable.getIntrinsicHeight() * metrics.widthPixels / drawable.getIntrinsicWidth();
                width = metrics.widthPixels;
            } else {
                height = originalHeightScaled;
                width = originalWidthScaled;
            }
        }
        Log.e(TAG," setDownloadedImageMetrices"+ "final width = " + width + " and height = " + height);
		try {
			drawable.setBounds(0, 0, width, height);
			Log.e(TAG, "image width = " + width + " & height = "+ height);
		} catch (Exception ex) {
		}

		return drawable;
	}
	
    
    
    //remove all the views of previous activity to clear memory
    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
    
    
    //clear background activity
    public static Intent clearBackStack(Intent i){
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }
    
    //change the behave of device back button to device HomeButton
    public static Intent behaveAsHomeButton(){
		   Intent i = new Intent(Intent.ACTION_MAIN);
		   i.addCategory(Intent.CATEGORY_HOME);
		   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   return i;
    }
    
    public static void updateImages(final boolean downloadImages, final String textHtml, final Context context, final DisplayMetrics metrics, final TextView tvContent, final int imageScaleFlag) {

        if (textHtml == null) return;
        Spanned spanned = Html.fromHtml(textHtml,
                new Html.ImageGetter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Drawable getDrawable(final String source) {
                        if(
//                                !AppUtil.hasInternet(context) &&
                                        downloadImages){
                            try {
                                Log.d(TAG, "Source = " + source);
                                Drawable drawable = Drawable.createFromStream(context.getAssets().open(source.substring(1, source.length())), null);

                                drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, AppConstants.IMAGE_SCALABILITY_FACTOR * metrics.scaledDensity, imageScaleFlag);
                                mImageCache.put(source, drawable);
                                updateImages(false, textHtml, context, metrics, tvContent, imageScaleFlag);
                                return drawable;
                            } catch (IOException e) {
                                Log.e(TAG,"Failed to load image from asset");
                                e.printStackTrace();
                            }
                            return null;
                        }

//                        Log.e(">>>>>>", "image src = " + source);
//                        Drawable drawable = mImageCache.get(source);
//                        if (drawable != null) {
//                            return drawable;
//                        }
//                        else if (downloadImages) {
//                            new ImageDownloader(new ImageDownloader.ImageDownloadListener() {
//                                @Override
//                                public void onImageDownloadComplete(byte[] bitmapData) {
//                                    try {
//                                        Drawable drawable = new BitmapDrawable(context.getResources(),
//                                                BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));
//
//                                        drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, AppConstants.IMAGE_SCALABILITY_FACTOR, imageScaleFlag);
//                                        mImageCache.put(source, drawable);
//                                        updateImages(false, textHtml, context, metrics, tvContent, imageScaleFlag);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        Log.e(">>>>>>>>>>>>>>","Failed to download image");
//                                        try {
//                                            Drawable drawable = Drawable.createFromStream(context.getAssets().open(source.substring(1, source.length())), null);
//
//                                            drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, AppConstants.IMAGE_SCALABILITY_FACTOR * metrics.scaledDensity, imageScaleFlag);
//                                            mImageCache.put(source, drawable);
//                                            updateImages(false, textHtml, context, metrics, tvContent, imageScaleFlag);
//                                        } catch (IOException e1) {
//                                            Log.e(">>>>>>>>>>>>>>","Failed to load image from asset");
//                                            e1.printStackTrace();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onImageDownloadFailed(Exception ex) {
//                                    Log.e("onImageDownloadFailed", "ImageDownloadFailed");
//                                    try {
//                                        Drawable drawable = Drawable.createFromStream(context.getAssets().open(source.substring(1, source.length())), null);
//
//                                        drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, AppConstants.IMAGE_SCALABILITY_FACTOR * metrics.scaledDensity, imageScaleFlag);
//                                        mImageCache.put(source, drawable);
//                                        updateImages(false, textHtml, context, metrics, tvContent, imageScaleFlag);
//                                    } catch (IOException e1) {
//                                        Log.e(">>>>>>>>>>>>>>","Failed to load image from asset");
//                                        e1.printStackTrace();
//                                    }
//                                }
//                            }).execute(source);
//                        }
                        return null;
                    }
                }, new MyTagHandler());
        tvContent.setText(spanned);
    }

    public static void insertMobileDataToLocalDB(JSONArray dataArray,Context context) {
        Log.d(TAG,"inserting mobile data to local DB");
        List<Page> pageList = Page.parsePages(dataArray);
        PBDatabase dbInstance = new PBDatabase(context);
        dbInstance.open();

        for (int indexPage = 0; indexPage < pageList.size(); indexPage++) {
            dbInstance.insertOrUpdatePage(pageList.get(indexPage));
        }
        dbInstance.close();
    }

    public static String loadJSONFromAsset(String jsonFileName ,Context context) {
        Log.d(TAG,"loading JSON from asset");
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ioException) {
            Log.e(TAG, "Exception while loading json file" + ioException.getMessage());
            ioException.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean isLanguageDataExists(Context context, String lang){
        return (getDBLoadedLanguages(context).contains(lang));
    }


    public static void vibrateForConfirmationOfAlertTriggered(Context context) {
        String confirmationFeedbackPattern = ApplicationSettings.getConfirmationFeedbackVibrationPattern(context);

        Log.d(TAG, "confirmation feedback pattern 1-Long, 2-Repeated short, 3-Three short pause three short, 4-SOS, 5-None " + confirmationFeedbackPattern);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (ALARM_SENDING_CONFIRMATION_PATTERN_LONG.equals(confirmationFeedbackPattern)) {
            vibrateContinusly(vibrator, ALERT_CONFIRMATION_VIBRATION_DURATION);
        } else if (ALARM_SENDING_CONFIRMATION_PATTERN_REPEATED_SHORT.equals(confirmationFeedbackPattern)) {
            repeatedThreeShortVibrations(vibrator);
        } else if (ALARM_SENDING_CONFIRMATION_PATTERN_THREESHORT_PAUSE_THREESHORT.equals(confirmationFeedbackPattern)) {
            vibrateThreeShortPauseThreeShort(vibrator);
        } else if (ALARM_SENDING_CONFIRMATION_PATTERN_SOS.equals(confirmationFeedbackPattern)) {
            vibrateSOS(vibrator);
        }
    }
    private static void vibrateContinusly(Vibrator vibrator, int feedbackDuration) {
        vibrator.vibrate(feedbackDuration);
    }

    private static void vibrateEverySecond(Vibrator vibrator, int feedbackDuration) {
        Log.d(TAG, "vibrate every second pattern selected and feedback duration is " + feedbackDuration);

        vibrator.vibrate(getPattern(feedbackDuration), -1);
    }

    private static long[] getPattern(int i) {
        switch (i) {
            case 1:
                // pattern {0, 600, 400};
                return new long[]{0, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG};
            case 2:
                // pattern {0, 600, 400,600, 400};
                return new long[]{0, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG};

            case 3:
                // pattern {0, 600, 400,600, 400,600, 400}
                return new long[]{0, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG};
            case 4:
                // pattern {0, 600, 400,600, 400,600, 400,600, 400}
                return new long[]{0, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG};
        }
        return new long[]{0, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG};
    }

    private static void repeatedThreeShortVibrations(Vibrator vibrator) {
        // pattern = {0, 400, 200,400, 200,400};
        long[] pattern = {0, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT};
        vibrator.vibrate(pattern, -1);
    }

    private static void vibrateThreeShortPauseThreeShort(Vibrator vibrator) {
        //pattern = {0, 400,200,400,200,400,1000,400,200,400,200,400};
        long[] pattern = {0, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_VERY_LONG, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT};
        vibrator.vibrate(pattern, -1);

    }

    // SOS: Three short - Three long - Three short
    private static void vibrateSOS(Vibrator vibrator) {
        //pattern = {0, 400,200,400,200,400,1000,400,1000,400,1000,400, 400,200,400,200,400,200};
        long[] pattern = {0, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_VERY_LONG,//three short
                VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG, VIBRATION_DURATION_LONG, VIBRATION_PAUSE_LONG,// Three long
                VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT, VIBRATION_PAUSE_SHORT, VIBRATION_DURATION_SHORT};//three short
        vibrator.vibrate(pattern, -1);
    }

    public static void vibrateForHapticFeedback(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        Log.d(TAG, "haptic feedback pattern 1-continues, 2-vibrate every second : " + ApplicationSettings.getHapticFeedbackVibrationPattern(context));
        //Code to fetch the Haptic feedback pattern
        if (context.getString(R.string.hapticFeedbackDefaultPattern).equals(ApplicationSettings.getHapticFeedbackVibrationPattern(context))) {
            vibrateContinusly(vibrator, (ONE_SECOND * Integer.parseInt(ApplicationSettings.getConfirmationWaitVibrationDuration(context))));
        } else {
            vibrateEverySecond(vibrator, Integer.parseInt(ApplicationSettings.getConfirmationWaitVibrationDuration(context)));
        }
    }

    public static boolean playTrainingForRelease1_5(Context context) {
        Log.d(TAG, "is App a fresh install " + isFirstRun(context));
        Log.d(TAG, "is 1.5 update training played once "+ isTrainingDoneRelease1_5(context));
        Log.d(TAG, "the version no of app installed in device. 10 corresponds to 1.5 release"+ getLastUpdatedVersion(context));
        if (getLastUpdatedVersion(context) == APP_RELEASE_VERSION_1_5 && !isTrainingDoneRelease1_5(context) && !isFirstRun(context)) {
            return true;
        }
        return false;
    }

    public static String getCustomTrainingToastMessage(Context context){
        String confirmationString="";
        if(isAlarmConfirmationRequired(context))
            confirmationString=context.getString(R.string.confirmationRequired);
        return getInitialClicksForAlertTrigger(context)+" " +context.getString(R.string.InitialPressesText) +" "+ confirmationString;

    }
}
