package fr.wheelmilk.android.altibusproject.support;

import android.content.Context;
import android.content.SharedPreferences;

public class Support {
	final private SharedPreferences mPref;

	public Support(Context cxt) {
		mPref = cxt.getSharedPreferences("prefFirstTimeRun", 0);
	}

	/**
	 * get if this is the first run
	 * 
	 * @return returns true, if this is the first run
	 */
	public boolean isSet(String key) {
		return mPref.getBoolean(key, true);
	}

	/**
	 * store the first run
	 */
	public void set(String key) {
		SharedPreferences.Editor edit = mPref.edit();
		edit.putBoolean(key, false);
		edit.commit();
	}
}
