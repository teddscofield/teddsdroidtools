package tss.droidtools.phone;

import java.lang.reflect.Method;

import tss.droidtools.BaseActivity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import com.android.internal.telephony.ITelephony;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

/**
 *
 * Activity that gets called by the broadcast receiver when the phone rings.
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
public class CallAnswerActivity extends BaseActivity {
	/**
	 * whether or not to use the AIDL technique or 
	 * the HEADSET_HOOK/package restart techniques
	 */
	private static final boolean USE_ITELEPHONY = true;
	
	/**
	 * internal phone state broadcast receiver
	 */
	protected BroadcastReceiver r;
	
	/**
	 * TelephonyManager instance used by this activity
	 */
	private TelephonyManager tm;
	
	/**
	 * AIDL access to the telephony service process
	 */
	private ITelephony telephonyService;

	// ------------------------------------------------------------------------
	// primary life cycle call backs
	// ------------------------------------------------------------------------

	/**
	 * main() :)
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debugLog("onCreate called");
		setContentView(R.layout.callanswerscreen);
		
		// grab an instance of telephony manager
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		
		// connect to the underlying Android telephony system
		if (USE_ITELEPHONY)
			connectToTelephonyService();
		
		// turn our idle phone state receiver on
		registerReciever();
		


		// touch screen return button
		Button returnToCallScreen = (Button) findViewById(R.id.returnToCallScreen);
		returnToCallScreen.setOnClickListener(new ReturnButtonOnClickListener());

		// touch screen reject/ignore call button
		Button rejectCall = (Button) findViewById(R.id.rejectCallButton);
		if (getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_ALLOW_REJECT_KEY, true)) 
			rejectCall.setOnLongClickListener(new RejectCallOnLongClickListener());
		else 
			rejectCall.setVisibility(View.GONE);			

		// touch screen answer button
		Button answerButton = (Button) findViewById(R.id.answerCallButton);
		if (getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_ANSWER_WITH_BUTTON_KEY, true)) 
			answerButton.setOnLongClickListener(new AnswerCallOnLongClickListener());
		else 
			answerButton.setVisibility(View.GONE);			
	}

	/** 
	 * (re)register phone state receiver on resume, exit if the phone is idle 
	 */
	@Override protected void onResume() {
		super.onResume();

		registerReciever();

		if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
			debugLog("phone is idle, stopping.");
			exitCleanly();
		}
	}

	/** 
	 * unregister phone state receiver, schedule restart if not exiting at the users request 
	 */
	@Override protected void onPause() {
		super.onPause();
		
		unHookReceiver();
		
		if (!isFinishing()) 
		{
			debugLog("system forced pause occured, scheduling delayed restart");
			Intent i = new Intent(getApplicationContext(), CallAnswerIntentService.class);
			i.putExtra("delay", Hc.RESTART_DELAY);
			startService(i);
		}
	}
	
	// ------------------------------------------------------------------------
	// Input event handler call backs
	// ------------------------------------------------------------------------
	
	/**
	 * Camera button press event handler that will answer a call
	 */
	@Override public boolean dispatchKeyEvent(KeyEvent event) {
		
		switch (event.getKeyCode()) 
		{
		case KeyEvent.KEYCODE_FOCUS: return true;
		case KeyEvent.KEYCODE_CAMERA: answerCall(); return true;
		case KeyEvent.KEYCODE_DPAD_CENTER: answerCall(); return true;		
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * Return button click listener will exit back to the
	 * phones stock answer call application.
	 */
	private class ReturnButtonOnClickListener implements OnClickListener {
		@Override public void onClick(View v) {
			debugLog("returnToCallScreen onClick event");
			exitCleanly();
		}
	}
	
	/**
	 * Reject button long click listener will reject the
	 * incoming call. 
	 */
	private class RejectCallOnLongClickListener implements OnLongClickListener {
      	@Override public boolean onLongClick(View v){
      		debugLog("touch screen ignore call button onClick event");
      		ignoreCall();
      		exitCleanly();
      		return true;
      	}		
	}

	/**
	 * Answer button long click listener will answer the
	 * incoming call.
	 */
	private class AnswerCallOnLongClickListener implements OnLongClickListener {
		@Override public boolean onLongClick(View v){
			debugLog("touch screen answer button onClick event");
			answerCall();
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
				if (!phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) 
				{
					debugLog("received "+phone_state+", time to go bye bye, thanks for playing!");
					exitCleanly();
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
	
	// ------------------------------------------------------------------------
	// application methods
	// ------------------------------------------------------------------------
	
	/** 
	 * get an instance of ITelephony to talk handle calls with 
	 */
	@SuppressWarnings("unchecked") private void connectToTelephonyService() {
		try 
		{
			// "cheat" with Java reflection to gain access to TelephonyManager's ITelephony getter
			Class c = Class.forName(tm.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			telephonyService = (ITelephony)m.invoke(tm);

		} catch (Exception e) {
			e.printStackTrace();
			debugLog("FATAL ERROR: could not connect to telephony subsystem");
			debugLog("Exception object: "+e);
			finish();
		}		
	}
	
	//
	// answer call
	//
	
	/** 
	 * answer incoming calls
	 */
	private void answerCall() {
		if (USE_ITELEPHONY)
			answerCallAidl();
		else
			answerCallHeadsetHook();

		exitCleanly();
	}
	
	/**
	 * ACTION_MEDIA_BUTTON broadcast technique for answering the phone
	 */
	private void answerCallHeadsetHook() {
		KeyEvent headsetHook =	new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
		Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		mediaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, headsetHook);
		sendOrderedBroadcast(mediaButtonIntent, null);
	}

	/**
	 * AIDL/ITelephony technique for answering the phone
	 */
	private void answerCallAidl() {
		try {
			telephonyService.silenceRinger();
			telephonyService.answerRingingCall();
		} catch (RemoteException e) {
			e.printStackTrace();
			errorLog("FATAL ERROR: call to service method answerRiningCall failed.");
			errorLog("Exception object: "+e);
		}		
	}

	//
	// ignore call
	//
	
	/**
	 * ignore incoming calls 
	 */
	private void ignoreCall() {
		if (USE_ITELEPHONY)
			ignoreCallAidl();
		else
			ignoreCallPackageRestart();
	}
	
	/**
	 * package restart technique for ignoring calls 
	 */
	private void ignoreCallPackageRestart() {
		ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		am.restartPackage("com.android.providers.telephony");
		am.restartPackage("com.android.phone");
	}
	
	/** 
	 * AIDL/ITelephony technique for ignoring calls
	 */
	private void ignoreCallAidl() {
		try 
		{
			telephonyService.silenceRinger();
			telephonyService.endCall();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
			errorLog("FATAL ERROR: call to service method endCall failed.");
			errorLog("Exception object: "+e);
		}
	}	
	
	/** 
	 * cleanup and exit routine
	 */
	private void exitCleanly() {
		unHookReceiver();
		moveTaskToBack(true);		
		finish();
	}

	// ------------------------------------------------------------------------
	// debugging
	// ------------------------------------------------------------------------
	
	@Override protected void onStart() {
		super.onStart();
		debugLog("onStart");
	}
	
	@Override protected void onStop() {
		super.onStop();
		debugLog("stopped, finishing? "+isFinishing());
	}
	
	@Override protected void onDestroy() {
		super.onStop();
		debugLog("destroyed");
	}
	
	private void debugLog(String s) {
		super.debugLog("CallAnswerActivity", s);
	}
	private void errorLog(String s) {
		super.errorLog("CallAnswerActivity", s);
	}
}


/*
 * 
 * Notes
 * 
 * I. KEYCODE_HEADSETHOOK
 * The "magic" goes here.  
 * 
 * Programmatically mimic the press of the button on a head set used to answer
 * an incoming call.  The recipe to do this is as follows:
 * 
 *  intent - ACTION_MEDIA_BUTTON
 *  action - ACTION_DOWN
 *  code   - KEYCODE_HEADSETHOOK
 *  
 *  Broadcasting that intent answers the phone =)
 *  
 *  However, if there are any other apps that are listening for 
 *  ACTION_MEDIA_BUTTION intents and consuming them they won't get
 *  to the Phone app.
 *  
 *
 * II.
 * case KeyEvent.KEYCODE_FOCUS:
 * this event occurs when you press down lightly on the camera button
 * e.g. auto focus.  The event happens a lot even when you press down
 * hard (as the button is on its way down to the "hard press").
 * returning true to consume the event and prevent further processing of 
 * it by other apps 
 * 
*/