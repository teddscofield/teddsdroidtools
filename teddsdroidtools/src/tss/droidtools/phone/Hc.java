package tss.droidtools.phone;

import android.content.Context;

/**
 * Application Hard Codes
 *
 * @author tedd
 *
 */
public class Hc {
	public static final boolean DBG = true;
	public static final long STARTUP_DELAY = 3000L;
	public static final long RESTART_DELAY = 500L;
	public static final String LOG_TAG = "TeddsDroidTools";
	public static final String PRE_TAG = ">";
	public static final String POST_TAG = "<";
	public static final String PREFSNAME = "tss.droidtools.phone";
	public static final String PREF_PHONE_TOOLS_KEY = "enabled";
	public static final String PREF_CALL_ANSWER_TOOLS_KEY = "callanswertools";
	public static final String PREF_DEBUG_LOGGING_KEY = "debuglogging";
	public static final String PREF_ANSWER_WITH_CAMERA_KEY = "cameraanswer";
	public static final String PREF_ANSWER_WITH_BUTTON_KEY = "buttonanswer";
	public static final String PREF_ANSWER_WITH_TRACKBALL_KEY = "trackballanswer";
	public static final String PREF_ALLOW_REJECT_KEY = "rejectcall";
	public static final String PREF_SCREEN_GUARD_TOOLS_KEY = "screenguardtools";
	
	public static boolean debugEnabled(Context c) {
		return c.getSharedPreferences(Hc.PREFSNAME,0).getBoolean(Hc.PREF_DEBUG_LOGGING_KEY, true);
	}
}
