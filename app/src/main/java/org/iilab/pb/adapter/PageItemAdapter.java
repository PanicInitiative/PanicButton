package org.iilab.pb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import org.iilab.pb.R;
import org.iilab.pb.model.PageItem;

/**
 * Created by aoe on 1/6/14.
 */
public class PageItemAdapter extends ArrayAdapter<PageItem> {

    private Context mContext;
    LayoutInflater mInflater;


    public PageItemAdapter(Context context, List<PageItem> itemList) {
        super(context, R.layout.row_page_item);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        TextView tvItem;
        ImageView ivArrow;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_page_item, null);

            holder = new ViewHolder();
            holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PageItem item = getItem(position);

        holder.tvItem.setText(item.getTitle());
        return convertView;
    }


    public void setData(List<PageItem> itemList) {
        clear();
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                add(itemList.get(i));
            }
        }
    }
}
