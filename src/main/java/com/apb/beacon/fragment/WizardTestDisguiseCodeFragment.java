package com.apb.beacon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apb.beacon.R;
import com.apb.beacon.WizardActivity;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;

import java.util.HashMap;

/**
 * Created by aoe on 1/18/14.
 */
public class WizardTestDisguiseCodeFragment extends Fragment {

    private EditText passwordEditText;
    private TextView tvContent;
    private Button bAction;

    private static final String PAGE_ID = "page_id";
    private HashMap<String, Drawable> mImageCache = new HashMap<String, Drawable>();
    private Activity activity;

    DisplayMetrics metrics;

    private Handler inactiveHandler = new Handler();
    private Handler failHandler = new Handler();


    Page currentPage;

    public static WizardTestDisguiseCodeFragment newInstance(String pageId) {
        WizardTestDisguiseCodeFragment f = new WizardTestDisguiseCodeFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_disguise_test_code, container, false);

        passwordEditText = (EditText) view.findViewById(R.id.create_pin_edittext);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        passwordEditText.addTextChangedListener(passwordTextChangeListener);

        bAction = (Button) view.findViewById(R.id.b_action);
        bAction.setText("Go");
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                if (ApplicationSettings.passwordMatches(activity, password)) {

                    inactiveHandler.removeCallbacks(runnableInteractive);
                    failHandler.removeCallbacks(runnableFailed);

//                    String pageId = currentPage.getSuccessId();
                    String pageId = null;
                    if (currentPage.getAction().size() > 0)
                        pageId = currentPage.getAction().get(0).getLink();
                    else
                        pageId = currentPage.getSuccessId();

                    Intent i = new Intent(activity, WizardActivity.class);
                    i.putExtra("page_id", pageId);
                    activity.startActivity(i);
                    activity.finish();
                    return;
                }
                AppUtil.setError(activity, passwordEditText, R.string.incorrect_pin);
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
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            bAction.setText(currentPage.getAction().get(0).getTitle());

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else {
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
                AppUtil.updateImages(true, currentPage.getContent(), activity, metrics, tvContent);
//                updateImages(true, currentPage.getContent());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause WizardTestDisguiseCodeFragment");

        inactiveHandler.removeCallbacks(runnableInteractive);
        failHandler.removeCallbacks(runnableFailed);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume WizardTestDisguiseCodeFragment");

        inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
        failHandler.postDelayed(runnableFailed, Integer.parseInt(currentPage.getTimers().getFail()) * 1000);
    }


    private TextWatcher passwordTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {
            inactiveHandler.removeCallbacks(runnableInteractive);
            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
        }

        @Override
        public void afterTextChanged(Editable text) {
        }
    };

    private Runnable runnableInteractive = new Runnable() {
        public void run() {

            failHandler.removeCallbacks(runnableFailed);

            String pageId = currentPage.getFailedId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra("page_id", pageId);
            activity.startActivity(i);
            activity.finish();
        }
    };

    private Runnable runnableFailed = new Runnable() {
        public void run() {

            inactiveHandler.removeCallbacks(runnableInteractive);

            String pageId = currentPage.getFailedId();

            Intent i = new Intent(activity, WizardActivity.class);
            i.putExtra("page_id", pageId);
            activity.startActivity(i);
            activity.finish();
        }
    };

//    private void updateImages(final boolean downloadImages, final String textHtml) {
//        if (textHtml == null) return;
//        Spanned spanned = Html.fromHtml(textHtml,
//                new Html.ImageGetter() {
//                    @Override
//                    public Drawable getDrawable(final String source) {
//                        Log.e(">>>>>>", "image src = " + source);
////                        if(!source.startsWith("http")){
////                            source = "http://teampanicbutton.github.io/" + source;
////                        }
//                        Drawable drawable = mImageCache.get(source);
//                        if (drawable != null) {
//                            return drawable;
//                        } else if (downloadImages) {
//                            new ImageDownloader(new ImageDownloader.ImageDownloadListener() {
//                                @Override
//                                public void onImageDownloadComplete(byte[] bitmapData) {
//                                    Drawable drawable = new BitmapDrawable(getResources(),
//                                            BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length));
//
//                                    int width, height;
//                                    int originalWidthScaled = (int) (drawable.getIntrinsicWidth() * metrics.density * 0.75);
//                                    int originalHeightScaled = (int) (drawable.getIntrinsicHeight() * metrics.density * 0.75);
//                                    if (originalWidthScaled > metrics.widthPixels) {
//                                        height = drawable.getIntrinsicHeight() * metrics.widthPixels / drawable.getIntrinsicWidth();
//                                        width = metrics.widthPixels;
//                                    } else {
//                                        height = originalHeightScaled;
//                                        width = originalWidthScaled;
//                                    }
//                                    try {
//                                        drawable.setBounds(0, 0, width, height);
//                                        Log.e(">>>>>>>>>>>>>>", "image width = " + width + " & height = " + height);
//                                    } catch (Exception ex) {
//                                    }
//                                    mImageCache.put(source, drawable);
//                                    updateImages(false, textHtml);
//                                }
//
//                                @Override
//                                public void onImageDownloadFailed(Exception ex) {
//                                }
//                            }).execute(source);
//                        }
//                        return null;
//                    }
//                }, new MyTagHandler());
//        tvContent.setText(spanned);
//    }
}

