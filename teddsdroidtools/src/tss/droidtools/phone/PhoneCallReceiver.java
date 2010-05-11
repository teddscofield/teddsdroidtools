package tss.droidtools.phone;

import tss.droidtools.BaseReceiver;
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
public class PhoneCallReceiver extends BaseReceiver {
	
	/**
	 * Call back which fires off when the phone changes state.  
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		debugOn = Hc.debugEnabled(context);
		
		/* make sure the feature is enabled */
		if (!context.getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_PHONE_TOOLS_KEY, true)) 
		{
			logMe("All Phone Tools have been disabled by user. ");
			return;
		} 

		/* examine the state of the phone that caused this receiver to fire off */
		String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) 
		{
			if (context.getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_CALL_ANSWER_TOOLS_KEY, true))
			{
				logMe("Phone Ringing: the phone is ringing, scheduling creation call answer screen activity");
				Intent i = new Intent(context, CallAnswerIntentService.class);
				i.putExtra("delay", Hc.STARTUP_DELAY);
				context.startService(i);
				logMe("Phone Ringing: started, time to go back to listening");
			} else {
				logMe("Phone Ringing: Call Answer tools disabled by user");
			}
		}
		if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
		{
			if (context.getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_SCREEN_GUARD_TOOLS_KEY, true))
			{
				Intent i = new Intent(context,InCallScreenGuardService.class);
				i.putExtra("delay", Hc.STARTUP_DELAY);
				logMe("Phone Offhook: starting screen guard service");
				context.startService(i);
			} else {
				logMe("Phone Offhook: In-Call Screen Guard disabled by user");
			}
		}
		if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE))
		{
			if (context.getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_SCREEN_GUARD_TOOLS_KEY, true))
			{
				Intent i = new Intent(context,InCallScreenGuardService.class);
				logMe("Phone Idle: stopping screen guard service");
				context.stopService(i);
			} else {
				logMe("Phone Idle: In-Call Screen Guard disabled by user");
			}
		}
		
		return;
	}
	
	private void logMe(String s) {
		super.logMe("PhoneCallReceiver", s);
	}
}