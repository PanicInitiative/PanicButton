package org.iilab.pb.fragment;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.adapter.PageItemAdapter;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.model.PageItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainSetupAlertFragment extends Fragment implements OnClickListener{

    private static final int EXACT_CHARACTERS = 4;

    private TextView alertDelayEditText;

    private static final String PAGE_ID = "page_id";
    private static final String PARENT_ACTIVITY = "parent_activity";
    private Activity activity;
    private String[] time_options = 
    		{"1", "2" , "3", "4", "5" , "6", "7", "8", "9" , "10",
    		 "11", "12" , "13", "14", "15" , "16", "17", "18", "19" , "20",
    		 "21", "22" , "23", "24", "25" , "26", "27", "28", "29" , "30",
    		 "31", "32" , "33", "34", "35" , "36", "37", "38", "39" , "40",
    		 "41", "42" , "43", "44", "45" , "46", "47", "48", "49" , "50",
    		 "51", "52" , "53", "54", "55" , "56", "57", "58", "59" , "60",
    		 "61", "62" , "63", "64", "65" , "66", "67", "68", "69" , "70",
    		 "71", "72" , "73", "74", "75" , "76", "77", "78", "79" , "80",
    		 "81", "82" , "83", "84", "85" , "86", "87", "88", "89" , "90",
    		 "91", "92" , "93", "94", "95" , "96", "97", "98", "99" , "100",
    		 "101", "102" , "103", "104", "105" , "106", "107", "108", "109" , "110",
    		 "111", "112" , "113", "114", "115" , "116", "117", "118", "119" , "120",
    		};
    AlertDialog actions;

    TextView tvTitle, tvContent, tvIntro, tvWarning;
    Button bAction;
    ListView lvItems;
    LinearLayout llWarning;

    Page currentPage;
    PageItemAdapter pageItemAdapter;

    public static MainSetupAlertFragment newInstance(String pageId, int parentActivity) {
        MainSetupAlertFragment f = new MainSetupAlertFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return(f);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_interactive_alert, container, false);
        alertDelayEditText = (TextView) view.findViewById(R.id.alertDelay_edittext);
        alertDelayEditText.setText(String.valueOf(ApplicationSettings.getAlertDelay(getActivity()))+" min");
        
        delayDialogSettings();
        
        alertDelayEditText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actions.show();
			}
		});

        tvTitle = (TextView) view.findViewById(R.id.fragment_title);
        tvIntro = (TextView) view.findViewById(R.id.fragment_intro);
        tvContent = (TextView) view.findViewById(R.id.fragment_contents);

        bAction = (Button) view.findViewById(R.id.fragment_action);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(">>>>", "action button pressed");
                ApplicationSettings.setAlertDelay(getActivity(), ApplicationSettings.getAlertDelay(getActivity()));

                
                String pageId = currentPage.getAction().get(0).getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
                    i = new Intent(activity, WizardActivity.class);
                } else{
//                	AppUtil.showToast("New frequency saved.", 1000, activity);
                    String confirmation = (currentPage.getAction().get(0).getConfirmation() == null)
                            ? AppConstants.DEFAULT_CONFIRMATION_MESSAGE
                            : currentPage.getAction().get(0).getConfirmation();
                    Toast.makeText(activity, confirmation, Toast.LENGTH_SHORT).show();

                    i = new Intent(activity, MainActivity.class);
                }

//                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);

                if(parentActivity == AppConstants.FROM_MAIN_ACTIVITY){
                    activity.finish();
                }
            }
        });


        lvItems = (ListView) view.findViewById(R.id.fragment_item_list);

        llWarning = (LinearLayout) view.findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) view.findViewById(R.id.fragment_warning);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageItem selectedItem = (PageItem) parent.getItemAtPosition(position);

                String pageId = selectedItem.getLink();
                int parentActivity = getArguments().getInt(PARENT_ACTIVITY);
                Intent i;

                if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
                    i = new Intent(activity, WizardActivity.class);
                } else{
                    i = new Intent(activity, MainActivity.class);
                }

//                Intent i = new Intent(activity, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);

            }
        });

        return view;
    }

    
	public void delayDialogSettings() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Choose an Option");
	    builder.setItems(time_options, actionListener);
	    builder.setNegativeButton("Cancel", null);
	    actions = builder.create();
		
	}
	
	DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    	  ApplicationSettings.setAlertDelay(getActivity(), which+1);
	    	  alertDelayEditText.setText(time_options[which].toString()+" min");
	    }
	  };
	

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        if (activity != null) {
//            bAction.setEnabled(isComplete());

            String pageId = getArguments().getString(PAGE_ID);
            String selectedLang = ApplicationSettings.getSelectedLanguage(activity);

            PBDatabase dbInstance = new PBDatabase(activity);
            dbInstance.open();
            currentPage = dbInstance.retrievePage(pageId, selectedLang);
            dbInstance.close();

            tvTitle.setText(currentPage.getTitle());

            if(currentPage.getContent() == null)
                tvContent.setVisibility(View.GONE);
            else
                tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));

            if(currentPage.getIntroduction() == null)
                tvIntro.setVisibility(View.GONE);
            else
                tvIntro.setText(currentPage.getIntroduction());

            if(currentPage.getWarning() == null)
                llWarning.setVisibility(View.GONE);
            else
                tvWarning.setText(currentPage.getWarning());

            bAction.setText(currentPage.getAction().get(0).getTitle());

            pageItemAdapter = new PageItemAdapter(activity, null);
            lvItems.setAdapter(pageItemAdapter);
            pageItemAdapter.setData(currentPage.getItems());

        }

    }


    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
