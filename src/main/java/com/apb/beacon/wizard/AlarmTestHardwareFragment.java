package com.apb.beacon.wizard;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apb.beacon.R;
import com.apb.beacon.adapter.PageActionAdapter;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;

import java.util.HashMap;

/**
 * Created by aoe on 1/9/14.
 */
public class AlarmTestHardwareFragment extends Fragment{

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

//    DisplayMetrics metrics;

//    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
//    Button bSuccess;
//    ListView lvItems, lvActions;
//    LinearLayout llWarning, llStatus;

    Page currentPage;
    PageItemAdapter pageItemAdapter;
    PageActionAdapter pageActionAdapter;

    public static AlarmTestHardwareFragment newInstance(String pageId) {
        AlarmTestHardwareFragment f = new AlarmTestHardwareFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_emergency_alert3, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
//            metrics = new DisplayMetrics();
//            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

//            IntentFilter filter = new IntentFilter();
//            filter.addAction(Intent.ACTION_SCREEN_ON);
//            filter.addAction(Intent.ACTION_SCREEN_OFF);
//            activity.registerReceiver(wizardHardwareReceiver, filter);


            String pageId = getArguments().getString(PAGE_ID);
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();


            if(currentPage.getContent() == null){
            }
            else{
                Toast.makeText(activity, Html.fromHtml(currentPage.getContent()), 5000).show();
            }
//                tvContent.setText(Html.fromHtml(currentPage.getContent()));
        }
    }
}
