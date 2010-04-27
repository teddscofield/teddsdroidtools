package tss.droidtools.phone;

import tss.droidtools.phone.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
public class CallAnswerActivity extends Activity {
	protected CallAnswerActivityReceiver r;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logMe("onCreate called");
		r = new CallAnswerActivityReceiver();
		
		setContentView(R.layout.callanswerscreen);
		Button returnToCallScreen = (Button) findViewById(R.id.returnToCallScreen);
		returnToCallScreen.setOnClickListener(new OnClickListener() {
          	public void onClick(View v){
          		logMe("returnToCallScreen onClick event");
          		finishHim();
          	}
		});
		

		// kill this activity once the phone is picked up.
		this.registerReceiver(r, new IntentFilter("android.intent.action.PHONE_STATE"));
		
	}
	
	public void finishHim() {
		unHookReceiver();
		finish();
	}
	
	public void unHookReceiver() {
		logMe("unhooking the reciever");
		if (r != null) {
			unregisterReceiver(r);
			r = null;
		} else {
			logMe("whoops! reciever was allready null...");
		}
		logMe("done");
	}
	

	
	/** broadcast HEADSETHOOK when the camera button is pressed*/
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
		//logMe("dispatchKeyEvent called with "+event);
		
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_FOCUS:
			/* this event occurs when you press down lightly on the camera button
			 * e.g. auto focus.  The event happens a lot even when you press down
			 * 
			 * hard (as the button is on its way down to the "hard press").
			 */
			
			//logMe("KEYCODE_FOCUS ignoring it");
			
			/* returning true to consume the event and prevent further processing of it by other apps */ 
			return true;
			
		case KeyEvent.KEYCODE_CAMERA:
			/*
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
			 */
			KeyEvent fakeHeadsetPress =	new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
			Intent fakeHeadsetIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			
			fakeHeadsetIntent.putExtra(Intent.EXTRA_KEY_EVENT, fakeHeadsetPress);
			logMe("broadcasting ACTION_MEDIA_BUTTION intent with a KEYCODE_HEADSETHOOK code on an ACTION_DOWN action");
			
			unHookReceiver();
			
			sendOrderedBroadcast(fakeHeadsetIntent, null);
			
			finish();
	  		
			return true;
			
		default:
			logMe("Unknown key event: "+event);
			break;
		}
		
		return super.dispatchKeyEvent(event);
	}
	
	/** shut off if call was not answered */
	private class CallAnswerActivityReceiver extends BroadcastReceiver {
		public CallAnswerActivityReceiver() {
			logMe("created a CallAnswerActivityReceiver for this task");
		}
		@Override
		public void onReceive(Context c, Intent i) {
			String phone_state = i.getStringExtra(TelephonyManager.EXTRA_STATE);
			if (!phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				logMe("received "+phone_state+", time to go bye bye, thanks for playing!");
				finishHim();
			}
		} 
	}
	
	
	private void logMe(String s) {
		if (Hc.DBG) Log.d(Hc.LOG_TAG, Hc.PRE_TAG + "CallAnswerActivity" + Hc.POST_TAG + " "+ s);
	}
}