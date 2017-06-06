package org.iilab.pb.fragment;

import org.iilab.pb.R;
import org.iilab.pb.adapter.PageLanguageSettingsAdapter;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Created by aoe on 2/25/14.
 */
public class LanguageSettingsFragment extends Fragment {

	private static final String PAGE_ID = "page_id";
    private static final String PARENT_ACTIVITY = "parent_activity";
    private Activity activity;

    TextView tvTitle, tvContent, tvIntro;
    ListView lvActions;

    Page currentPage;
    PageLanguageSettingsAdapter pageLanguageSettingsAdapter;


    public static LanguageSettingsFragment newInstance(String pageId, int parentActivity) {
        LanguageSettingsFragment f = new LanguageSettingsFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_language_settings, container, false);

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        lvActions = (ListView) view.findViewById(R.id.fragment_action_list);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        if (activity != null) {

            String pageId = getArguments().getString(PAGE_ID);
            int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if (currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));
//                tvContent.setText(currentPage.getContent());

            if (currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());


            pageLanguageSettingsAdapter = new PageLanguageSettingsAdapter(activity, parentActivity);
            lvActions.setAdapter(pageLanguageSettingsAdapter);

            pageLanguageSettingsAdapter.setData(currentPage.getAction());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause LanguageSettingsFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(">>>>>>>>>>", "onStop LanguageSettingsFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(">>>>>>>>>>", "onStart LanguageSettingsFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(">>>>>>>>>>", "onResume LanguageSettingsFragment");
    }
}
