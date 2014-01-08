package com.apb.beacon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.R;
import com.apb.beacon.model.PageAction;
import com.apb.beacon.wizard.WizardActivity;

import java.util.List;


/**
 * Created by aoe on 1/5/14.
 */
public class PageActionAdapter extends ArrayAdapter<PageAction> {

    private Context mContext;
    LayoutInflater mInflater;

    public PageActionAdapter(Context context, List<PageAction> actionList) {
        super(context, R.layout.row_page_action);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    private static class ViewHolder {
        Button bAction;
        ImageView ivTick;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_page_action, null);

            holder = new ViewHolder();
            holder.bAction = (Button) convertView.findViewById(R.id.b_action);
            holder.ivTick = (ImageView) convertView.findViewById(R.id.iv_tick);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PageAction item = getItem(position);

        holder.bAction.setText(item.getTitle());
        holder.bAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pageId = getItem(position).getLink();

                if (pageId.equals("close")) {
                    ApplicationSettings.completeFirstRun(mContext);
                    Intent i = new Intent(mContext.getApplicationContext(), CalculatorActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(i);
                    ((Activity) mContext).overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
//                    ((Activity) mContext).finish();
                } else {
                    Intent i = new Intent(mContext, WizardActivity.class);
                    i.putExtra("page_id", pageId);
                    mContext.startActivity(i);
//                    mContext.startActivityForResult(i, 0);
                }
            }
        });

        if (item.getStatus() != null) {
            holder.bAction.setEnabled(false);
            if (item.getStatus().equals("checked")) {
                holder.ivTick.setVisibility(View.VISIBLE);
            } else {           // statis = "disabled"
                holder.ivTick.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.bAction.setEnabled(true);
            holder.ivTick.setVisibility(View.INVISIBLE);
        }

        return convertView;
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
