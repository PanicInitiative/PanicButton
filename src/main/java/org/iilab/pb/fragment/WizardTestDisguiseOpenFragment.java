package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.adapter.AppInfoAdapter;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.AppInfo;
import org.iilab.pb.model.Page;

/**
 * Created by aoe on 1/16/14.
 */
public class WizardTestDisguiseOpenFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    List<AppInfo> appList;
    GridView gvAppList;

    Page currentPage;

    public static WizardTestDisguiseOpenFragment newInstance(String pageId) {
        WizardTestDisguiseOpenFragment f = new WizardTestDisguiseOpenFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_disguise_test_open, container, false);

        gvAppList = (GridView) view.findViewById(R.id.gv_app_list);
        gvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppInfo selectedAppInfo = (AppInfo) parent.getItemAtPosition(position);

                if (selectedAppInfo.getPackageName().equals(activity.getPackageName())) {

                    String pageId = currentPage.getSuccessId();

                    Intent i = new Intent(activity, WizardActivity.class);
                    i.putExtra("page_id", pageId);
                    activity.startActivity(i);
                    activity.finish();
                } else {
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
                if (otherPackageCount < 15 && (packageName.startsWith("com.android.") || packageName.startsWith(" com.google.android."))) {
                    try {
                        ApplicationInfo app = pm.getApplicationInfo(packageName, 0);
                        String appName = pm.getApplicationLabel(app).toString();
                        if (appName != null && !appName.equals(packageName)) {
                            appList.add(new AppInfo(appName, packageName));
                            otherPackageCount++;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (packageName.equals(activity.getPackageName())) {
                    appList.add(new AppInfo(activity.getString(R.string.app_name), packageName));
                }
            }

            gvAppList.setAdapter(new AppInfoAdapter(activity, appList));


            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause WizardTestDisguiseOpenFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume WizardTestDisguiseOpenFragment");
    }
}
