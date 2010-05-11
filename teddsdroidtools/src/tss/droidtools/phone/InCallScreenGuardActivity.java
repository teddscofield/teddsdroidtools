package tss.droidtools.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import tss.droidtools.BaseActivity;

public class InCallScreenGuardActivity extends BaseActivity {
	BroadcastReceiver r;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReciever();
		setContentView(R.layout.incallscreenguard);
		Button exitScreenGuard = (Button) findViewById(R.id.exitScreenGuard);
		exitScreenGuard.setOnLongClickListener(new ExitScreenGuardLongClickListener());
	}
	
	public void onResume() {
		super.onResume();
		debugLog("Resume");
	}
	
	
	public void onPause() {
		super.onPause();

		debugLog("Paused");
	}
	public void onStop() {
		super.onStop();
		debugLog("Stopped!");
	}
	
	public void onDestroy() {
		debugLog("destroyed!");
		unHookReceiver();
		super.onDestroy();
	}
	private class ExitScreenGuardLongClickListener implements OnLongClickListener {
		@Override public boolean onLongClick(View v){
			debugLog("exit screen guard button onClick event");
			moveTaskToBack(true);
			//finish();
			return true;
		}		
	}	
	
	// ------------------------------------------------------------------------
	// broadcast receivers
	// ------------------------------------------------------------------------

	/**
	 * register phone state receiver 
	 */
	private void registerReciever() {
		if (r != null) return;

		r = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) 	{
				String phone_state = i.getStringExtra(TelephonyManager.EXTRA_STATE);
				if (!phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) 
				{
					debugLog("received "+phone_state+", time to go bye bye, thanks for playing!");
					finish();
				}
			} 
		};

		registerReceiver(r, new IntentFilter("android.intent.action.PHONE_STATE"));
	}

	/**
	 * unregister phone state receiver 
	 */
	private void unHookReceiver() {
		if (r != null) 
		{
			unregisterReceiver(r);
			r = null;
		}
	}
	
	private void debugLog(String s) {
		debugLog("InCallScreenGuardActivity",s);
	}
}
