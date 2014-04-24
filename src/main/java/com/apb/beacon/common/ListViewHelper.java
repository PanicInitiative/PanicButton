package com.apb.beacon.common;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewHelper {
    
    public static void getListViewSize(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        Log.e(">>>>>>>>>", "list adpater count = " + listAdapter.getCount());
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup)
                listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            Log.e(">>>>>>>", "this item height = " + listItem.getMeasuredHeight());
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        Log.e(">>>>>>>>>>", "params height before modify = " + params.height);
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        Log.e(">>>>>>>>>>", "params height after modify = " + params.height);
        listView.setLayoutParams(params);
    }

}
