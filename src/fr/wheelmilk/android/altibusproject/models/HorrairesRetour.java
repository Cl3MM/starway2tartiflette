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
			return (hr.contains(":")) ? hr : hr.replace("H", ":");
		}

		@Override
		public String heureArrivee() {
			return (hra.contains(":")) ? hra : hra.replace("H", ":");
		}
		public int heureArriveeOfTheDay() {
			int index = heureArrivee().indexOf(":");
			String heure = hra.substring(0, index);
			return Integer.valueOf(heure);
		}
		public int minuteArriveeOfTheDay() {
			int index = heureArrivee().indexOf(":");
			String min = hra.substring(index+1, hra.length());
			return Integer.valueOf(min);
		}
		// Parcelable implementation
		public HorrairesRetour(Parcel in) {
			readFromParcel(in);
		}
		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(hr);
			dest.writeString(hra);
			dest.writeString(nbr);
		}
		
	    public void readFromParcel(Parcel in) {
	        hr = in.readString();
	        hra = in.readString();
	        nbr = in.readString();
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