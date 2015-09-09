package org.iilab.pb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;


import java.util.List;

import org.iilab.pb.R;
import org.iilab.pb.model.PageAction;

/**
 * Created by aoe on 2/10/14.
 */
public class PageActionFakeAdapter extends ArrayAdapter<PageAction> {

    private Context mContext;
    private LayoutInflater mInflater;

    public PageActionFakeAdapter(Context context, List<PageAction> actionList) {
        super(context, R.layout.row_page_action);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        holder.bActionWithStatus.setVisibility(View.INVISIBLE);
        holder.bActionWithoutStatus.setVisibility(View.VISIBLE);

        holder.bActionWithoutStatus.setText(item.getTitle());
        holder.bActionWithoutStatus.setEnabled(false);


//        holder.bActionWithoutStatus.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String pageId = getItem(position).getLink();
//
//                if (pageId.equals("close")) {
//                    ApplicationSettings.setFirstRun(mContext, false);
//                    Intent i = new Intent(mContext.getApplicationContext(), CalculatorActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    mContext.startActivity(i);
//                    ((Activity) mContext).overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
//                } else {
//                    Intent i = new Intent(mContext, WizardActivity.class);
//                    i.putExtra("page_id", pageId);
//                    mContext.startActivity(i);
//                }
//            }
//        });
//
//        if (item.getStatus() != null) {
//            holder.bActionWithoutStatus.setEnabled(false);
//            if (item.getStatus().equals("checked")) {
//                holder.ivTick.setVisibility(View.VISIBLE);
//            } else {           // status = "disabled"
//                holder.ivTick.setVisibility(View.INVISIBLE);
//            }
//        } else {
//            holder.bActionWithoutStatus.setEnabled(true);
//            holder.ivTick.setVisibility(View.INVISIBLE);
//        }


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
