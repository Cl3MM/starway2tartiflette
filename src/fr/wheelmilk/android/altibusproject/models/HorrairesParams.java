package fr.wheelmilk.android.altibusproject.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.wheelmilk.android.altibusproject.support.Helper;

import android.os.Parcel;
import android.os.Parcelable;

public class HorrairesParams implements Parcelable {
	Date dateAller;
	Date dateRetour;
	String route;
	String gareAller;
	String gareArrivee;

//	http://www.altibus.com/sw/altibus/data.aspx?tip=horaires&da=03/08/2013&dr=03/08/2013&rt=CHA861

	public HorrairesParams(Date da, Date dr, GaresArrivee rt, String gareAller, String gareArrivee ) {
		this.dateAller = da;
		this.dateRetour = dr;
		this.route = rt.gareCode();		
		this.gareAller = gareAller;
		this.gareArrivee = gareArrivee;
	}
	public String gareAller() {
		return this.gareAller;
	}
	public String gareArrivee() {
		return this.gareArrivee;
	}
	public String prettyDateAller() {
		return Helper.prettifyDate(dateAller, "EEEE d MMMM yyyy");
	}
	public String dateAller() {
		return new SimpleDateFormat("d/M/yyyy", Locale.FRANCE).format(dateAller);
	}
	public String prettyDateRetour() {
		return Helper.prettifyDate(dateRetour, "EEEE d MMMM yyyy");
	}
	public String dateRetour() {
		if ( this.dateRetour != null )
			return new SimpleDateFormat("d/M/yyyy", Locale.FRANCE).format(this.dateRetour);
		else return null;
	}
	public String route() {
		return this.route;
	}
	
	// Parcelable implementation
	public HorrairesParams(Parcel in) {
		gareAller 	= in.readString();
		gareArrivee = in.readString();
		route 		= in.readString();
		dateAller 	= new Date(in.readLong());
		dateRetour 	= new Date(in.readLong());
		if (dateRetour.getTime() == new Date(-1).getTime()) dateRetour = null;

	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(gareAller);
		dest.writeString(gareArrivee);
		dest.writeString(route);
		dest.writeLong(dateAller.getTime());
		if (dateRetour == null) dateRetour = new Date(-1);
		dest.writeLong(dateRetour.getTime());
	}
	
    public static final Parcelable.Creator<HorrairesParams> CREATOR = new Parcelable.Creator<HorrairesParams>() {
        @Override
		public HorrairesParams createFromParcel(Parcel in) {
            return new HorrairesParams(in);
        }
 
        @Override
		public HorrairesParams[] newArray(int size) {
            return new HorrairesParams[size];
        }
    };
}
