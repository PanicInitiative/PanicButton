package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.iilab.pb.R;
import org.iilab.pb.adapter.PageItemAdapter;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import info.guardianproject.panic.Panic;
import info.guardianproject.panic.PanicTrigger;

public class PanicRespondersFragment extends Fragment {
    public static final String TAG = "PanicRespondersFragment";

    private static final String PAGE_ID = "page_id";
    private static final String PARENT_ACTIVITY = "parent_activity";
    private Activity activity;
    DisplayMetrics metrics;
    TextView tvTitle, tvIntro;
    Page currentPage;
    PageItemAdapter pageItemAdapter;

    private static final int CONNECT_RESULT = 0x01;

    private String responders[];
    private Set<String> enabledResponders;
    private Set<String> respondersThatCanConnect;
    private ArrayList<CharSequence> appLabelList;
    private ArrayList<Drawable> iconList;

    private String requestPackageName;

    public static PanicRespondersFragment newInstance(String pageId, int parentActivity) {
        PanicRespondersFragment f = new PanicRespondersFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_panic_responders, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        enabledResponders = PanicTrigger.getEnabledResponders(activity);
        respondersThatCanConnect = PanicTrigger.getRespondersThatCanConnect(activity);

        // sort enabled first, then disabled
        LinkedHashSet<String> a = new LinkedHashSet<String>(enabledResponders);
        LinkedHashSet<String> b = new LinkedHashSet<String>(PanicTrigger.getAllResponders(activity));
        b.removeAll(enabledResponders);
        a.addAll(b);
        responders = a.toArray(new String[a.size()]);

        PackageManager pm = getActivity().getPackageManager();
        appLabelList = new ArrayList<CharSequence>(responders.length);
        iconList = new ArrayList<Drawable>(responders.length);
        for (String packageName : responders) {
            try {
                appLabelList.add(pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)));
                iconList.add(pm.getApplicationIcon(packageName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(activity));
        recyclerView.setHasFixedSize(true); // does not change, except in onResume()
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(new RecyclerView.Adapter<AppRowHolder>() {
            @Override
            public AppRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.responder_row, parent, false);
                return new AppRowHolder(view);
            }

            @Override
            public void onBindViewHolder(AppRowHolder holder, int position) {
                String packageName = responders[position];
                boolean canConnect = respondersThatCanConnect.contains(packageName);
                holder.setupForApp(
                        packageName,
                        iconList.get(position),
                        appLabelList.get(position),
                        canConnect);
            }

            @Override
            public int getItemCount() {
                return appLabelList.size();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if (currentPage.getIntroduction() == null) {
                tvIntro.setVisibility(View.GONE);
            } else {
                tvIntro.setText(currentPage.getIntroduction());
            }

            pageItemAdapter = new PageItemAdapter(activity, null);
            pageItemAdapter.setData(currentPage.getItems());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, intent);
            }
        }
    }

    class AppRowHolder extends RecyclerView.ViewHolder {

        private final View.OnClickListener onClickListener;
        private final Switch onSwitch;
        private final TextView editableLabel;
        private final ImageView iconView;
        private final TextView appLabelView;
        private String rowPackageName;

        AppRowHolder(final View row) {
            super(row);

            iconView = (ImageView) row.findViewById(R.id.iconView);
            appLabelView = (TextView) row.findViewById(R.id.appLabel);
            editableLabel = (TextView) row.findViewById(R.id.editableLabel);
            onSwitch = (Switch) row.findViewById(R.id.on_switch);
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestPackageName = rowPackageName;
                    Intent intent = new Intent(Panic.ACTION_CONNECT);
                    intent.setPackage(requestPackageName);
                    startActivityForResult(intent, CONNECT_RESULT);
                }
            };

            onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean enabled) {
                    setEnabled(enabled);
                    if (enabled) {
                        PanicTrigger.enableResponder(activity, rowPackageName);
                    } else {
                        PanicTrigger.disableResponder(activity, rowPackageName);
                    }
                }
            });
        }

        void setEnabled(boolean enabled) {
            if (enabled) {
                editableLabel.setVisibility(View.VISIBLE);
                appLabelView.setEnabled(true);
                iconView.setEnabled(true);
                iconView.setColorFilter(null);
            } else {
                editableLabel.setVisibility(View.GONE);
                appLabelView.setEnabled(false);
                iconView.setEnabled(false);
                // grey out app icon when disabled
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                iconView.setColorFilter(filter);
            }
        }

        void setupForApp(String packageName, Drawable icon, CharSequence appLabel, boolean editable) {
            this.rowPackageName = packageName;
            iconView.setImageDrawable(icon);
            appLabelView.setText(appLabel);
            if (editable) {
                iconView.setOnClickListener(onClickListener);
                appLabelView.setOnClickListener(onClickListener);
                editableLabel.setOnClickListener(onClickListener);
                editableLabel.setText(R.string.edit);
                editableLabel.setTypeface(null, Typeface.BOLD);
                if (Build.VERSION.SDK_INT >= 14)
                    editableLabel.setAllCaps(true);
            } else {
                iconView.setOnClickListener(null);
                appLabelView.setOnClickListener(null);
                editableLabel.setOnClickListener(null);
                editableLabel.setText(R.string.app_hides);
                editableLabel.setTypeface(null, Typeface.NORMAL);
                if (Build.VERSION.SDK_INT >= 14)
                    editableLabel.setAllCaps(false);
            }
            boolean enabled = enabledResponders.contains(packageName);
            if (Build.VERSION.SDK_INT >= 14)
                onSwitch.setChecked(enabled);
            setEnabled(enabled);
        }
    }

}
