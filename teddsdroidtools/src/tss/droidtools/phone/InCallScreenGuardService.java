package tss.droidtools.phone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class InCallScreenGuardService extends Service {

	private BroadcastReceiver r;
	public void onStart(Intent i, int id) {

		debugLog("onStart");
		registerScreenOffReceiver();
		// pause
		try {
			Long delay = (Long)i.getExtras().get("delay");
			debugLog("pausing for "+delay+" millis");
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		debugLog("now starting screen guard");
		startScreenGuardActivity(getBaseContext());
	}
	
	private void registerScreenOffReceiver() {
		if (r == null) {
			debugLog("registering screen off receiver");
			r = new BroadcastReceiver() {
				@Override
				public void onReceive(Context c, Intent iR) {
					debugLog("received this intent: "+iR);
					startScreenGuardActivity(c);
				};
			};
			registerReceiver(r, new IntentFilter("android.intent.action.SCREEN_OFF"));
		}	
	}
	
	private void startScreenGuardActivity(Context c) {
		Intent i = new Intent(c,InCallScreenGuardActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION );
		debugLog("starting screen guard");
		startActivity(i);
		
	}
	
	
	public void onDestroy() {
		super.onDestroy();
		debugLog("onDestroy");
		if (r != null)
			unregisterReceiver(r);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void debugLog(String s) {
		Log.d(Hc.LOG_TAG,">InCallScreenGuardService<"+s);
	}
}
