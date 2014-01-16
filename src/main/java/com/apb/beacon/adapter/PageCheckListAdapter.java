package com.apb.beacon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.apb.beacon.R;
import com.apb.beacon.model.PageChecklist;
import com.apb.beacon.wizard.WizardActivity;

import java.util.List;

/**
 * Created by aoe on 1/16/14.
 */
public class PageCheckListAdapter extends ArrayAdapter<PageChecklist> {

    private Context mContext;
    LayoutInflater mInflater;


    public PageCheckListAdapter(Context context, List<PageChecklist> checklist) {
        super(context, R.layout.row_page_checklist);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        Button bAction;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_page_checklist, null);

            holder = new ViewHolder();
            holder.bAction = (Button) convertView.findViewById(R.id.b_action);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PageChecklist item = getItem(position);

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


    public void setData(List<PageChecklist> itemList) {
        clear();
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                add(itemList.get(i));
            }
        }
    }
}
