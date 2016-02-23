package org.iilab.pb;


import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.iilab.pb.common.MyTagHandler;
import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.fragment.LanguageSettingsFragment;
import org.iilab.pb.fragment.SetupCodeFragment;
import org.iilab.pb.fragment.SetupContactsFragment;
import org.iilab.pb.fragment.SetupMessageFragment;
import org.iilab.pb.fragment.SimpleFragment;
import org.iilab.pb.fragment.WarningFragment;
import org.iilab.pb.fragment.WizardAlarmTestDisguiseFragment;
import org.iilab.pb.fragment.WizardAlarmTestHardwareFragment;
import org.iilab.pb.fragment.WizardTestDisguiseCodeFragment;
import org.iilab.pb.fragment.WizardTestDisguiseOpenFragment;
import org.iilab.pb.fragment.WizardTestDisguiseUnlockFragment;
import org.iilab.pb.model.Page;
import org.iilab.pb.trigger.HardwareTriggerService;

import java.util.HashMap;
import java.util.Map;

import static org.iilab.pb.common.AppConstants.FROM_WIZARD_ACTIVITY;
import static org.iilab.pb.common.AppConstants.IS_BACK_BUTTON_PRESSED;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_ALARM_TEST_DISGUISE;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_ALARM_TEST_HARDWARE;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_CODE;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_CONTACTS;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_DISGUISE_TEST_CODE;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_DISGUISE_TEST_OPEN;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_DISGUISE_TEST_UNLOCK;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_LANGUAGE;
import static org.iilab.pb.common.AppConstants.PAGE_COMPONENT_MESSAGE;
import static org.iilab.pb.common.AppConstants.PAGE_FROM_NOT_IMPLEMENTED;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED_ALARM;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED_DISGUISE;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_READY;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_TEST_HARDWARE;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_TEST_HARDWARE_RETRAINING;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS_RETRAINING;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS_TRAINING_1_5;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_TEST_HARDWARE_TRAINING_1_5;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_WARNING;
import static org.iilab.pb.common.AppConstants.PAGE_TYPE_SIMPLE;
import static org.iilab.pb.common.AppConstants.PAGE_TYPE_WARNING;
import static org.iilab.pb.common.AppConstants.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_READY;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_SETUP_WARNING;
import static org.iilab.pb.common.AppUtil.checkAndRequestPermissions;
import static org.iilab.pb.common.ApplicationSettings.getSelectedLanguage;
import static org.iilab.pb.common.ApplicationSettings.getWizardState;
import static org.iilab.pb.common.ApplicationSettings.isHardwareTriggerServiceEnabled;
import static org.iilab.pb.common.ApplicationSettings.setWizardState;


public class WizardActivity extends BaseFragmentActivity {

    Page currentPage;
    String pageId;
    String selectedLang;

    TextView tvToastMessage;
    Boolean flagRiseFromPause = false;

    private Handler inactiveHandler = new Handler();
    private static final String TAG = WizardActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_layout);

        tvToastMessage = (TextView) findViewById(R.id.tv_toast);

        try {
            pageId = getIntent().getExtras().getString(PAGE_ID);
            Log.d(TAG, "pageId is = " + pageId);
            if (pageId == null)
                pageId = PAGE_HOME_NOT_CONFIGURED;

        } catch (Exception e) {
            pageId = PAGE_HOME_NOT_CONFIGURED;
            e.printStackTrace();
        }
        selectedLang = getSelectedLanguage(this);

        Log.d(TAG, "pageId = " + pageId);

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, selectedLang);
        dbInstance.close();

        if (currentPage == null) {
            Log.d(TAG, "page = null");
            Toast.makeText(this, "Still to be implemented.", Toast.LENGTH_SHORT).show();
            PAGE_FROM_NOT_IMPLEMENTED = true;
            finish();
            return;
        } else if (currentPage.getId().equals(PAGE_HOME_READY)) {
            setWizardState(WizardActivity.this, WIZARD_FLAG_HOME_READY);
            changeAppIconToCalculator();
            if (isHardwareTriggerServiceEnabled(this)) {
                startService(new Intent(this, HardwareTriggerService.class));
            }
            Intent i = new Intent(WizardActivity.this, MainActivity.class);
            i.putExtra(PAGE_ID, pageId);
            startActivity(i);

            callFinishActivityReceiver();

            finish();
            return;
        } else {

            /*
            setup the milestone of the wizard state if the app flow reaches to THREE specific page,
            i.e. home-not-configured, home-not-configured-alarm and home-not-configured-disguise.
             */
            if (currentPage.getId().equals(PAGE_HOME_NOT_CONFIGURED)) {
                setWizardState(WizardActivity.this, WIZARD_FLAG_HOME_NOT_CONFIGURED);
            } else if (currentPage.getId().equals(PAGE_HOME_NOT_CONFIGURED_ALARM)) {
                setWizardState(WizardActivity.this, WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM);
            } else if (currentPage.getId().equals(PAGE_HOME_NOT_CONFIGURED_DISGUISE)) {
                setWizardState(WizardActivity.this, WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment fragment = null;

            if (currentPage.getType().equals(PAGE_TYPE_SIMPLE)) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new SimpleFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
            } else if (currentPage.getType().equals(PAGE_TYPE_WARNING)) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new WarningFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
            } else {          // type = interactive
                if (currentPage.getComponent().equals(PAGE_COMPONENT_CONTACTS))
                    fragment = new SetupContactsFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals(PAGE_COMPONENT_MESSAGE))
                    fragment = new SetupMessageFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals(PAGE_COMPONENT_CODE))
                    fragment = new SetupCodeFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals(PAGE_COMPONENT_LANGUAGE))
                    fragment = new LanguageSettingsFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
                else if (currentPage.getComponent().equals(PAGE_COMPONENT_ALARM_TEST_HARDWARE)) {
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if (currentPage.getIntroduction() != null) {
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new WizardAlarmTestHardwareFragment().newInstance(pageId);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                } else if (currentPage.getComponent().equals(PAGE_COMPONENT_ALARM_TEST_DISGUISE)) {
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if (currentPage.getIntroduction() != null) {
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new WizardAlarmTestDisguiseFragment().newInstance(pageId);
                } else if (currentPage.getComponent().equals(PAGE_COMPONENT_DISGUISE_TEST_OPEN)) {
                    findViewById(R.id.wizard_layout_root).setBackgroundColor(Color.BLACK);
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if (currentPage.getIntroduction() != null) {
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new WizardTestDisguiseOpenFragment().newInstance(pageId);
                } else if (currentPage.getComponent().equals(PAGE_COMPONENT_DISGUISE_TEST_UNLOCK)) {
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if (currentPage.getIntroduction() != null) {
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }

                    fragment = new WizardTestDisguiseUnlockFragment().newInstance(pageId);
                } else if (currentPage.getComponent().equals(PAGE_COMPONENT_DISGUISE_TEST_CODE)) {
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if (currentPage.getIntroduction() != null) {
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new WizardTestDisguiseCodeFragment().newInstance(pageId);
                }
//                //TODO remove this, just for testing
//                else if (currentPage.getComponent().equals(PAGE_COMPONENT_ADVANCED_SETTINGS)) {
//                    fragment = new AdvancedSettingsFragment().newInstance(pageId, FROM_MAIN_ACTIVITY);
//                }
                else
                    fragment = new SimpleFragment().newInstance(pageId, FROM_WIZARD_ACTIVITY);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    private void changeAppIconToCalculator() {
        Log.i(TAG, "changeAppIconToCalculator");

        getPackageManager().setComponentEnabledSetting(
                new ComponentName("org.iilab.pb", "org.iilab.pb.HomeActivity-calculator"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        getPackageManager().setComponentEnabledSetting(
                new ComponentName("org.iilab.pb", "org.iilab.pb.HomeActivity-setup"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "page = " + pageId);

//        if (currentPage.getId().equals("home-ready") && isRestartedSetup(WizardActivity.this)) {
//            Log.e("WizardActivity.onPause", "false->RestartedSetup");
//            setRestartedSetup(WizardActivity.this, false);
//        }

        /*
        It's part of setup-alarm-test-hardware page testing to press power button consecutively 5 times that calls the activity's onPause method every time
        the device goes locked & onResume method every time the device is unlocked. That's why we omit this page for this below condition, otherwise every time
        device goes to pause state by pressing power button, flagRiseFromPause becomes true & display home-not-configured-alarm page, thus our test can't be successful.

        In short, we block this page - setup-alarm-test-hardware for pause-resume action
         */
        if (!(pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE) || pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE_RETRAINING) || pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE_TRAINING_1_5))) {
            Log.d(TAG, "assert flagRiseFromPause = " + true);
            flagRiseFromPause = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "page = " + pageId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        inactiveHandler.removeCallbacks(runnableInteractive);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "page = " + pageId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "pageId = " + pageId + " and flagRiseFromPause = " + flagRiseFromPause);

        int wizardState = getWizardState(WizardActivity.this);

        /*
        Check-1
        if this page is resumed from the page still not implemented, then we'll handle it here.
        If we don't do this check, then the resume procedure falls under Check-3 & execute that code snippet, which is not proper.
         */
        if (PAGE_FROM_NOT_IMPLEMENTED) {
            Log.d(TAG, "returning from not-implemented page.");
            PAGE_FROM_NOT_IMPLEMENTED = false;
            return;
        }

        /*
        Check-2
        if this page is resumed by navigating-back from the next page, then we'll handle it here.
        If we don't do this check, then the resume procedure falls under Check-3 & execute that code snippet, which is not proper.
         */
        if (IS_BACK_BUTTON_PRESSED) {
            Log.d(TAG, "back button pressed");
            IS_BACK_BUTTON_PRESSED = false;
            return;
        }

        /*
        Check-3
        The below code snippet partitions the wizard into 3 check-point, so if we pass the latest one, that will be marked as the opening page of the app
        if we closes the app at some point of wizard-configuration & resume later. We have 2 conditions to make this code executed -

        1. flagRiseFromPause -
        Every time device gets resumed, flagRiseFromPause becomes true(except for setup-alarm-test-hardware) & we only want this part of code executed in that case.
        if we don't user flagRiseFromPause flag, every-time activity comes to onResume, this code executes, calls another activity, which will make an infinite loop.

        2. !pageId.equals("setup-alarm-test-hardware-success") -
        It's part of setup-alarm-test-hardware page test to press power button consecutively 5 times that calls the activity's onPause
        method every time the device goes locked & onResume method every time the device is unlocked. setup-alarm-test-hardware-success
        comes when we successfully test this scenario.
        So setup-alarm-test-hardware-success always comes with flagRiseFromPause = true, so we have to handle this page explicitly so that app shouldn't
        go to the mile-stone page every time this page loads.
        Side Effect - if we are in this page - setup-alarm-test-hardware-success & go to home & come back, still this page will be there as the
        opening page of the app.
         */
        if (flagRiseFromPause && !(pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS) || pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS_RETRAINING) || (pageId.equals(PAGE_SETUP_ALARM_TEST_HARDWARE_SUCCESS_TRAINING_1_5)))) {
            flagRiseFromPause = false;

            if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED) {
                pageId = PAGE_HOME_NOT_CONFIGURED;
            } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM) {
                pageId = PAGE_HOME_NOT_CONFIGURED_ALARM;
            } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE) {
                pageId = PAGE_HOME_NOT_CONFIGURED_DISGUISE;
            } else if (wizardState == WIZARD_FLAG_HOME_READY) {
                pageId = PAGE_HOME_READY;
            } else if (wizardState == WIZARD_FLAG_SETUP_WARNING) {
                pageId = PAGE_SETUP_WARNING;
            }

            Intent i = new Intent(WizardActivity.this, WizardActivity.class);
            i.putExtra(PAGE_ID, pageId);
            startActivity(i);

            callFinishActivityReceiver();
            finish();
        }
    }

    @Override
    public void onUserInteraction() {
        Log.i(TAG, "onUserInteraction");
        super.onUserInteraction();
        hideToastMessageInInteractiveFragment();
        if (currentPage != null && currentPage.getComponent() != null &&
                (
                        currentPage.getComponent().equals(PAGE_COMPONENT_ALARM_TEST_HARDWARE)
                                || currentPage.getComponent().equals(PAGE_COMPONENT_ALARM_TEST_DISGUISE)
                                || currentPage.getComponent().equals(PAGE_COMPONENT_DISGUISE_TEST_OPEN)
                                || currentPage.getComponent().equals(PAGE_COMPONENT_DISGUISE_TEST_UNLOCK)
                                || currentPage.getComponent().equals(PAGE_COMPONENT_DISGUISE_TEST_CODE)
                )
                ) {
            inactiveHandler.postDelayed(runnableInteractive, Integer.parseInt(currentPage.getTimers().getFail()) * 1000);
        }
    }

    public void hideToastMessageInInteractiveFragment() {
        tvToastMessage.setVisibility(View.INVISIBLE);
    }


    public void confirmationToastMessage() {
        tvToastMessage.setText(Html.fromHtml(getApplication().getString(R.string.confirmation_message), null, new MyTagHandler()));
        tvToastMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IS_BACK_BUTTON_PRESSED = true;
    }


    private Runnable runnableInteractive = new Runnable() {
        public void run() {

            String pageId = currentPage.getFailedId();

            Intent i = new Intent(WizardActivity.this, WizardActivity.class);
            i.putExtra(PAGE_ID, pageId);
            startActivity(i);
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called---------------------------------------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.

                Map<String, Integer> perms = new HashMap<>();
                // Initialize
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for ACCESS_FINE_LOCATION
                    if (perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        setWizardState(this, WIZARD_FLAG_SETUP_WARNING);

                    } else {

                        Log.d(TAG, "Some permissions are not granted ask again");
                        //permission is denied (never ask again is not checked) so ask again
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            flagRiseFromPause = false;
                            showRationaleForPermissions(getString(R.string.PermissionDenied),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Toast.makeText(WizardActivity.this, R.string.EnablePermissionsMessage, Toast.LENGTH_LONG)
                                                            .show();
                                                    Intent i = new Intent(WizardActivity.this, WizardActivity.class);
                                                    i.putExtra(PAGE_ID, PAGE_SETUP_WARNING);
                                                    startActivity(i);
                                                    break;
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions(WizardActivity.this);
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied  (never ask again is  checked), proceed with setup
                        else {
                            Toast.makeText(this, R.string.EnablePermissionsMessage, Toast.LENGTH_LONG)
                                    .show();
                            setWizardState(this, WIZARD_FLAG_SETUP_WARNING);
                        }
                    }
                }
            }
        }
    }
    private void showRationaleForPermissions(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(R.string.OKButton, okListener)
                .setNegativeButton(R.string.CancelButton, okListener)
                .create()
                .show();
    }
}
