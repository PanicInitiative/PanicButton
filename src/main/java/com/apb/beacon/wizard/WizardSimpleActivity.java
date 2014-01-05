package com.apb.beacon.wizard;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.common.ImageDownloader;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;

import java.util.HashMap;

/**
 * Created by aoe on 1/5/14.
 */
public class WizardSimpleActivity extends Activity{

    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();

    DisplayMetrics metrics;

    TextView tvTitle, tvContent;
    Button bAction;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_simple_fragment);

        tvTitle = (TextView) findViewById(R.id.fragment_title);
        tvContent = (TextView) findViewById(R.id.fragment_contents);
        bAction = (Button) findViewById(R.id.fragment_action);
        lvItems = (ListView) findViewById(R.id.fragment_item_list);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String pageId = getIntent().getExtras().getString("page_id");
        String defaultLang = "en";

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        Page thisPage = dbInstance.retrievePage(pageId, defaultLang);
        dbInstance.close();

        tvTitle.setText(thisPage.getTitle());
        tvContent.setText(thisPage.getContent());

        bAction.setText(thisPage.getAction().get(0).getTitle());

        updateImages(true, thisPage.getContent());
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
