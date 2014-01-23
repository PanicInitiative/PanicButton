package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.R;
import com.apb.beacon.adapter.PageCheckListAdapter;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;


/**
 * Created by aoe on 1/16/14.
 */
public class WizardModalActivity extends Activity {

    Page currentPage;
    PageCheckListAdapter pageCheckListAdapter;

    LinearLayout llWarning, llStatus;
    TextView tvTitle, tvContent, tvIntro, tvWarning, tvStatus;
    ListView checkList;
    Button bAction1, bAction2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_modal);

        tvTitle = (TextView) findViewById(R.id.fragment_title);
        tvIntro = (TextView) findViewById(R.id.fragment_intro);
        tvContent = (TextView) findViewById(R.id.fragment_contents);

        llWarning = (LinearLayout) findViewById(R.id.ll_fragment_warning);
        tvWarning  = (TextView) findViewById(R.id.fragment_warning);

        llStatus = (LinearLayout) findViewById(R.id.ll_fragment_status);
        tvStatus = (TextView) findViewById(R.id.fragment_status);

        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pageId = currentPage.getStatus().get(0).getLink();

                Intent i = new Intent(WizardModalActivity.this, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
            }
        });

        bAction1 = (Button) findViewById(R.id.b_action1);
        bAction2 = (Button) findViewById(R.id.b_action2);

        checkList = (ListView) findViewById(R.id.lv_checklist);

        String pageId = getIntent().getExtras().getString("page_id");
        String defaultLang = "en";

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, defaultLang);
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

            if(currentPage.getAction().get(1).getStatus() != null && currentPage.getAction().get(1).getStatus().equals("disabled"))
                bAction2.setEnabled(false);

            bAction2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pageId = currentPage.getAction().get(1).getLink();

                    if (pageId.equals("close")) {
                        ApplicationSettings.completeFirstRun(WizardModalActivity.this);

                        getPackageManager().setComponentEnabledSetting(
                                new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-calculator"),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        getPackageManager().setComponentEnabledSetting(
                                new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-setup"),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                        Intent i = new Intent(WizardModalActivity.this, CalculatorActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);

                        finish();
                    } else {
                        finish();
                    }
                }
            });
        } else {
            bAction2.setVisibility(View.GONE);
        }

        if (currentPage.getAction().size() > 0) {
            bAction1.setText(currentPage.getAction().get(0).getTitle());

            if(currentPage.getAction().get(0).getStatus() != null && currentPage.getAction().get(0).getStatus().equals("disabled"))
                bAction2.setEnabled(false);

            bAction1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pageId = currentPage.getAction().get(0).getLink();

                    if (pageId.equals("close")) {
                        ApplicationSettings.completeFirstRun(WizardModalActivity.this);

                        getPackageManager().setComponentEnabledSetting(
                                new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-calculator"),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                        getPackageManager().setComponentEnabledSetting(
                                new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-setup"),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

                        Intent i = new Intent(WizardModalActivity.this, CalculatorActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
                        sendBroadcast(broadcastIntent);

                        finish();
                    } else {
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
    }

}
