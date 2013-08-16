package fr.wheelmilk.android.altibusproject.models;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Passagers extends ArrayList<Passager> implements Parcelable {
	private ArrayList<Passager> passagers;
	
	public Passagers(ArrayList<Passager> _passagers) {
		passagers = new ArrayList<Passager>();
		passagers = _passagers;
	}
	public Passagers(Passager _passager) {
		passagers = new ArrayList<Passager>();
		passagers.add(_passager);
	}
	public Passager first() {
		if (size() > 0) {
			return get(0);
		} else {
			return null;
		}
	}
	
	public Passagers(Parcel in) {
		in.readTypedList(this, null);
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this);		
	}
    public static final Parcelable.Creator<Passagers> CREATOR = new Parcelable.Creator<Passagers>() {
        @Override
		public Passagers createFromParcel(Parcel in) {
            return new Passagers(in);
        }
 
        @Override
		public Passagers[] newArray(int size) {
            return new Passagers[size];
        }
    };
}
