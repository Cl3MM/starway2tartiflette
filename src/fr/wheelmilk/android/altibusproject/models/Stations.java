package fr.wheelmilk.android.altibusproject.models;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Altibus.data")
public class Stations {

	public Stations(@ElementList(inline=true) List<Station> _stations) {
		stations = _stations;
	}
	@ElementList(name = "stations", inline=true)
	public List<Station> stations;

	public boolean isStation() {
		boolean isStation = false;
		for(Station station : stations) {
			if( station.station == 1) isStation = true;
		}
		return isStation;
	}
}
//<?xml version="1.0" encoding="UTF-8" ?><Altibus.data><stations><station>0</station></stations></Altibus.data>
