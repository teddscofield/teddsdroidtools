package tss.droidtools.phone;

import tss.droidtools.BaseActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;
/**
 * Configuration screen for the camera button 
 * @author tedd
 *
 */
public class ConfigScreenActivity extends BaseActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        renderConfigCheckbox(Hc.PREF_PHONE_TOOLS_KEY,true,R.id.phoneToolsCheckBox,"ALL Phone Tools have been");
        renderConfigCheckbox(Hc.PREF_CALL_ANSWER_TOOLS_KEY,true,R.id.callAnswerTools,"Call Answer Tools");
        renderConfigCheckbox(Hc.PREF_ANSWER_WITH_CAMERA_KEY,true,R.id.cameraAnswerCheckBox,"Camera Button Answer");
        renderConfigCheckbox(Hc.PREF_ANSWER_WITH_TRACKBALL_KEY,true,R.id.trackballAnswerCheckBox,"Trackball Answer");
        renderConfigCheckbox(Hc.PREF_ANSWER_WITH_BUTTON_KEY,true,R.id.touchscreenButtonAnswerCheckBox,"Touchscreen Button Answer");
        renderConfigCheckbox(Hc.PREF_ALLOW_REJECT_KEY,true,R.id.rejectCallsCheckBox,"Reject Call Feature");
        renderConfigCheckbox(Hc.PREF_SCREEN_GUARD_TOOLS_KEY,true,R.id.inCallScreenGuardCheckBox,"In-Call Screen Guard");
        renderConfigCheckbox(Hc.PREF_DEBUG_LOGGING_KEY,true,R.id.debugLoggingCheckBox,"Debug Logging");
        
    }
    
    private void renderConfigCheckbox(String key,boolean defaultVal,int viewId, String toastMsg) {
    	// set the default if it didn't exist
        if (!p.contains(key))
        	p.edit().putBoolean(key, defaultVal).commit();

        // get the current setting
        Boolean currentSetting = p.getBoolean(key, defaultVal);
        
        // get the view component
        final CheckBox checkbox = (CheckBox) findViewById(viewId);
        
        // set the current setting
        checkbox.setChecked(currentSetting);
        
        // register the onClick call back
        checkbox.setOnClickListener(new CustomOnClickListener(key,toastMsg) {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) 
                {
                	p.edit().putBoolean(key, true).commit();
                    Toast.makeText(ConfigScreenActivity.this, toastMsg + " Enabled", Toast.LENGTH_SHORT).show();
                } 
                else 
                {
                	p.edit().putBoolean(key, false).commit();
                    Toast.makeText(ConfigScreenActivity.this, toastMsg + " Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });  
    }
    
    private class CustomOnClickListener implements OnClickListener {

    	protected String key;
    	protected String toastMsg;
    	public CustomOnClickListener(String key,String toastMsg) {
    		this.key = key; 
    		this.toastMsg = toastMsg;
    	}
		@Override
		public void onClick(View v) {
			throw new UnsupportedOperationException();
			
		}
    	
    }
}