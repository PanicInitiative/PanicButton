package com.apb.beacon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.apb.beacon.R;
import com.apb.beacon.model.PageAction;
import com.apb.beacon.wizard.WizardActivity;

import java.util.List;


/**
 * Created by aoe on 1/5/14.
 */
public class PageActionAdapter extends ArrayAdapter<PageAction>{

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

                Intent i = new Intent(mContext, WizardActivity.class);
                i.putExtra("page_id", pageId);
                mContext.startActivity(i);
            }
        });
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
