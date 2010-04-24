package tss.droidtools.phone;

import android.app.Activity;
import android.content.SharedPreferences;
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
public class ConfigScreenActivity extends Activity {

	private SharedPreferences p;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        p = this.getSharedPreferences(Hc.PREFSNAME, 0);
        if (!p.contains(Hc.PREF_ENABLED_KEY))
        	p.edit().putBoolean(Hc.PREF_ENABLED_KEY, false).commit();

        Boolean enabled = p.getBoolean(Hc.PREF_ENABLED_KEY, false);
        final CheckBox checkbox = (CheckBox) findViewById(R.id.cameraAnswerCheckBox);
        checkbox.setChecked(enabled);
        
        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                	p.edit().putBoolean(Hc.PREF_ENABLED_KEY, true).commit();
                    Toast.makeText(ConfigScreenActivity.this, "Feature Enabled", Toast.LENGTH_SHORT).show();
                } else {
                	p.edit().putBoolean(Hc.PREF_ENABLED_KEY, false).commit();
                    Toast.makeText(ConfigScreenActivity.this, "Feature Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//	private void logMe(String s) {
//		if (Hc.DBG) Log.d(Hc.LOG_TAG, Hc.PRE_TAG + "ConfigScreenActivity" + Hc.POST_TAG + " "+ s);
//	}
}