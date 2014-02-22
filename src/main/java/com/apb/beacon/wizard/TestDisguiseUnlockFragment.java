package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;

import com.apb.beacon.R;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;

/**
 * Created by aoe on 1/17/14.
 */
public class TestDisguiseUnlockFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private Activity activity;

    private int[] buttonIds = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight,
            R.id.nine, R.id.zero, R.id.equals_sign, R.id.plus, R.id.minus, R.id.multiply, R.id.divide};

    private Handler inactiveHandler = new Handler();
    private Handler failHandler = new Handler();

    Page currentPage;

    boolean mHasPerformedLongPress;
    Runnable mPendingCheckForLongPress;


    public static TestDisguiseUnlockFragment newInstance(String pageId) {
        TestDisguiseUnlockFragment f = new TestDisguiseUnlockFragment();
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
            String defaultLang = "en";

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, defaultLang);
            dbInstance.close();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause TestDisguiseUnlockFragment");

        inactiveHandler.removeCallbacks(runnableInteractive);
        failHandler.removeCallbacks(runnableFailed);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume TestDisguiseUnlockFragment");

        inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
        failHandler.postDelayed(runnableFailed, Integer.parseInt(currentPage.getTimers().getFail()) * 1000);
    }


    private void registerButtonEvents(View view) {
        for (int buttonId : buttonIds) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnTouchListener(touchListener);
//            button.setOnLongClickListener(longClickListener);
            button.setOnClickListener(clickListener);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            inactiveHandler.removeCallbacks(runnableInteractive);
            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getInactive()) * 1000);
        }
    };


    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:

                    if (!mHasPerformedLongPress) {
                        // This is a tap, so remove the longpress check
                        if (mPendingCheckForLongPress != null) {
                            v.removeCallbacks(mPendingCheckForLongPress);
                        }
                        // v.performClick();
                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                    if (mPendingCheckForLongPress == null) {
                        mPendingCheckForLongPress = new Runnable() {
                            public void run() {
                                inactiveHandler.removeCallbacks(runnableInteractive);
                                failHandler.removeCallbacks(runnableFailed);

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
}
