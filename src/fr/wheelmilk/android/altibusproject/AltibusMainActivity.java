package fr.wheelmilk.android.altibusproject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.PageIndicator;

import fr.wheelmilk.android.altibusproject.support.IconsTabPageIndicator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

public class AltibusMainActivity extends SherlockFragmentActivity {

	AltibusFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	private static final int RESULT_SETTINGS = 1;

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
			Log.v("Altibus", "Launching super asynchronous task...");
			Toast.makeText(this, "Launching super asynchronous task...",
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.altibus_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(this, ShowUserPreferences.class));
		}
		return super.onOptionsItemSelected(item);
	}

	// @Override
	// public void onBackPressed() {
	// Fragment fragment =
	// AltibusFragmentAdapter.getRegisteredFragment(mPager.getCurrentItem());
	// if (fragment != null) // could be null if not instantiated yet
	// {
	// if (fragment.getView() != null) {
	// // Pop the backstack on the ChildManager if there is any. If not, close
	// this activity as normal.
	// if (!fragment.getChildFragmentManager().popBackStackImmediate()) {
	// finish();
	// }
	// }
	// }
	// }
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
