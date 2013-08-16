package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;

//@Element(name = "garesArrivee")
//public class GaresArrivee {
//	@Element
//	public String ga;
//
//	@Element
//	public String rt;
//}

@Element(name="garesArrivee")
public class GaresArrivee implements GaresDataModel {
	
	public GaresArrivee(@Element(name="ga") String _ga, @Element(name = "rt") String _rt ) {
		ga = _ga;
		rt = _rt;
	}	
	
	@Element
	public String ga;
	
	@Element
	public String rt;
	
	@Override
	public String gareName() {
		return this.ga;
	}
	
	@Override
	public String gareCode() {
		return this.rt;
	}

	@Override
	public String heureAller() {
		return null;
	}

	@Override
	public String heureRetour() {
		return null;
	}
	
	// Parcelable implementation
	public GaresArrivee(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ga);
		dest.writeString(rt);
	}
	
    public void readFromParcel(Parcel in) {
        ga = in.readString();
        rt = in.readString();
    }
	
    public static final Parcelable.Creator<GaresArrivee> CREATOR = new Parcelable.Creator<GaresArrivee>() {
        @Override
		public GaresArrivee createFromParcel(Parcel in) {
            return new GaresArrivee(in);
        }
 
        @Override
		public GaresArrivee[] newArray(int size) {
            return new GaresArrivee[size];
        }
    };
}