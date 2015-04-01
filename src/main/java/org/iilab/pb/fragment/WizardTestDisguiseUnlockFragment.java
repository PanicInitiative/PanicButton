package org.iilab.pb.fragment;

import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(">>>>>", "onDestroyView WizardTestDisguiseUnlockFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(">>>>>", "onDestroy WizardTestDisguiseUnlockFragment");
    }

    private void registerButtonEvents(View view) {
        for (int buttonId : buttonIds) {
            Button button = (Button) view.findViewById(buttonId);
            button.setOnTouchListener(touchListener);
        }
    }

    private void unregisterButtonEvents(Activity activity) {
        for (int buttonId : buttonIds) {
            Button button = (Button) activity.findViewById(buttonId);
            button.setOnTouchListener(null);
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(final View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                	Log.e(">>>>>", "ACTION_UP");
                	Log.e(">>>>>", event.toString());
                    if (!mHasPerformedLongPress) {
                    	Log.e(">>>>>", "ACTION_UP No Long Press");
                        // This is a tap, so remove the long-press check
                        if (mPendingCheckForLongPress != null) {
                            v.removeCallbacks(mPendingCheckForLongPress);
                        	Log.e(">>>>>", "ACTION_UP Removed Callbacks");
                        }
                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                	Log.e(">>>>>", "ACTION_DOWN");
                    if (mPendingCheckForLongPress == null) {
                        mPendingCheckForLongPress = new Runnable() {
                            public void run() {
                            	Log.e(">>>>>", "RUNNING RUNNABLE");

                                String pageId = currentPage.getSuccessId();
                                unregisterButtonEvents(activity);
                                Intent i = new Intent(activity, WizardActivity.class);
                                i.putExtra("page_id", pageId);
                                activity.startActivity(i);
                                activity.finish();
                            }
                        };
                       	Log.e(">>>>>", "ACTION_DOWN CREATED RUNNABLE");
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
