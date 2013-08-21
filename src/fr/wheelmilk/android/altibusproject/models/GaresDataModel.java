package fr.wheelmilk.android.altibusproject.models;

import android.os.Parcelable;

public interface GaresDataModel extends Parcelable {
	public String gareName();
	public String gareCode();
	public String heureAller();
	public String heureArrivee();
}