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

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import com.viewpagerindicator.PageIndicator;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.support.Helper;
import fr.wheelmilk.android.altibusproject.support.Support;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;

public class AltibusMainActivity extends SlidingFragmentActivity {

	AltibusFragmentAdapter mAdapter;
	Date exitCounter;
	ViewPager mPager;
	PageIndicator mIndicator;
	private Fragment mContent;
	private SlidingMenu sm ;
	private static final int RESULT_SETTINGS = 1;
	public ArrayList<String> countryPreferencesList;
	private SharedPreferences mPref;
	 
	// ConnectivityManager cm =
	// (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	//
	// NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	// boolean isConnected = activeNetwork.isConnectedOrConnecting();
	// boolean isWiFi = activeNetwork.getType() ==
	// ConnectivityManager.TYPE_WIFI;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.simple_tabs);
	    
		LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("billetCreated"));
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("nouvelAchat"));
	    
	   
//	    LocationListener mlocListener = new GeolocationManager(this);
//	    LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//
//	    mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	    
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
//		mIndicator = (IconsTabPageIndicator) findViewById(R.id.indicator);
//		((IconsTabPageIndicator) mIndicator)
//				.setTabIconLocation(IconsTabPageIndicator.LOCATION_UP);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);
//		mIndicator.setViewPager(mPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSlidingActionBarEnabled(true);
//		getSupportActionBar().setLogo(R.drawable.ic_navigation_drawer);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_navigation_drawer) );
//        getSupportActionBar().set
//        createOrUpdateCountryList();
		
	    LocalBroadcastManager.getInstance(this).registerReceiver(
	            mMessageReceiver, new IntentFilter("billetCreated"));
	    
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    	// set the Above View

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.menu_frame, new AltibusMenuFragment())
        .commit();

//    	// customize the SlidingMenu
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.slidingmenuWidth);
    	sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setSlidingActionBarEnabled(false);
//        sm.setMenu(R.layout.slidingmenu);
        
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}
		});
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    Support s = new Support(this);
	    boolean firstSlide = s.isSet("firstSlide");
	    Log.v(getClass().toString(), firstSlide ? "First Run" : "Second Run");
	    if (firstSlide) {
		    new Handler().postDelayed(new Runnable() {
		        @Override
		        public void run() {
		            toggle();
		        }
		    }, 1400);
		    s.set("firstSlide");
	    }
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(int position) {

		mPager.setCurrentItem(position, true);
		getSlidingMenu().showContent();
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
				boolean allerSimple = extras.getBoolean("allersimple");
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
				f.createNewSearchFromPageHorraires(gd, ga, da, ha, dr, hr, allerSimple);
				mPager.setCurrentItem(position, true);
//				switchToMesBillets();
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
//		getSupportMenuInflater().inflate(R.menu.altibus_main, menu);
		return true;
	}

	// If-else statements instead of switch
	// http://tools.android.com/recent/buildchangesinrevision14
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.prefs) {
        	startActivity(new Intent(this, UserPreferences.class));
        } else if (item.getItemId() == android.R.id.home) { // Sliding Menu
            sm.toggle();
        } else 
            return super.onOptionsItemSelected(item);
        return true;
	}

	@Override
    public void onBackPressed() {
//		if (mContent != null) {
//			Bundle args = mContent.getArguments();
//			int val = args.getInt("val");
//			if (val != 0) {
//				switchContent(PageAchat.init(0));
//			}
//		} else {
        if ( sm.isMenuShowing()) {
            sm.toggle();
        } else if (exitCounter == null) {
        	exitCounter = new Date();
        	Helper.grilledRare(this, "Appuyez une seconde fois pour quitter");
        } else if (exitCounter != null) {
        	Date now = new Date();
        	Log.v(getClass().toString(), "Temps écoulé : "  + (now.getTime() - exitCounter.getTime() ) );
        	if (now.getTime() - exitCounter.getTime() < 1000)
        		super.onBackPressed();
        	else exitCounter = null;        	
        }
        else {
            super.onBackPressed();
        }
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            sm.toggle();
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
