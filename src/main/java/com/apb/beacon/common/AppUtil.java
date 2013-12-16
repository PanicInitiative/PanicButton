package com.apb.beacon.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import com.apb.beacon.model.LocalCachePage;

import java.util.Calendar;
import java.util.List;

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



    public static LocalCachePage parseMarkDown(int pageNumber, String pageName, String mdContent){
        LocalCachePage page = new LocalCachePage();
        page.setPageNumber(pageNumber);
        page.setPageName(pageName);

        String[] subStr = mdContent.split("---");
        if(subStr.length > 1){
            String[] headerStr = subStr[1].split("\n");
            Log.e(">>>>>>>>", "headerStr length = " + headerStr.length);
            for(String str : headerStr){
                Log.e("<<<<<", "headStr string = " + str);
                if(str == null || str.equals(""))
                    continue;
                String[] strPart = str.split(":");
                for(String testStr : strPart){
                    Log.e(">>>>>>>", "test = " + testStr);
                }

                String key = strPart[0];
                String field = strPart[1].trim();

                if(key.equalsIgnoreCase("title")){
                    page.setPageTitle(field);
                } else if(key.equalsIgnoreCase("option")){
                    page.setPageOption(field);
                }  if(key.equalsIgnoreCase("action")){
                    page.setPageAction(field);
                }
            }
        }

        if(subStr.length > 2){
            String content = subStr[2].trim();
            page.setPageContent(content);
        }

        return page;
    }

}
