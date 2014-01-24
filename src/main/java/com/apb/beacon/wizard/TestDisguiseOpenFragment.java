package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.apb.beacon.R;
import com.apb.beacon.adapter.AppInfoAdapter;
import com.apb.beacon.common.ImageDownloader;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.AppInfo;
import com.apb.beacon.model.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aoe on 1/16/14.
 */
public class TestDisguiseOpenFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    List<AppInfo> appList;
    GridView gvAppList;

//    TextView tvContent;
//    Button bSkip;

    Page currentPage;

    public static TestDisguiseOpenFragment newInstance(String pageId) {
        TestDisguiseOpenFragment f = new TestDisguiseOpenFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_interactive_disguise_test_open, container, false);

//        tvContent = (TextView) view.findViewById(R.id.fragment_contents);
//        bSkip = (Button) view.findViewById(R.id.b_action);
        gvAppList = (GridView) view.findViewById(R.id.gv_app_list);
        gvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo selectedAppInfo = (AppInfo) parent.getItemAtPosition(position);

                if(selectedAppInfo.getPackageName().equals(activity.getPackageName())){
                        String pageId = currentPage.getSuccessId();

                        Intent i = new Intent(activity, WizardActivity.class);
                        i.putExtra("page_id", pageId);
                        activity.startActivity(i);
                        activity.finish();
                }
                else{
                    Toast.makeText(activity, "Please press the Panic Button app icon.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);


            final PackageManager pm = activity.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            appList = new ArrayList<AppInfo>();
            int otherPackageCount = 0;
            for (ApplicationInfo packageInfo : packages) {
                String packageName = packageInfo.packageName;
                if(otherPackageCount < 15 && (packageName.startsWith("com.android.") || packageName.startsWith(" com.google.android."))){
                    try {
                        ApplicationInfo app = pm.getApplicationInfo(packageName, 0);
                        String appName =  pm.getApplicationLabel(app).toString();
                        if(appName != null && !appName.equals(packageName)){
                            Log.e(">>>>>>>", "app name = " + appName);
                            appList.add(new AppInfo(appName, packageName));
                            otherPackageCount++;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if(packageName.equals(activity.getPackageName())){
                    appList.add(new AppInfo(activity.getString(R.string.app_name), packageName));
                }
            }

            gvAppList.setAdapter(new AppInfoAdapter(activity, appList));


            String pageId = getArguments().getString(PAGE_ID);
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

//            if(currentPage.getAction() != null && currentPage.getAction().size() > 0){
//                bSkip.setText(currentPage.getAction().get(0).getTitle());
//                bSkip.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String pageId = currentPage.getAction().get(0).getLink();
//
//                        Intent i = new Intent(activity, WizardActivity.class);
//                        i.putExtra("page_id", pageId);
//                        activity.startActivity(i);
//                        activity.finish();
//                    }
//                });
//            } else {
//                bSkip.setVisibility(View.GONE);
//            }

//            if(currentPage.getContent() == null)
//                tvContent.setVisibility(View.GONE);
//            else{
//                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
//                updateImages(true, currentPage.getContent());
//            }

//            if(currentPage.getIntroduction() != null){
//                Toast.makeText(activity, Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()), Toast.LENGTH_LONG).show();
//            }
        }
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
//        tvContent.setText(spanned);
    }
}
