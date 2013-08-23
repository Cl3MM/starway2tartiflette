package fr.wheelmilk.android.altibusproject.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import android.content.Context;

import com.loopj.android.http.RequestParams;

public class Billet {
	RequestParams params;
	Passagers passagers;
	GaresArrivee ga;
	HorrairesAller horrairesA;
	HorrairesRetour horrairesR;
	String rt;
	String nba;
	String nbr;
	Date da;
	Date dr;
	String ha;
	String haa;
	String hr;
	String hra;
	
	public Billet(Passagers _passagers, GaresArrivee _ga, Date _da, HorrairesAller _horrairesA, Date _dr, HorrairesRetour _horrairesR) {
		passagers = _passagers;
		horrairesA = _horrairesA;
		horrairesR = _horrairesR;
		ga = _ga;
		rt = ga.rt;
		da = _da;
		ha = _horrairesA.ha;
		haa = _horrairesA.haa;
		nba = _horrairesA.nba;
		if (horrairesR != null) {
			dr = _dr;
			hr = _horrairesR.hr;
			hra = _horrairesR.hra;
			nbr = _horrairesR.nbr;
		}
	}

	public RequestParams setParams(Context cxt) {
		TreeMap<String, String> t = passagers.getParams(cxt);
		params = new RequestParams(t);
		params.put("rt", rt);
		params.put("nb", String.valueOf(passagers.size()));
		String _da = new SimpleDateFormat("d/M/yyyy", Locale.FRANCE).format(da);
		params.put("da", _da);
		params.put("ha", ha);
		params.put("haa", haa);
		params.put("nba", nba);
		params.put("nba", nba);
		params.put("version", "999.9");

//		params.put("nba", );
		if (horrairesR != null) {
			String _dr = new SimpleDateFormat("d/M/yyyy", Locale.FRANCE).format(da);
			params.put("dr", _dr);
			params.put("hr", hr);
			params.put("hra", hra);
			params.put("nbr", nbr);
		}
//		params.put("aa", "ne_pas_supprimer");
		return params;
	}
}
