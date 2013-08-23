package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;

@Element(name="reservation")
public class Reservation implements Parcelable {

	public Reservation(	@Element(name = "montant") String _montant, 	@Element(name = "refReservation") String _refReservation) {
		refReservation = _refReservation;
		montant = _montant;
	}
	@Element(name = "montant")
	public String montant;
	
	@Element(name = "refReservation")
	public String refReservation;

	
	public String getMontant() {
		return montant;
	}
	public String getRef() {
		return refReservation;
	}

	public String getPrettyMontant() {
		StringBuilder str = new StringBuilder();
		str.append("Prix : ");
		str.append( montant.replace("EUR", "€").replace(".", ",") );
		return str.toString();
	}
	// Parcelable implementation
	public Reservation(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(montant);
		dest.writeString(refReservation);
	}
	
    public void readFromParcel(Parcel in) {
        montant = in.readString();
        refReservation = in.readString();
    }
	
    public static final Parcelable.Creator<Reservation> CREATOR = new Parcelable.Creator<Reservation>() {
        @Override
		public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }
 
        @Override
		public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };
}
