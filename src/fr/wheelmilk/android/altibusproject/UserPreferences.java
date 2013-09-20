package fr.wheelmilk.android.altibusproject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.AltibusDataPays;
import fr.wheelmilk.android.altibusproject.support.Helper;
import fr.wheelmilk.android.altibusproject.support.Support;

public class UserPreferences extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 11) {
			// Change la couleur du texte de l'action bar pour annuler le theme light
			int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView Tv = (TextView)findViewById(titleId);

			Tv.setTextColor(getResources().getColor(R.color.table_background));			
		}

        PreferenceManager.setDefaultValues(UserPreferences.this, R.xml.preferences, false);
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                initSummary(getPreferenceScreen().getPreference(i));
            }
 	  
		AltibusDataPays al = createOrUpdateCountryList();
		CharSequence[] cs = al.getListPays().toArray(new CharSequence[al.getListPays().size()]);
		ListPreference lp = (ListPreference) findPreference("prefUserCountry");
		lp.setEntries(cs);
		lp.setEntryValues(cs);
		
		CharSequence[] ages = generateAgeList();
		ListPreference lpA = (ListPreference) findPreference("prefUserAge");
		lpA.setEntries(ages);
		lpA.setEntryValues(ages);
	}

	private CharSequence[] generateAgeList() {
		CharSequence[] cs = new CharSequence[121];
		for(int i = 0; i < 121; i++) {
			cs[i] = String.valueOf(i);
		}
		return cs;
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
//			Log.v(this.getClass().toString(), file.replace("\n", " ").replace("\t", " ").replace("  ", " "));
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

	@Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

	    Support s = new Support(this);
	    boolean prefWarning = s.isSet("prefWarning");
	    Log.v(getClass().toString(), prefWarning ? "First Run" : "Second Run");

	    if( prefWarning) {
	    	SimpleAlertDialog dlg = new SimpleAlertDialog(this, getString(R.string.pressBackToSave));
	    	dlg.show();
	    	s.set("prefWarning");
	    }
    }

	@Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
	
    @Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
    	Log.v(getClass().toString(), "KEY: " +key);
    	Preference p = findPreference(key);
    	
    	if (p.getKey().equals("prefUserEmail")) {
            EditTextPreference editTextPref = (EditTextPreference) p;
           	String k = p.getKey();
           	String v = editTextPref.getText().replace(" ", "");
    		if( v.contains("@") ) updatePrefSummary(findPreference(key));
    		else {
    			Helper.grilledRare(this, "Votre adresse email n'est pas valide");
    			p.setSummary( getResources().getString(R.string.pref_user_set_email));
    			editTextPref.setText("");
    		}
    	} else updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory pCat = (PreferenceCategory) p;
            for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                initSummary(pCat.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
//            p.setSummary(listPref.getEntry());
            if (listPref.getValue().equals("-1")) {
            	p.setSummary("Veuillez saisir votre age");
            } else p.setSummary(listPref.getValue());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
           	String k = p.getKey();
           	String v = editTextPref.getText().replace(" ", "");
           	
           	if (TextUtils.isEmpty(v)) {
           		if (k.equals("prefUserLastName")) 
           			p.setSummary( getResources().getString(R.string.pref_user_last_name_summary));
           		else if (k.equals("prefUserFirstName"))
           			p.setSummary( getResources().getString(R.string.pref_user_first_name_summary));
           		else if (k.equals("prefUserTelephone"))
           			p.setSummary( getResources().getString(R.string.pref_user_set_telephone));
           		else if (k.equals("prefUserEmail"))
           			p.setSummary( getResources().getString(R.string.pref_user_set_email));
           		else if (k.equals("prefUserAddress"))
           			p.setSummary( getResources().getString(R.string.pref_user_set_adresse));
           		else if (k.equals("prefUserAddress2"))
           			p.setSummary( getResources().getString(R.string.pref_user_set_adresse));
           		else if (k.equals("prefUserCodePostal"))
           			p.setSummary( getResources().getString(R.string.pref_user_code_postal_summary));
           		else if (k.equals("prefUserVille"))
           			p.setSummary( getResources().getString(R.string.pref_user_ville_summary));
           		else if (k.equals("prefUserCountry"))
           			p.setSummary( getResources().getString(R.string.pref_user_countries_summary));
           	} else if (k.equals("prefUserLastName") && v.equals("Nom")) 
       				p.setSummary( getResources().getString(R.string.pref_user_last_name_summary));
       			else if (k.equals("prefUserFirstName") && v.equals("Prénom"))
       				p.setSummary( getResources().getString(R.string.pref_user_first_name_summary)); 
       			else p.setSummary(editTextPref.getText());
        }
    }
}