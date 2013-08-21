package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;

@Element(name="horaireAller")
public class HorrairesAller implements GaresDataModel {
	
	public HorrairesAller(@Element(name="ha") String _ha, @Element(name = "haa") String _haa, @Element(name = "nba") String _nba ) {
		ha = _ha;
		haa = _haa;
		nba = _nba;
	}
	@Element
	public String ha;
	
	@Element
	public String haa;
	
	@Element
	public String nba;

	@Override
	public String gareName() {
		return ha.replace("H", ":");
	}
	@Override
	public String heureAller() {
		return ha.replace("H", ":");
	}
	@Override
	public String heureArrivee() {
		return haa.replace("H", ":");
	}
	@Override
	public String gareCode() {
		return this.nba;
	}
	
	// Parcelable implementation
	public HorrairesAller(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ha);
		dest.writeString(haa);
		dest.writeString(nba);
	}
	
    public void readFromParcel(Parcel in) {
        ha = in.readString();
        haa = in.readString();
        nba = in.readString();
    }
	
    public static final Parcelable.Creator<HorrairesAller> CREATOR = new Parcelable.Creator<HorrairesAller>() {
        @Override
		public HorrairesAller createFromParcel(Parcel in) {
            return new HorrairesAller(in);
        }
 
        @Override
		public HorrairesAller[] newArray(int size) {
            return new HorrairesAller[size];
        }
    };
}