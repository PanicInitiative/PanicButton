package com.apb.beacon.wizard;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.common.ImageDownloader;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.WizardPage;

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

    TextView tvTitle, tvContent;
    Button bAction;
    ListView lvItems;


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
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);
        bAction = (Button) view.findViewById(R.id.fragment_action);
        lvItems = (ListView) view.findViewById(R.id.fragment_item_list);

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
            WizardPage thisPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();

//            WizardSimplePage thisPage = HomeActivity.FirstPage;

            tvTitle.setText(thisPage.getTitle());
            tvContent.setText(thisPage.getContent());

            bAction.setText(thisPage.getAction().get(0).getTitle());

            updateImages(true, thisPage.getContent());
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
                                    int originalWidthScaled = (int) (drawable.getIntrinsicWidth() * metrics.density);
                                    int originalHeightScaled = (int) (drawable.getIntrinsicHeight() * metrics.density);
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