package org.iilab.pb;

import org.iilab.pb.adapter.PageCheckListAdapter;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



/**
 * Created by aoe on 1/16/14.
 */
public class WizardModalActivity extends BaseFragmentActivity {

    Page currentPage;
    PageCheckListAdapter pageCheckListAdapter;
    int parentActivity;

    DisplayMetrics metrics;

    LinearLayout llWarning, llStatus;
    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
    ListView checkList;
    Button bAction1, bAction2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_modal);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        tvTitle = (TextView) findViewById(R.id.fragment_title);
        tvIntro = (TextView) findViewById(R.id.fragment_intro);
        tvContent = (TextView) findViewById(R.id.fragment_contents);

        llWarning = (LinearLayout) findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) findViewById(R.id.fragment_warning);

        llStatus = (LinearLayout) findViewById(R.id.ll_fragment_status);
        tvStatus = (TextView) findViewById(R.id.fragment_status);

        bAction1 = (Button) findViewById(R.id.b_action1);
        bAction2 = (Button) findViewById(R.id.b_action2);

        checkList = (ListView) findViewById(R.id.lv_checklist);

        String pageId = getIntent().getExtras().getString("page_id");
        String selectedLang = ApplicationSettings.getSelectedLanguage(this);
        parentActivity = getIntent().getExtras().getInt("parent_activity");

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, selectedLang);
        dbInstance.close();

        tvTitle.setText(currentPage.getTitle());

        if(currentPage.getStatus() == null || currentPage.getStatus().size() == 0)
            llStatus.setVisibility(View.GONE);
        else{
            String color = currentPage.getStatus().get(0).getColor();
            if(color.equals("red"))
                tvStatus.setTextColor(Color.RED);
            else
                tvStatus.setTextColor(Color.GREEN);
            tvStatus.setText(currentPage.getStatus().get(0).getTitle());
        }

        if(currentPage.getIntroduction() == null)
            tvIntro.setVisibility(View.GONE);
        else
            tvIntro.setText(currentPage.getIntroduction());

        if(currentPage.getWarning() == null)
            llWarning.setVisibility(View.GONE);
        else
            tvWarning.setText(currentPage.getWarning());

        if(currentPage.getContent() == null)
            tvContent.setVisibility(View.GONE);
        else
            tvContent.setText(Html.fromHtml(currentPage.getContent(), null, new MyTagHandler()));

        if (currentPage.getAction().size() > 1) {
            bAction2.setText(currentPage.getAction().get(1).getTitle());

            bAction2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String pageId = currentPage.getAction().get(1).getLink();

                    if (pageId.equals("close")) {
/*
                        if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
//                            ApplicationSettings.setFirstRun(WizardModalActivity.this, false);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("org.iilab.pb", "org.iilab.pb.HomeActivity-calculator"),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("org.iilab.pb", "org.iilab.pb.HomeActivity-setup"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        }
*/
                        Intent i = new Intent(WizardModalActivity.this, CalculatorActivity.class);
                        i = AppUtil.clearBackStack(i);
                        startActivity(i);
                        overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

                        callFinishActivityReceiver();

                        finish();
                    } else {
                        if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
//                            ApplicationSettings.setFirstRun(WizardModalActivity.this, true);
                            AppConstants.IS_BACK_BUTTON_PRESSED = true;
                        }
                        finish();
                    }
                }
            });
        } else {
            bAction2.setVisibility(View.GONE);
        }

        if (currentPage.getAction().size() > 0) {
            bAction1.setText(currentPage.getAction().get(0).getTitle());

            bAction1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pageId = currentPage.getAction().get(0).getLink();

                    if (pageId.equals("close")) {

/*                        if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
//                            ApplicationSettings.setFirstRun(WizardModalActivity.this, false);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("org.iilab.pb", "org.iilab.pb.HomeActivity-calculator"),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                            getPackageManager().setComponentEnabledSetting(
                                    new ComponentName("org.iilab.pb", "org.iilab.pb.HomeActivity-setup"),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                        }
*/
                        Intent i = new Intent(WizardModalActivity.this, CalculatorActivity.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i = AppUtil.clearBackStack(i);
                        startActivity(i);
                        overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

                        callFinishActivityReceiver();

                        finish();
                    } else {
                        if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
//                            ApplicationSettings.setFirstRun(WizardModalActivity.this, true);
                            AppConstants.IS_BACK_BUTTON_PRESSED = true;
                        }
                        finish();
                    }
                }
            });
        } else {
            bAction1.setVisibility(View.GONE);
        }

        pageCheckListAdapter = new PageCheckListAdapter(this, null);
        checkList.setAdapter(pageCheckListAdapter);
        pageCheckListAdapter.setData(currentPage.getChecklist());

        AppUtil.updateImages(true, currentPage.getContent(), WizardModalActivity.this, metrics, tvContent, AppConstants.IMAGE_INLINE);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (parentActivity == AppConstants.FROM_WIZARD_ACTIVITY) {
//            ApplicationSettings.setFirstRun(WizardModalActivity.this, true);
            AppConstants.IS_BACK_BUTTON_PRESSED = true;
        }
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
        callFinishActivityReceiver();
    	finish();
    }

}
