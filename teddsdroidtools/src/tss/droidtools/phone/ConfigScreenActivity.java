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
        p = this.getSharedPreferences(Hc.PREFSNAME, MODE_WORLD_READABLE);
        Boolean enabled = p.getBoolean("enable", false);
        
        final CheckBox checkbox = (CheckBox) findViewById(R.id.cameraAnswerCheckBox);
        checkbox.setChecked(enabled);
        
        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	
            	SharedPreferences.Editor ed = p.edit();
            	
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                	ed.putBoolean("enable", true);
                    Toast.makeText(ConfigScreenActivity.this, "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                	ed.putBoolean("enable", true);
                    Toast.makeText(ConfigScreenActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
                }
                ed.commit();
            }
        });
    }
}