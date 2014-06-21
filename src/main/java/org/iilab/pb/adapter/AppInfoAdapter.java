package org.iilab.pb.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import org.iilab.pb.R;
import org.iilab.pb.model.AppInfo;

/**
 * Created by aoe on 1/24/14.
 */
public class AppInfoAdapter extends BaseAdapter {

    private static final String TAG = AppInfoAdapter.class.getSimpleName();

    private Context mContext;
    private List<AppInfo> mListAppInfo;
    Bitmap bMap;

    public AppInfoAdapter(Context context, List<AppInfo> list) {
        mContext = context;
        mListAppInfo = list;
    }

    @Override
    public int getCount() {
        return mListAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mListAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfo entry = mListAppInfo.get(position);
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.griditem_appinfo, null);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String packageName = entry.getPackageName();

        holder.tvName.setText(entry.getAppName());

        if (packageName.equals(mContext.getPackageName())) {
            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_calculator);
            holder.ivIcon.setImageBitmap(icon);
        } else {
            try {
                ApplicationInfo app = mContext.getPackageManager().getApplicationInfo(packageName, 0);
                Drawable icon = mContext.getPackageManager().getApplicationIcon(app);
                holder.ivIcon.setBackgroundDrawable(icon);

            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(mContext, "error in getting icon", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
    }
}
