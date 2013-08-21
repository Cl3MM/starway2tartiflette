package fr.wheelmilk.android.altibusproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import com.viewpagerindicator.PageIndicator;

import fr.wheelmilk.android.altibusproject.models.AltibusDataPays;
import fr.wheelmilk.android.altibusproject.support.IconsTabPageIndicator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class AltibusMainActivity extends SherlockFragmentActivity {

	AltibusFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	private SlidingMenu slidingMenu ;
	private static final int RESULT_SETTINGS = 1;
	public ArrayList<String> countryPreferencesList;

	// ConnectivityManager cm =
	// (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	//
	// NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	// boolean isConnected = activeNetwork.isConnectedOrConnecting();
	// boolean isWiFi = activeNetwork.getType() ==
	// ConnectivityManager.TYPE_WIFI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTheme(R.style.Theme_Altibus);
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_altibus_main);
		setContentView(R.layout.simple_tabs);

		if (isOnline(this)) {
			Log.v("Altibus", "Internet connection found! Launching super asynchronous task...");
			Toast.makeText(this, "Internet connection found! Launching super asynchronous task...",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Internet connection not found.",
					Toast.LENGTH_SHORT).show();
		}
		mAdapter = new AltibusFragmentAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(4);
		mIndicator = (IconsTabPageIndicator) findViewById(R.id.indicator);
		((IconsTabPageIndicator) mIndicator)
				.setTabIconLocation(IconsTabPageIndicator.LOCATION_UP);

		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		
		slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createOrUpdateCountryList();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	
	
	private StringBuilder getXml(String url) {

		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;

		URL serverAddress = null;

		try {
			serverAddress = new URL(url);
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(5000);

			connection.connect();

			// read the result from the server
			rd = new BufferedReader(new InputStreamReader( connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}

			return sb;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the connection, set all objects to null
			connection.disconnect();
			rd = null;
			sb = null;
			connection = null;
		}
		return null;
	}

	private void createOrUpdateCountryList() {
		AssetManager assetManager = getAssets();
		InputStream countryFile;
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

			AltibusDataPays countries = null;
			
			if (strReader != null) {
				try {
					 countries = serializer.read(AltibusDataPays.class, strReader);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.altibus_main, menu);
		return true;
	}

	// If-else statements instead of switch
	// http://tools.android.com/recent/buildchangesinrevision14
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.prefs) {
        	startActivity(new Intent(this, UserPreferences.class));
        } else if (item.getItemId() == android.R.id.home) { // Sliding Menu
            this.slidingMenu.toggle();
        } else 
            return super.onOptionsItemSelected(item);
        return true;
	}

	@Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	public boolean isOnline(Context con) {
		boolean connected = false;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			return connected;

		} catch (Exception e) {
			System.out
					.println("CheckConnectivity Exception: " + e.getMessage());
			Log.v("connectivity", e.toString());
		}
		return connected;
	}
}
