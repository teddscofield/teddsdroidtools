package tss.droidtools;

import tss.droidtools.phone.Hc;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	protected SharedPreferences p;
	protected Boolean debugOn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		p = getSharedPreferences(Hc.PREFSNAME, 0);
		debugOn = Hc.debugEnabled(getApplicationContext());
	}
	
	protected void debugLog(String extra,String s) {
		if (debugOn) Log.d(Hc.LOG_TAG, Hc.PRE_TAG + extra + Hc.POST_TAG + " "+ s);
	}
	protected void errorLog(String extra,String s) {
		Log.e(Hc.LOG_TAG, Hc.PRE_TAG + extra + Hc.POST_TAG + " "+ s);
	}
}
