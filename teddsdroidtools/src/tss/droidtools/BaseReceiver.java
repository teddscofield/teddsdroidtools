package tss.droidtools;

import tss.droidtools.phone.Hc;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BaseReceiver extends BroadcastReceiver {
	protected Boolean debugOn;
	
	@Override
	public void onReceive(Context c, Intent i) {
		throw new UnsupportedOperationException();
	}

	protected void logMe(String extra, String s) {
		if (debugOn) Log.d(Hc.LOG_TAG, Hc.PRE_TAG + extra + Hc.POST_TAG + " "+ s);
	}	
}
