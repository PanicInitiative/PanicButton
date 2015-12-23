package org.iilab.pb.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.iilab.pb.R;
import org.iilab.pb.adapter.PageItemAdapter;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

import java.util.List;

public class PanicRespondersFragment extends Fragment {

    private static final String PAGE_ID = "page_id";
    private static final String PARENT_ACTIVITY = "parent_activity";
    private Activity activity;
    DisplayMetrics metrics;
    TextView tvTitle, tvIntro;
    Page currentPage;
    PageItemAdapter pageItemAdapter;

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

}
