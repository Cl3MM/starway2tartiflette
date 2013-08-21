package fr.wheelmilk.android.altibusproject.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PassagerSimpleASupprimer extends Passager {

	public PassagerSimpleASupprimer(String _nom, String _prenom, int _age) {
		super(_nom, _prenom, _age);
	}
	public PassagerSimpleASupprimer(Parcel in) {
		super(in);
	}
    public PassagerSimpleASupprimer(String _nom, String _prenom, String _age) {
    	super(_nom, _prenom, _age);
	}
	public static final Parcelable.Creator<PassagerSimpleASupprimer> CREATOR = new Parcelable.Creator<PassagerSimpleASupprimer>() {
        @Override
		public PassagerSimpleASupprimer createFromParcel(Parcel in) {
            return new PassagerSimpleASupprimer(in);
        }
 
        @Override
		public PassagerSimpleASupprimer[] newArray(int size) {
            return new PassagerSimpleASupprimer[size];
        }
    };
    @Override
	public int describeContents() {
		return 2;
	}
}
