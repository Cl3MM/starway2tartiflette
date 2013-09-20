package fr.wheelmilk.android.altibusproject.models;

import java.text.DecimalFormat;

import android.location.Location;

public class Gares4Geoloc {
	public Location location = null;
	public String name;
	public String distance = null;
	float distanceFloat = -1;
	
	public Gares4Geoloc(String _name, Location _location) {
		name = _name;
		location = _location;
	}
	
	public String getDistance(Location from) {
		if (from == null) return null;
		if(location != null) {
			distanceFloat = location.distanceTo(from);
			StringBuilder distanceStr = new StringBuilder( new DecimalFormat("#.##").format(distanceFloat / 1000) );
			distance = distanceStr.append(" km").toString();
			return distance;
		}
		return null;
	}
	
}