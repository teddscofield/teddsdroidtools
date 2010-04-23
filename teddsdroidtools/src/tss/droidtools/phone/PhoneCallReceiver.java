package tss.droidtools.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
*
* Broadcast receiver that reacts to an incoming call and starts up the
* activity which will allow the user to answer the call with the camera
* button.
*
* credit goes to the following open source projects for showing a guy 
* like me how this technique works.  They're the smart ones who were cool
* enough to share the technique via an open source app:
* 
* MyLock - http://code.google.com/p/mylockforandroid/
* auto-answer - http://code.google.com/p/auto-answer/		
* 
* @author tedd
*
*/
public class PhoneCallReceiver extends BroadcastReceiver {
	
	private Handler sh;
	private Context c;
	private CreateCallAnswerActivityTask t = new CreateCallAnswerActivityTask();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		c = context;
		SharedPreferences p = c.getSharedPreferences(Hc.PREFSNAME,0);
		
		if (!p.getBoolean("enabled", false)) {				logMe("feature disabled.");
			return;
		} 
		else												logMe("feature enabled, checking call state");

		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		
		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) 
		{													logMe("EXTRA_STATE_RINGING phone state received, scheduling delayed creation of the CallAnswerActivity creation task.");
			sh = new Handler();
			sh.postDelayed(t, Hc.STARTUP_DELAY);
		}
		else												logMe("unknown phone state received: "+phone_state);

	}
	
	class CreateCallAnswerActivityTask implements Runnable {

		@Override
		public void run() {									logMe("CreateCallAnswerActivityTask#run call back called");

			Intent callAnswerActivity = new Intent();
    		callAnswerActivity.setClassName("tss.droidtools.phone","tss.droidtools.phone.CallAnswerActivity");
    		callAnswerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
    		
    														logMe("starting CallAnswerActivity");
        	c.startActivity(callAnswerActivity);
        	sh.removeCallbacks(t);
        	sh = null;
		}
	}
	
	private void logMe(String s) {
		if (Hc.DBG) Log.d(Hc.PRE_TAG + "PhoneCallReceiver" + Hc.POST_TAG,Hc.LOG_FLUFF + s);
	}
}
