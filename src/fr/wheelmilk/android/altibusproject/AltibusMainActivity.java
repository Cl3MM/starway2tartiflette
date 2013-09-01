package fr.wheelmilk.android.altibusproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import com.viewpagerindicator.PageIndicator;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.support.IconsTabPageIndicator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;

public class AltibusMainActivity extends SherlockFragmentActivity {

	AltibusFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
//	private SlidingMenu slidingMenu ;
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
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("billetCreated"));
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("nouvelAchat"));
//		if (isOnline(this)) {
//			Log.v("Altibus", "Internet connection found! Launching super asynchronous task...");
//			Toast.makeText(this, "Internet connection found! Launching super asynchronous task...",
//					Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(this, "Internet connection not found.",
//					Toast.LENGTH_SHORT).show();
//		}
		mAdapter = new AltibusFragmentAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(3);
		mIndicator = (IconsTabPageIndicator) findViewById(R.id.indicator);
		((IconsTabPageIndicator) mIndicator)
				.setTabIconLocation(IconsTabPageIndicator.LOCATION_UP);
		mPager.setAdapter(mAdapter);
		mIndicator.setViewPager(mPager);
		
//		slidingMenu = new SlidingMenu(this);
//        slidingMenu.setMode(SlidingMenu.LEFT);
//        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
//        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
//        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        slidingMenu.setFadeDegree(0.35f);
//        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        slidingMenu.setMenu(R.layout.slidingmenu);
//        
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        createOrUpdateCountryList();
		
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("billetCreated"));
	    
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.v(getClass().toString(), "Received intent: " + action);
			if( action.equals("billetCreated") ) { 
				// Un nouveau billet vient d'être crée, on scroll 
				// jusqu'au fragment Mes billets et on met à jour la liste des billets.
				int position = 2; // Fragment mes billets en position 2 
				MesBilletsFragment f = (MesBilletsFragment) AltibusFragmentAdapter.getRegisteredFragment(position);
				f.updateView();
				mPager.setCurrentItem(position, true);
			} else if (action.equals("nouvelAchat")) {
				Bundle extras = intent.getExtras();
				
				Date da = new Date(extras.getLong("dateA"));
				GaresDepart gd = extras.getParcelable("gareAl");
				GaresArrivee ga = extras.getParcelable("gareAr");
				HorrairesAller ha = extras.getParcelable("horraireA");
				Date dr = null;
				if (extras.containsKey("dateR")) { 
					dr = new Date(extras.getLong("dateR"));
				}
				 HorrairesRetour hr = null;
				if (extras.containsKey("horraireR")) {
					hr = extras.getParcelable("horraireR");
				}
				
				int position = 0; // Fragment mes billets en position 2 
				PageAchat f = (PageAchat) AltibusFragmentAdapter.getRegisteredFragment(position);
				f.createNewSearchFromPageHorraires(gd, ga, da, ha, dr, hr);
				mPager.setCurrentItem(position, true);
			}
		}
	};
	private String getFragmentTag(int pos){
	    return "android:switcher:"+R.id.pager+":"+pos;
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
//            this.slidingMenu.toggle();
        } else 
            return super.onOptionsItemSelected(item);
        return true;
	}

	@Override
    public void onBackPressed() {
//        if ( slidingMenu.isMenuShowing()) {
//            slidingMenu.toggle();
//        }
//        else {
            super.onBackPressed();
//        }
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
//            this.slidingMenu.toggle();
//            return true;
//        }
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
