package com.apb.beacon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseFragmentActivity extends FragmentActivity {

	public void callFinishActivityReceivier() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.apb.beacon.ACTION_LOGOUT");
		registerReceiver(activityFinishReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(activityFinishReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	BroadcastReceiver activityFinishReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.apb.beacon.ACTION_LOGOUT")) {
//				Log.d("MainActivity.onReceive", "Logout in progress");
				finish();
			}
		}
	};

}
