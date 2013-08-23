package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Altibus.data")
public class AltibusDataReservation {

	@Element(name = "reservation")
	public Reservation reservation;
	
	public String getRefReservation() {
		return reservation.refReservation;
	}
	public String getMontant() {
		return reservation.montant;
	}
	public String getPrettyMontant() {
		return reservation.getPrettyMontant();
	}
}