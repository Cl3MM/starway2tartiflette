package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;

@Element(name="pays")
public class Pays {

	@Element(name = "paysFR")
	public String paysFr;

	public Pays(@Element(name = "paysFR") String _paysFr) {
		paysFr = _paysFr;
	}

	public String getPays() {
		return paysFr;
	}

}