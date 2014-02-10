package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.adapter.PageActionAdapter;
import com.apb.beacon.adapter.PageActionFakeAdapter;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.common.ImageDownloader;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageItem;

import java.util.HashMap;


/**
 * Created by aoe on 1/3/14.
 */
public class NewSimpleFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
    ListView lvItems, lvActions;
    LinearLayout llWarning, llStatus;

    Page currentPage;
    PageItemAdapter pageItemAdapter;
    PageActionAdapter pageActionAdapter;
    boolean isPageStatusAvailable;

    public static NewSimpleFragment newInstance(String pageId) {
        NewSimpleFragment f = new NewSimpleFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return(f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_simple_fragment, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        llStatus = (LinearLayout) view.findViewById(R.id.ll_fragment_status);
        tvStatus = (TextView) view.findViewById(R.id.fragment_status);

        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageId = currentPage.getStatus().get(0).getLink();

                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
            }
        });

        lvItems = (ListView) view.findViewById(R.id.fragment_item_list);
        lvActions = (ListView) view.findViewById(R.id.fragment_action_list);

        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) view.findViewById(R.id.fragment_warning);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageItem selectedItem = (PageItem) parent.getItemAtPosition(position);

                String pageId = selectedItem.getLink();

                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
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

            String pageId = getArguments().getString(PAGE_ID);
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if(currentPage.getStatus() == null || currentPage.getStatus().size() == 0){
                isPageStatusAvailable = false;
                llStatus.setVisibility(View.GONE);
            } else{
                isPageStatusAvailable = true;
                String color = currentPage.getStatus().get(0).getColor();
                if(color.equals("red"))
                    tvStatus.setTextColor(Color.RED);
                else
                    tvStatus.setTextColor(Color.GREEN);
                tvStatus.setText(currentPage.getStatus().get(0).getTitle());
            }

            if(currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else
                tvContent.setText(currentPage.getContent());

            if(currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());

            if(currentPage.getWarning() == null)
                llWarning.setVisibility(View.GONE);
            else
                tvWarning.setText(currentPage.getWarning());

            if(currentPage.getId().equals("home-ready")){
                isPageStatusAvailable = false;
            }

            pageActionAdapter = new PageActionAdapter(activity, null, isPageStatusAvailable);

            if (pageId.equals("setup-alarm-test-hardware-success") || pageId.equals("setup-alarm-test-disguise-success")) {
                PageActionFakeAdapter pageActionFakeAdapter = new PageActionFakeAdapter(activity, null);
                lvActions.setAdapter(pageActionFakeAdapter);
                pageActionFakeAdapter.setData(currentPage.getAction());

                Handler actionHandler = new Handler();
                actionHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lvActions.setAdapter(pageActionAdapter);
                        pageActionAdapter.setData(currentPage.getAction());
                    }
                }, 1000);
            } else {
                lvActions.setAdapter(pageActionAdapter);
                pageActionAdapter.setData(currentPage.getAction());
            }


            pageItemAdapter = new PageItemAdapter(activity, null);
            lvItems.setAdapter(pageItemAdapter);

            pageItemAdapter.setData(currentPage.getItems());

            updateImages(true, currentPage.getContent());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause NewSimpleFragment");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(">>>>>>>>>>", "onStop NewSimpleFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(">>>>>>>>>>", "onStart NewSimpleFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(">>>>>>>>>>", "onResume NewSimpleFragment");
        if(currentPage.getId().equals("home-not-configured-alarm")){
            ApplicationSettings.setWizardState(activity, AppConstants.wizard_flag_home_not_configured_alarm);
        } else if(currentPage.getId().equals("home-not-configured-disguise")){
            ApplicationSettings.setWizardState(activity, AppConstants.wizard_flag_home_not_configured_disguise);
        } else if(currentPage.getId().equals("home-ready")){
            ApplicationSettings.setWizardState(activity, AppConstants.wizard_flag_home_ready);
        }
    }

    private void updateImages(final boolean downloadImages, final String textHtml) {
        if (textHtml == null) return;
        Spanned spanned = Html.fromHtml(textHtml,
                new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(final String source) {
                        Log.e(">>>>>>", "image src = " + source);
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

}