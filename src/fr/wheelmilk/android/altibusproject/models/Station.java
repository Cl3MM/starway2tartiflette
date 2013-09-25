package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "stations")
public class Station {
	@Element
	public int station;
}