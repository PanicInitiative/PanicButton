package com.apb.beacon.wizard;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.R;
import com.apb.beacon.adapter.PageCheckListAdapter;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;


/**
 * Created by aoe on 1/16/14.
 */
public class WizardModalActivity extends Activity {

    Page currentPage;
    PageCheckListAdapter pageCheckListAdapter;

    Button bAction1, bAction2;
    ListView checkList;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_modal);

        tvTitle = (TextView) findViewById(R.id.fragment_title);
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

        if (currentPage.getAction().size() > 1) {
            bAction2.setText(currentPage.getAction().get(1).getTitle());
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
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
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
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);
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
