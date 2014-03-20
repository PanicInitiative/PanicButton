package com.apb.beacon.common;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AppUtil {
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


    public static boolean hasInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i < info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

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
			DisplayMetrics metrics, double densityRatio) {

		int width, height;
		int originalWidthScaled = (int) (drawable.getIntrinsicWidth()* metrics.density * densityRatio);
		int originalHeightScaled = (int) (drawable.getIntrinsicHeight()* metrics.density * densityRatio);
		if (originalWidthScaled > metrics.widthPixels) {
			height = drawable.getIntrinsicHeight() * metrics.widthPixels/ drawable.getIntrinsicWidth();
			width = metrics.widthPixels;
		} else {
			height = originalHeightScaled;
			width = originalWidthScaled;
		}
		try {
			drawable.setBounds(0, 0, width, height);
			Log.e(">>>>>>>>>>>>>>", "image width = " + width + " & height = "+ height);
		} catch (Exception ex) {
		}

		return drawable;
	}
	
	//call the receiver to check the current running activity
    public static void CheckCurrentRunningActivity(Context context) {
		
		// Start receiver with the name StartupReceiver_Manual_Start
		// Check AndroidManifest.xml file
    	context.getApplicationContext().sendBroadcast(
				new Intent("StartupReceiver_Manual_Start"));
	}
    
    
    //remove all the views of previous activity to clear memory
    public void unbindDrawables(View view) {
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


}
