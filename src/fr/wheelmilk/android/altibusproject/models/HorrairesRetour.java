package fr.wheelmilk.android.altibusproject.models;

import org.simpleframework.xml.Element;

import android.os.Parcel;
import android.os.Parcelable;

@Element(name="horaireRetour")
public class HorrairesRetour implements GaresDataModel {
	
		public HorrairesRetour(@Element(name="hr") String _hr, @Element(name = "hra") String _hra, @Element(name = "nbr") String _nbr ) {
			hr = _hr;
			hra = _hra;
			nbr = _nbr;
		}

		@Element
		public String hr;
		
		@Element
		public String hra;
		
		@Element
		public String nbr;

		@Override
		public String gareName() {
			return this.hr.replace("H", ":");
		}

		@Override
		public String gareCode() {
			return this.nbr;
		}

		@Override
		public String heureAller() {
			return this.hr.replace("H", ":");
		}

		@Override
		public String heureRetour() {
			return this.hra.replace("H", ":");
		}
		
		// Parcelable implementation
		public HorrairesRetour(Parcel in) {
			readFromParcel(in);
		}
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(hr);
			dest.writeString(hra);
			dest.writeString(hra);
		}
		
	    public void readFromParcel(Parcel in) {
	        hr = in.readString();
	        hra = in.readString();
	        hra = in.readString();
	    }
		
	    public static final Parcelable.Creator<HorrairesRetour> CREATOR = new Parcelable.Creator<HorrairesRetour>() {
	        @Override
			public HorrairesRetour createFromParcel(Parcel in) {
	            return new HorrairesRetour(in);
	        }
	 
	        @Override
			public HorrairesRetour[] newArray(int size) {
	            return new HorrairesRetour[size];
	        }
	    };
	}