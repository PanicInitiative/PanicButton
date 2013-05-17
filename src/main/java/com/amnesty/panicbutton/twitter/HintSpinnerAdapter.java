package com.amnesty.panicbutton.twitter;

import android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.R.layout.simple_spinner_item;

public class HintSpinnerAdapter extends ArrayAdapter<String> {

    public HintSpinnerAdapter(String hintText, List<String> values, Context context) {
        super(context, simple_spinner_item);
        for (String value : values) {
            this.add(value);
        }
        this.add(hintText);
        this.setDropDownViewResource(R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        if (position == getCount()) {
            TextView hintView = (TextView) view.findViewById(R.id.text1);
            hintView.setText("");
            hintView.setHint(getItem(getCount()));
        }
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }
}
