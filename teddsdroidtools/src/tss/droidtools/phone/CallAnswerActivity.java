package tss.droidtools.phone;

import tss.droidtools.phone.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	logMe("onCreate called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callanswerscreen);
		Button returnToCallScreen = (Button) findViewById(R.id.returnToCallScreen);
		returnToCallScreen.setOnClickListener(new OnClickListener() {
          	public void onClick(View v){					logMe("returnToCallScreen onClick event");
          		finish();
          	}
		});
	}
	
	/** broadcast HEADSETHOOK when the camera button is pressed*/
	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {		logMe("dispatchKeyEvent called with "+event);
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_FOCUS:						logMe("KEYCODE_FOCUS ignoring it");
			return true;
		case KeyEvent.KEYCODE_CAMERA:						logMe("KEYCODE_CAMERA");
			
			KeyEvent fakeHeadsetPress =	new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HEADSETHOOK);
			Intent fakeHeadsetIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
			fakeHeadsetIntent.putExtra(Intent.EXTRA_KEY_EVENT, fakeHeadsetPress);

			sendOrderedBroadcast(fakeHeadsetIntent, null);
			
			// not sure what this accomplishes here really...
			logMe("moveTaskToBack(true) returns: "+moveTaskToBack(true));
	  		
			finish();
	  		
			return true;
		default: 											logMe("Unknown key event: "+event);
			break;
		}
		return super.dispatchKeyEvent(event);
	}
	
	private void logMe(String s) {
		if (Hc.DBG) Log.d(Hc.PRE_TAG + "CallAnswerActivity" + Hc.POST_TAG,Hc.LOG_FLUFF + s);
	}
}