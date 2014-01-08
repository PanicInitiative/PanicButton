package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.adapter.PageActionAdapter;
import com.apb.beacon.adapter.PageItemAdapter;
import com.apb.beacon.common.ImageDownloader;
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
//    private String mDescription = "...your html here...";

    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
//    Button bAction;
    ListView lvItems, lvActions;
    LinearLayout llWarning, llStatus;

    Page currentPage;
    PageItemAdapter pageItemAdapter;
    PageActionAdapter pageActionAdapter;

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

//        bAction = (Button) view.findViewById(R.id.fragment_action);
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

//        bAction.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String pageId = currentPage.getAction().get(0).getLink();
//
//                Intent i = new Intent(activity, WizardActivity.class);
//                i.putExtra("page_id",  pageId);
//                startActivity(i);
//            }
//        });

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

            if(currentPage.getStatus() == null || currentPage.getStatus().size() == 0)
                llStatus.setVisibility(View.GONE);
            else{
                String color = currentPage.getStatus().get(0).getColor();
                if(color.equals("red"))
                    tvStatus.setTextColor(Color.RED);
                else
                    tvStatus.setTextColor(Color.GREEN);
                tvStatus.setText(currentPage.getStatus().get(0).getTitle());
            }

            if(currentPage.getContent() == null)
                tvContent.setText(View.GONE);
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

//            bAction.setText(currentPage.getAction().get(0).getTitle());

            pageActionAdapter = new PageActionAdapter(activity, null);
            lvActions.setAdapter(pageActionAdapter);
            pageActionAdapter.setData(currentPage.getAction());


            pageItemAdapter = new PageItemAdapter(activity, null);
            lvItems.setAdapter(pageItemAdapter);

//            Log.e(">>>>>>>>", "item count = " + currentPage.getItems().size());
            pageItemAdapter.setData(currentPage.getItems());

            updateImages(true, currentPage.getContent());
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
                }, null);
        tvContent.setText(spanned);
    }

}