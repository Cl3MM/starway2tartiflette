package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

@Root(name="Altibus.data")
public class AltibusDataReservation implements Parcelable {

	@Element(name = "reservation")
	public Reservation reservation;

	public AltibusDataReservation(@Element(name="reservation") Reservation _reservation) {
		reservation = _reservation;
	}
	public String getRefReservation() {
		return reservation.refReservation;
	}
	public String getMontant() {
		return reservation.montant;
	}
	public String getPrettyMontant() {
		return reservation.getPrettyMontant();
	}
	
	public AltibusDataReservation(Parcel in) {
		in.readParcelable(Reservation.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(reservation, flags);
	}
    public static final Parcelable.Creator<AltibusDataReservation> CREATOR = new Parcelable.Creator<AltibusDataReservation>() {
        @Override
		public AltibusDataReservation createFromParcel(Parcel in) {
            return new AltibusDataReservation(in);
        }

		@Override
		public AltibusDataReservation[] newArray(int size) {
			return null;
		}
    };
}