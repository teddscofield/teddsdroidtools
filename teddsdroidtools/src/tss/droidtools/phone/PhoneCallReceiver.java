package tss.droidtools.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

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

	//private static final String TAG = "PhoneCallReceiver";
    
	@Override
	public void onReceive(Context context, Intent intent) {
		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			try {
				Thread.sleep(2500L);
			} catch (InterruptedException e) { }
			
    		Intent prompt = new Intent();
        	prompt.setClassName("tss.droidtools.phone","tss.droidtools.phone.CallAnswerActivity");
        	prompt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        	context.startActivity(prompt);
		}
	}
}
