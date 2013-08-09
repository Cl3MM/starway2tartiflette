package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;

@Element(name="garesDepart")
public class GaresDepart implements GaresDataModel, Parcelable {

	public GaresDepart(@Element(name="gd") String _gd, @Element(name = "lat") float _lat, @Element (name = "lon") float _lon ) {
		gd = _gd;
		lat = _lat;
		lon = _lon;
	}	
	@Element
	public String gd;
	
	@Element
	public float lat;
	
	@Element
	public float lon;

	@Override
	public String gareName() {
		return this.gd;
	}

	@Override
	public String gareCode() {
		return this.gd;
	}

	@Override
	public String heureAller() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String heureRetour() {
		// TODO Auto-generated method stub
		return null;
	}

	// Parcelable implementation
	public GaresDepart(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(gd);
		dest.writeFloat(lat);
		dest.writeFloat(lon);
	}
	
    public void readFromParcel(Parcel in) {
        gd = in.readString();
        lat = in.readFloat();
        lon = in.readFloat();
    }
	
    public static final Parcelable.Creator<GaresDepart> CREATOR = new Parcelable.Creator<GaresDepart>() {
        @Override
		public GaresDepart createFromParcel(Parcel in) {
            return new GaresDepart(in);
        }
 
        @Override
		public GaresDepart[] newArray(int size) {
            return new GaresDepart[size];
        }
    };
}
