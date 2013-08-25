package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="xml")
public class ResultatPaiement {
	@Element
	public int cdr;
	@Element
	public float version;
	@Element
	public String reference;
	
	public int getCdr() {
		return cdr;
	}
	public float getVersion() {
		return version;
	}
	public String getVersionAsString() {
		return String.valueOf(version);
	}
	public String getReference() {
		return reference;
	}
}

//<?xml version="1.0" encoding="ISO-8859-1"?>
//<xml>
//	<cdr>-5</cdr>
//	<version>3.0</version>
//	<reference>CHA3366</reference>
//</xml>
