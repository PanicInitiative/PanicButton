package org.iilab.pb.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AppUtil {

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

        Log.e("AppUtil -> setDownloadedImageMetrices", "density ratio = " + densityRatio + " and metrics =" + metrics);
		int width, height;
		int originalWidthScaled = (int) (drawable.getIntrinsicWidth()* metrics.density * densityRatio);
		int originalHeightScaled = (int) (drawable.getIntrinsicHeight()* metrics.density * densityRatio);
        Log.e("AppUtil -> setDownloadedImageMetrices", "originalWidthScaled = " + originalWidthScaled + " and originalHeightScaled = " + originalHeightScaled);

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
        Log.e("AppUtil -> setDownloadedImageMetrices", "final width = " + width + " and height = " + height);
		try {
			drawable.setBounds(0, 0, width, height);
			Log.e(">>>>>>>>>>>>>>", "image width = " + width + " & height = "+ height);
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
    
    /*-----This method show Toast.-----*/
	public static void showToast(String message, int time,Context context) {
		Toast.makeText(context, message, time).show();
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
                                Log.e(">>>>>>>>>>>", "Source = " + source);
                                Drawable drawable = Drawable.createFromStream(context.getAssets().open(source.substring(1, source.length())), null);

                                drawable = AppUtil.setDownloadedImageMetrices(drawable, metrics, AppConstants.IMAGE_SCALABILITY_FACTOR * metrics.scaledDensity, imageScaleFlag);
                                mImageCache.put(source, drawable);
                                updateImages(false, textHtml, context, metrics, tvContent, imageScaleFlag);
                                return drawable;
                            } catch (IOException e) {
                                Log.e(">>>>>>>>>>>>>>","Failed to load image from asset");
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


}
