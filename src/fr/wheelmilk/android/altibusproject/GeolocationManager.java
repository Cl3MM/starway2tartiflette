package fr.wheelmilk.android.altibusproject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GeolocationManager<T extends OnGPSLocationManagerStatus> implements LocationListener {

	T parent;

	public GeolocationManager(T t) {
		parent = t;
	}
	@Override
	public void onLocationChanged(Location loc) {
		parent.onGPSLocationFound(loc);
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
