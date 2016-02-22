package org.iilab.pb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import org.iilab.pb.CalculatorActivity;
import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.model.PageAction;
import org.iilab.pb.trigger.HardwareTriggerService;

import java.util.List;

import static org.iilab.pb.common.AppConstants.FROM_WIZARD_ACTIVITY;
import static org.iilab.pb.common.AppConstants.PAGE_ADVANCED_SETTINGS;
import static org.iilab.pb.common.AppConstants.PAGE_CLOSE;
import static org.iilab.pb.common.AppConstants.PAGE_CLOSE_TRAINING;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_WARNING;
import static org.iilab.pb.common.AppConstants.PAGE_STATUS_CHECKED;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_READY;
import static org.iilab.pb.common.AppUtil.checkAndRequestPermissions;
import static org.iilab.pb.common.ApplicationSettings.getWizardState;
import static org.iilab.pb.common.ApplicationSettings.isHardwareTriggerServiceEnabled;
/**
 * Created by aoe on 1/5/14.
 */
public class PageActionAdapter extends ArrayAdapter<PageAction> {

    private final Context mContext;
    private boolean isPageStatusAvailable;
    private LayoutInflater mInflater;
    private int parentActivity;
    private static final String TAG = PageActionAdapter.class.getName();

    public PageActionAdapter(Context context, List<PageAction> actionList, boolean isPageStatusAvailable, int parentActivity) {
        super(context, R.layout.row_page_action);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isPageStatusAvailable = isPageStatusAvailable;
        this.parentActivity = parentActivity;
    }


    private static class ViewHolder {
        Button bActionWithoutStatus;
        Button bActionWithStatus;
        ImageView ivTick;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_page_action, null);

            holder = new ViewHolder();
            holder.bActionWithoutStatus = (Button) convertView.findViewById(R.id.b_action_without_status);
            holder.bActionWithStatus = (Button) convertView.findViewById(R.id.b_action_with_status);
            holder.ivTick = (ImageView) convertView.findViewById(R.id.iv_tick);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PageAction item = getItem(position);

        if (isPageStatusAvailable) {
            holder.bActionWithoutStatus.setVisibility(View.INVISIBLE);
            holder.bActionWithStatus.setVisibility(View.VISIBLE);
        } else {
            holder.bActionWithoutStatus.setVisibility(View.VISIBLE);
            holder.bActionWithStatus.setVisibility(View.INVISIBLE);
        }

        holder.bActionWithoutStatus.setText(item.getTitle());
        holder.bActionWithoutStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pageId = getItem(position).getLink();

                if (pageId.equals(PAGE_CLOSE)) {
//                    setFirstRun(mContext, false);
                    Intent i = new Intent(mContext, CalculatorActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(i);
                    ((MainActivity) mContext).callFinishActivityReceiver();
//                    ((Activity) mContext).overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
                }
                else if (pageId.equals(PAGE_CLOSE_TRAINING)){
                    Log.d(TAG,"inside close training ");
                    Intent i = new Intent(mContext, MainActivity.class);
                    i.putExtra(PAGE_ID, PAGE_ADVANCED_SETTINGS);
                    mContext.startActivity(i);
                    int wizardState = getWizardState(mContext.getApplicationContext());
                    Log.e(TAG, "wizardState = " + wizardState);
                    if (wizardState == WIZARD_FLAG_HOME_READY && isHardwareTriggerServiceEnabled(mContext)) {
                        //after the redo training excercise is done, we need to restart the hardware trigger service.
                        mContext.startService(new Intent(mContext, HardwareTriggerService.class));
                    }
                    ((WizardActivity) mContext).callFinishActivityReceiver();
                }
                else if(pageId.equals(PAGE_SETUP_WARNING)){
                    if(checkAndRequestPermissions((WizardActivity)mContext))
                        callNextActivity(pageId);
                }
                else {
                    callNextActivity(pageId);
                }
            }
        });

        if (item.getStatus() != null) {
            holder.bActionWithoutStatus.setEnabled(false);
            if (item.getStatus().equals(PAGE_STATUS_CHECKED)) {
                holder.ivTick.setVisibility(View.VISIBLE);
            } else {           // status = "disabled"
                holder.ivTick.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.bActionWithoutStatus.setEnabled(true);
            holder.ivTick.setVisibility(View.INVISIBLE);
        }


        holder.bActionWithStatus.setText(item.getTitle());
        holder.bActionWithStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pageId = getItem(position).getLink();

                if (pageId.equals(PAGE_CLOSE)) {
//                    setFirstRun(mContext, false);
                    Intent i = new Intent(mContext.getApplicationContext(), CalculatorActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(i);
                    ((Activity) mContext).overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
                } else {
                    callNextActivity(pageId);
                }
            }
        });

        if (item.getStatus() != null) {
            holder.bActionWithStatus.setEnabled(false);
            if (item.getStatus().equals(PAGE_STATUS_CHECKED)) {
                holder.ivTick.setVisibility(View.VISIBLE);
            } else {           // status = "disabled"
                holder.ivTick.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.bActionWithStatus.setEnabled(true);
            holder.ivTick.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void callNextActivity(String pageId) {
        Intent i;
        if (parentActivity == FROM_WIZARD_ACTIVITY) {
            i = new Intent(mContext, WizardActivity.class);

        } else {
            i = new Intent(mContext, MainActivity.class);
        }
        i.putExtra(PAGE_ID, pageId);
        mContext.startActivity(i);
    }
    public void setData(List<PageAction> actionList) {
        clear();
        if (actionList != null) {
            for (int i = 0; i < actionList.size(); i++) {
                add(actionList.get(i));
            }
        }
    }
}
