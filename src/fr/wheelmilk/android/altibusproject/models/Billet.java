package fr.wheelmilk.android.altibusproject.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.RequestParams;

public class Billet implements Parcelable {
	RequestParams params;
	Passagers passagers;
	GaresArrivee ga;
	GaresDepart gd;
	HorrairesAller horrairesA;
	HorrairesRetour horrairesR;
	Reservation reservation = null;
	String rt;
	String nba;
	String nbr;
	Date da;
	Date dr;
	String ha;
	String haa;
	String hr;
	String hra;
	
	public Billet() { }
	public Billet(Passagers _passagers, GaresDepart _gd, GaresArrivee _ga, Date _da, HorrairesAller _horrairesA, Date _dr, HorrairesRetour _horrairesR) {
		passagers = _passagers;
		horrairesA = _horrairesA;
		horrairesR = _horrairesR;
		ga = _ga;
		gd = _gd;
		da = _da;
		dr = _dr;
		initBillet();
	}

	private void initBillet() {
		rt = ga.rt;
		ha = horrairesA.ha;
		haa = horrairesA.haa;
		nba = horrairesA.nba;
		if (horrairesR != null) {
			hr = horrairesR.hr;
			hra = horrairesR.hra;
			nbr = horrairesR.nbr;
		}
	}
	public String getMontant() {
		return reservation.getMontant().replace("EUR", "€").replace(".", ",");
	}
	public String getRefReservation() {
		return reservation.getRef();
	}
	
	public void setReservation(Reservation res) {
		reservation = res;
	}
	public boolean hasValidReservation() {
		if( reservation == null ) return false;
		if( reservation.getRef().equals("ERREUR")) return false;
		return true;
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
	
	public Billet(Parcel in) {
		passagers = in.readParcelable(Passagers.class.getClassLoader());
		ga = in.readParcelable( GaresArrivee.class.getClassLoader());
		gd = in.readParcelable( GaresDepart.class.getClassLoader());
		horrairesA = in.readParcelable( HorrairesAller.class.getClassLoader());
		horrairesR = in.readParcelable( HorrairesRetour.class.getClassLoader());
		reservation = in.readParcelable( Reservation.class.getClassLoader());
		if (horrairesR.heureAller().equals("empty")) horrairesR = null;
		da = new Date(in.readLong());
		dr = new Date(in.readLong());
		if (dr.getTime() == -1 ) dr = null;
		initBillet();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(passagers, flags);
		dest.writeParcelable(ga, flags);
		dest.writeParcelable(gd, flags);
		dest.writeParcelable(horrairesA, flags);

		if( horrairesR == null ) {
			horrairesR = new HorrairesRetour("empty", "empty", "empty");
		}
		dest.writeParcelable(horrairesR, flags);
		dest.writeParcelable(reservation, flags);
		dest.writeLong(da.getTime());
		if ( dr == null) {
			dr = new Date(-1);
		}
		dest.writeLong(dr.getTime());
	}
    public static final Parcelable.Creator<Billet> CREATOR = new Parcelable.Creator<Billet>() {
        @Override
		public Billet createFromParcel(Parcel in) {
            return new Billet(in);
        }
 
        @Override
		public Billet[] newArray(int size) {
            return new Billet[size];
        }
    };
}
