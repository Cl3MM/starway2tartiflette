package fr.wheelmilk.android.altibusproject.models;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import fr.wheelmilk.android.altibusproject.R;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class Passager implements Parcelable {
	private String nom;
	private String prenom;
	int position;
	boolean valid = false;
	boolean completed = false;
	private int age;
	private ArrayList<String> errors;
	
	public Passager(String _nom, String _prenom, int _age, int _position) {
		nom = _nom;
		prenom = _prenom;
		age = _age;
		position = _position;
	}
	public Passager(String _nom, String _prenom, int _age) {
		nom = _nom;
		prenom = _prenom;
		age = _age;
		position = -1;
	}
	public boolean isValid(Resources mRes) {
		errors = new ArrayList<String>();
		boolean resultCode = true;
		
		if (TextUtils.isEmpty(nom) || nom.equals(R.string.saisirNom)) {
			errors.add(mRes.getString(R.string.erreurNom));
			resultCode = false;
		}
		if (TextUtils.isEmpty(prenom) || prenom.equals(mRes.getString(R.string.saisirPrenom))) {
			errors.add(mRes.getString(R.string.erreurPrenom));
			resultCode = false;
		}
		if ( age < 4 || age > 120) {
			errors.add(mRes.getString(R.string.erreurAge));
			resultCode = false;
		}

		return resultCode;
	}
	public String errorMessages() {
		StringBuilder s = new StringBuilder("");
		for(String m : errors) {
			s.append( m );
			s.append("\n");
	    }
		return s.toString();
	}
	public void setPosition(int _position ) {
		position = _position;
	}
	public int getPosition() {
		return position;
	}
	public Passager(String _nom, String _prenom, String _age, int _position) {
		nom = _nom;
		prenom = _prenom;
		age = Integer.decode(_age);
		position = _position;
	}	
	public Passager(String _nom, String _prenom, String _age) {
		nom = _nom;
		prenom = _prenom;
		age = Integer.decode(_age);
		position = -1;
	}
	@Override
	public String toString() {
		return prenom + " " + nom + " " + age;
	}
	public String getFullName() {
		return Helper.capitalizeFirstLetter(prenom) + " " + Helper.capitalizeFirstLetter(nom);
	}
	public String getAgeAsString() {
		return String.valueOf(age);
	}
	public String getAgeAsStringWithYear() {
		return getAgeAsString() + " ans";
	}
	public int getAge() {
		return age;
	}
	public void setNom(String _nom) {
		nom = _nom;
	}
	public void setPrenom(String _nom) {
		prenom = _nom;
	}
	
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	
	public void setAge(int _age) {
		age = _age;
	}
	public void setAge(String _age) {
		age = Integer.decode(_age);
	}
	
	public Passager(Parcel in) {
		nom = in.readString();
		prenom = in.readString();
		age = in.readInt();
		position = in.readInt();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nom);
		dest.writeString(prenom);
		dest.writeInt(age);
		dest.writeInt(position);
	}
    public static final Parcelable.Creator<Passager> CREATOR = new Parcelable.Creator<Passager>() {
        @Override
		public Passager createFromParcel(Parcel in) {
            return new Passager(in);
        }
 
        @Override
		public Passager[] newArray(int size) {
            return new Passager[size];
        }
    };
}
