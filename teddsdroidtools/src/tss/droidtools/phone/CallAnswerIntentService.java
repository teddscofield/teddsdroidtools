package tss.droidtools.phone;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallAnswerIntentService extends IntentService {

	public CallAnswerIntentService() {
		super("CallAnswerIntentService");
		logMe("constructed");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		logMe("onCreate");
	}

	
	@Override
	protected void onHandleIntent(Intent intent) {
		Context c = getBaseContext();

		
		// pause
		try {
			Long delay = (Long)intent.getExtras().get("delay");
			logMe("pausing for "+delay+" millis");
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logMe("checking call state");
		// make sure the phone is ringing...
		TelephonyManager t = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		if (t.getCallState() != TelephonyManager.CALL_STATE_RINGING)
			return;

		// (re)start the CallAnswerActivity
		Intent i = new Intent();
		i.setClassName("tss.droidtools.phone","tss.droidtools.phone.CallAnswerActivity");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		logMe("starting call screen");
		c.startActivity(i);
		return;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		logMe("onDestroy");
	}
	
	private void logMe(String s) {
		Log.d(Hc.LOG_TAG,">CallAnswerIntentService< "+s);
	}
}
