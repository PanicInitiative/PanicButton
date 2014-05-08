package com.apb.beacon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;

import com.apb.beacon.R;
import com.apb.beacon.WizardActivity;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;

/**
 * Created by aoe on 1/17/14.
 */
public class WizardTestDisguiseUnlockFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    private int[] buttonIds = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply, R.id.divide, R.id.decimal_point, R.id.char_c};

    Page currentPage;

    boolean mHasPerformedLongPress;
    Runnable mPendingCheckForLongPress;


    public static WizardTestDisguiseUnlockFragment newInstance(String pageId) {
        WizardTestDisguiseUnlockFragment f = new WizardTestDisguiseUnlockFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_layout, container, false);
        registerButtonEvents(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
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
        Log.e(">>>>>", "onPause WizardTestDisguiseUnlockFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume WizardTestDisguiseUnlockFragment");
    }


    private void registerButtonEvents(View view) {
        for (int buttonId : buttonIds) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnTouchListener(touchListener);
        }
    }


    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:

                    if (!mHasPerformedLongPress) {
                        // This is a tap, so remove the long-press check
                        if (mPendingCheckForLongPress != null) {
                            v.removeCallbacks(mPendingCheckForLongPress);
                        }
                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                    if (mPendingCheckForLongPress == null) {
                        mPendingCheckForLongPress = new Runnable() {
                            public void run() {

                                String pageId = currentPage.getSuccessId();

                                Intent i = new Intent(activity, WizardActivity.class);
                                i.putExtra("page_id", pageId);
                                activity.startActivity(i);
                                activity.finish();
                            }
                        };
                    }


                    mHasPerformedLongPress = false;
                    v.postDelayed(mPendingCheckForLongPress, 3000);

                    break;
                case MotionEvent.ACTION_MOVE:
                    final int x = (int) event.getX();
                    final int y = (int) event.getY();

                    // Be lenient about moving outside of buttons
                    int slop = ViewConfiguration.get(v.getContext()).getScaledTouchSlop();
                    if ((x < 0 - slop) || (x >= v.getWidth() + slop) ||
                            (y < 0 - slop) || (y >= v.getHeight() + slop)) {

                        if (mPendingCheckForLongPress != null) {
                            v.removeCallbacks(mPendingCheckForLongPress);
                        }
                    }
                    break;
                default:
                    return false;
            }

            return false;
        }

    };
}
