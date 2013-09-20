package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import java.util.TreeMap;

import com.loopj.android.http.RequestParams;

import fr.wheelmilk.android.altibusproject.models.Gares4Geoloc;
import fr.wheelmilk.android.altibusproject.support.Config;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class GareAllerPopUp extends ActivityPopUpFactory implements OnGPSLocationManagerStatus {
//	protected static final String popupColor = Config.POPUP_GREEN_COLOR;

	boolean geoloc = false;
	public static double longitude = 0;
	public static double latitude = 0;
	public Location location = null;
	private boolean acquiringGPS = false;
	boolean isWebserviceOk = false;
	boolean isGpsOk = false;

	@Override
	protected PopUpArrayAdapter buildArrayAdpater( ArrayList<Gares4Geoloc> _arrayList) {
		PopUpArrayAdapter _arrayAdapter = new PopUpArrayAdapter(this, geoloc,
		R.layout.popup_list_item, _arrayList, popupColor);
		return _arrayAdapter;
	}

	@Override
	public void initialize( Bundle extras ) {
		super.initialize(extras);
		geoloc = extras.getBoolean("geoloc");
		if (geoloc) {
			acquiringGPS = true;
			mlocListener = new GeolocationManager(this);
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		}
	}

	@Override
	protected void startAsyncHTTPRequest() {
		RequestParams params = new RequestParams();
		params.put("tip", "gps7");
		new AltibusWebservice<ActivityPopUpFactory>(this, "sw/altibus/data.aspx?", params);
	}

	@Override
	protected void addLayoutCustomizations() {
		LinearLayout llHorrairesDepart = (LinearLayout) findViewById(R.id.llHorrairesDepart);
		llHorrairesDepart.setVisibility(View.GONE);
		LinearLayout ll1 = (LinearLayout) findViewById(R.id.llBgToChange2);
		ll1.setBackgroundColor(popupColor);

		if (popupColor == Color.parseColor(Config.POPUP_ORANGE_COLOR)) {
			listeDesGares.setSelector(getResources().getDrawable(R.drawable.orange_horraire_list_selector));
		}
	}

	@Override
	public void onGPSLocationFound(Location loc) {
		if (acquiringGPS) { // 1ere coordonnées trouvées
			location = loc;
	        latitude = loc.getLatitude();
	        longitude = loc.getLongitude();
			String text = "My current location is: " +	"Latitud = " + loc.getLatitude() +	"Longitud = " + loc.getLongitude();
			Log.v(getClass().toString(), text);
			isGpsOk = true;
			acquiringGPS = false;
			dismissProgressBar();
			populateListView();
		} //else { // On annule le listener pour ne pas recevoir d'autres coordonnées
//	        latitude = loc.getLatitude();
//	        longitude = loc.getLongitude();
//			String text = "Latitud = " + loc.getLatitude() +	"Longitud = " + loc.getLongitude();
//			Log.v(getClass().toString(), "Nouvelles coordonnées recues :" + text);
//			mlocListener = null;
//			locationManager = null;
//		}
	}

	@Override
	public void onWebserviceSuccess(String xmlString) {
		isWebserviceOk = true;
		super.onWebserviceSuccess(xmlString);
	}
	@Override
	protected synchronized void dismissProgressBar() {
		if (isWebserviceOk && !geoloc) super.dismissProgressBar();
		else if (isWebserviceOk && geoloc && isGpsOk) super.dismissProgressBar();
	}
	
	@Override
	protected synchronized void populateListView() {
		if (isWebserviceOk && !geoloc) super.populateListView();
		else if (isWebserviceOk && geoloc && isGpsOk) {
			// Si gareWithDistance n'est pas null :
			if (!garesWithDistance.isEmpty()) {
				TreeMap<Float, Gares4Geoloc> garesSorted = altibusDataModel.garesDeLaMuerte(location, 50);
				garesWithDistance = new ArrayList<Gares4Geoloc>( garesSorted.values() );
			} else {
				
			}
			super.populateListView();
//			if (geoloc) {
//				TextView tvEmpty = (TextView) findViewById(R.id.tvEmptyList);
////				tvEmpty.setText(getString(R.string.erreurPasDeGareACote));
//			}
		}
	}
}