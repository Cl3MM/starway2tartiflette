package fr.wheelmilk.android.altibusproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.AltibusDataPays;

public class UserPreferences extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);

		AltibusDataPays al = createOrUpdateCountryList();
		CharSequence[] cs = al.getListPays().toArray(new CharSequence[al.getListPays().size()]);
		ListPreference lp = (ListPreference) findPreference("prefUserCountry");
		lp.setEntries(cs);
		lp.setEntryValues(cs);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        //do your own thing here
	    	finish();
	        return true;
	    default: return true; 
	    }
	}
	
	private AltibusDataPays createOrUpdateCountryList() {
		AssetManager assetManager = getAssets();
		InputStream countryFile;
		AltibusDataPays countries = null;
		try {
			countryFile = assetManager.open("countries.xml");
		} catch (IOException e) {
			e.printStackTrace();
			countryFile = null;
		}
		
		if (countryFile != null) {
			Serializer serializer = new Persister();
			Reader reader = null;
			java.util.Scanner s = new java.util.Scanner(countryFile).useDelimiter("\\A");
		    String file = (s.hasNext() ? s.next() : "");
			StringReader strReader = new StringReader(file);
			Log.v(this.getClass().toString(), file.replace("\n", " ").replace("\t", " ").replace("  ", " "));
			reader = new InputStreamReader(countryFile);
			
			if (strReader != null) {
				try {
					 countries = serializer.read(AltibusDataPays.class, strReader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return countries;
	}
}