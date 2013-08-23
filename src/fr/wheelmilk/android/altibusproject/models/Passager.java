package fr.wheelmilk.android.altibusproject.models;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import fr.wheelmilk.android.altibusproject.R;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class Passager implements Parcelable {
	protected String nom;
	protected String prenom;

	private String adresse = null;
	private String adresse2 = null;
	private String ville = null;
	private String codePostal = null;
	private String pays = null;
	private String telephone = null;
	private String email = null;
	
	int position;
	boolean isPrincipal = false;
	boolean valid = false;
	boolean completed = false;
	protected int age;
	protected ArrayList<String> errors;
	
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
	public Passager createPassagerPrincipal(String addr, String addr2, String cp, String _ville, String _pays, String tel, String _email) {
		position = 0;
		isPrincipal = true;
		adresse = addr;
		adresse2 = addr2;
		ville = _ville;
		codePostal = cp;
		pays = _pays;
		telephone = tel;
		email = _email;
		return this;
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
	public TreeMap<String, String> getParams(Context cxt) {
		TreeMap<String, String> _params = new TreeMap<String, String>();
		_params.put("nom" + (position + 1), nom);
		_params.put("prenom" + (position + 1), prenom);
		_params.put("age" + (position + 1), getAgeAsString());
		return _params;
	}
	public TreeMap<String, String> getPassagerPrincipalParams(Context cxt) {
		// Initialisation avec getParams pour avoir nom1, prenom1 et age1
		TreeMap<String, String> _params = new TreeMap<String, String>(getParams(cxt));

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(cxt);
		_params.put("nom", prefs.getString("prefUserLastName", cxt.getString(R.string.saisirNom)));
		_params.put("prenom", prefs.getString("prefUserFirstName", cxt.getString(R.string.pref_user_last_name_summary)));
//		_params.put("age", prefs.getString("prefUserAge", cxt.getString(R.string.pref_user_last_name_summary)));
		_params.put("adresse", prefs.getString("prefUserAddress", cxt.getString(R.string.pref_user_adresse)));
		_params.put("adresse2", prefs.getString("prefUserAddress", "(null)"));
		
//		adresse2 = prefs.getString("prefUserAddress2", cxt.getString(R.string.pref_user_adresse2));
//		if (!TextUtils.isEmpty(adresse2) ) _params.put("adresse2", adresse2);
		_params.put("cp", prefs.getString("prefUserCodePostal", cxt.getString(R.string.pref_user_code_postal)));
		_params.put("ville", prefs.getString("prefUserVille", cxt.getString(R.string.pref_user_ville)));
		_params.put("pays", prefs.getString("prefUserCountry", cxt.getString(R.string.pref_user_countries)));
		_params.put("tel", prefs.getString("prefUserTelephone", cxt.getString(R.string.pref_user_telephone)));
		_params.put("email", prefs.getString("prefUserEmail", cxt.getString(R.string.pref_user_email)));
		return _params;
	}
	public boolean isPassagerPrincipalValid(Resources mRes) {
		errors = new ArrayList<String>();
		boolean resultCode = true;
		
		if (TextUtils.isEmpty(nom) || nom.equals(R.string.saisirNom)) {
			errors.add(mRes.getString(R.string.erreurNom));
		}
		if (TextUtils.isEmpty(prenom) || prenom.equals(mRes.getString(R.string.saisirPrenom))) {
			errors.add(mRes.getString(R.string.erreurPrenom));
		}
		if ( age < 4 || age > 120) {
			errors.add(mRes.getString(R.string.erreurAge));
		}

		if (email == null || TextUtils.isEmpty(email) ) 		errors.add(mRes.getString(R.string.erreurEmptyUserPrefEmail));
		if (telephone == null || TextUtils.isEmpty(telephone)) 	errors.add(mRes.getString(R.string.erreurEmptyUserPrefTel));
		if (adresse == null || TextUtils.isEmpty(adresse)) 		errors.add(mRes.getString(R.string.erreurEmptyUserPrefAdresse));
		if (ville == null || TextUtils.isEmpty(ville)) 			errors.add(mRes.getString(R.string.erreurEmptyUserPrefVille));
		if (codePostal == null || TextUtils.isEmpty(codePostal)) errors.add(mRes.getString(R.string.erreurEmptyUserPrefCodePostal));
		if (pays == null || TextUtils.isEmpty(pays)) 			errors.add(mRes.getString(R.string.erreurEmptyUserPrefPays));

		if (errors.size() > 0) {
			errors.add(0, mRes.getString(R.string.erreurEmptyUserPref));
			resultCode = false;
		}

		return resultCode;
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
		setAge(_age);
		position = _position;
	}	
	public Passager(String _nom, String _prenom, String _age) {
		nom = _nom;
		prenom = _prenom;
		setAge(_age);
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
		try {
			age = Integer.decode(_age);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			age = 0;
		}
	}
	public boolean isEnfant() {
		if (age < 26) return true;
		else return false;
	}
	public Passager(Parcel in) {
		nom = in.readString();
		prenom = in.readString();
		age = in.readInt();
		position = in.readInt();
		isPrincipal = (in.readInt() == 0) ? false : true;
		if (isPrincipal) {
			adresse = in.readString();
			adresse2 = in.readString();
			ville = in.readString();
			codePostal = in.readString();
			pays = in.readString();
			telephone = in.readString();
			email = in.readString();
		}
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
		dest.writeInt(isPrincipal ? 1 : 0);
		if (isPrincipal) {
			dest.writeString(adresse);
			dest.writeString(adresse2);
			dest.writeString(ville);
			dest.writeString(pays);
			dest.writeString(codePostal);
			dest.writeString(telephone);
			dest.writeString(email);
		}
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
