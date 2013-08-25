package fr.wheelmilk.android.altibusproject.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.loopj.android.http.RequestParams;

public class Billet implements Parcelable {
	RequestParams params;
	Passagers passagers;
	GaresArrivee gareA;
	GaresDepart gareD;
	HorrairesAller horrairesA;
	HorrairesRetour horrairesR;
	Reservation reservation = null;
	ResultatPaiement resultatPaiement;
	String nba;
	String nbr;
	Date dr;
	String hr;
	String hra;

	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField
	String ga;
	@DatabaseField
	String gd;
	@DatabaseField
	String rt;
	@DatabaseField(dataType = DataType.DATE_LONG, index = true)
	Date da;
	@DatabaseField
	String ha;
	@DatabaseField
	String haa;
	@DatabaseField
	int nb;
	@DatabaseField
	String pt;	// Montant
	@DatabaseField
	boolean aller = true;
	@DatabaseField(index = true)
	String ref;
	@DatabaseField
	boolean valide = false;
	@DatabaseField
	String nom;
	@DatabaseField
	String prenom;
	@DatabaseField
	int age;	
	@DatabaseField
	int nbEnfant;
	@DatabaseField
	int nbAdulte;

	public Billet() { }
	
	public void prepareForDB(boolean _aller) {
		if(_aller) {
			aller = true;
		} else { 
			aller = false;
			da = dr;
			ha = hr;
			haa = hra;
		}
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append("ga=").append(gareA.ga);
		sb.append(", ").append("gd=").append(gareD.gd);
		sb.append(", ").append("rt=").append(rt);
		sb.append(", ").append("pt=").append(pt);
		sb.append(", ").append("cdr=").append(ref);
		sb.append(", ").append("valide=").append(valide);
		sb.append(", ").append("aller=").append(aller);
		sb.append(", ").append("age=").append(age);
		sb.append(", ").append("prenom=").append(nom);
		sb.append(", ").append("nom=").append(prenom);
		sb.append(", ").append("nbEnfant=").append(nbEnfant);
		sb.append(", ").append("nbAdulte=").append(nbAdulte);
		sb.append(", ").append("nb=").append(nb);

//		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S");
		// dateFormatter.format(da)
		if (aller) {
			sb.append(", ").append("da=").append(da);
			sb.append(", ").append("ha=").append(ha);
			sb.append(", ").append("haa=").append(haa);
		} else {
			sb.append(", ").append("da=").append(dr);
			sb.append(", ").append("ha=").append(hr);
			sb.append(", ").append("haa=").append(hra);
		}
		return sb.toString();
	}
	public int getNbEnfants() {
		return nbEnfant;
	}
	public int getNbAdultes() {
		return nbAdulte;
	}
	public void setAller(boolean b) {
		aller = b;
	}
	public boolean isAller() {
		return aller;
	}
	public boolean isPerime() {
		Calendar today = Calendar.getInstance();  
		Calendar dateArrivee = Calendar.getInstance();  
		today.setTime(new Date());
		dateArrivee.setTime(da);
		if ( dateArrivee.getTimeInMillis() > today.getTimeInMillis() ) {
			return false;
		}
		return true;
	}
	public void setValide(boolean b) {
		valide = b;
	}
	public boolean getValide() {
		return valide;
	}
	public boolean hasHorraireRetour() {
		if (horrairesR != null) return true;
		return false;
	}
	public Billet(Passagers _passagers, GaresDepart _gd, GaresArrivee _ga, Date _da, HorrairesAller _horrairesA, Date _dr, HorrairesRetour _horrairesR) {
		passagers = _passagers;
		horrairesA = _horrairesA;
		horrairesR = _horrairesR;
		gareA = _ga;
		gareD = _gd;
		setDateAllerWithHorraireArrivee(_da, horrairesA);
		if (_dr != null ) setDateRetourWithHorraireArrivee(_dr, horrairesR);
		else dr = null;
		initBillet();
	}
	private void setDateAllerWithHorraireArrivee(Date date, HorrairesAller h) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S", Locale.FRANCE);
		dateFormatter.format(date);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);
		Log.v(getClass().toString(), "Horraire Aller : " + h.haa);
		Log.v(getClass().toString(), "Ancienne Date : " + dateFormatter.format(cal.getTime()));
		cal.set(Calendar.HOUR_OF_DAY, h.heureArriveeOfTheDay());
		cal.set(Calendar.MINUTE, h.minuteArriveeOfTheDay());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Log.v(getClass().toString(), "Nouvelle Date incluant l'arrivée : " + cal.getTime());
		da = cal.getTime();
	}
	private void setDateRetourWithHorraireArrivee(Date date, HorrairesRetour h) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S", Locale.FRANCE);
		dateFormatter.format(date);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);
		Log.v(getClass().toString(), "Ancienne Date : " + dateFormatter.format(cal.getTime()));
		cal.set(Calendar.HOUR_OF_DAY, h.heureArriveeOfTheDay());
		cal.set(Calendar.MINUTE, h.minuteArriveeOfTheDay());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Log.v(getClass().toString(), "Nouvelle Date incluant l'arrivée : " + cal.getTime());
		da = cal.getTime();
	}
	private void initBillet() {
		ga = gareA.ga;
		gd = gareD.gd;
		rt = gareA.rt;
		ha = horrairesA.ha;
		haa = horrairesA.haa;
		nba = horrairesA.nba;
		age = passagers.get(0).getAge();
		nom = passagers.get(0).getNom();
		prenom = passagers.get(0).getPrenom();
		nbEnfant = passagers.getNombreEnfants();
		nbAdulte = passagers.getNombreAdultes();
		nb = passagers.size();	
		if (horrairesR != null) {
			hr = horrairesR.hr;
			hra = horrairesR.hra;
			nbr = horrairesR.nbr;
		}
		if (reservation != null) {
			pt = reservation.getMontantAsString();
			ref = reservation.getRef();
		}
	}
	public String getMontant() {
		return reservation.getMontant().replace("EUR", "€").replace(".", ",");
	}
	public String getRefReservation() {
		return reservation.getRef();
	}
	public String getGareDepart() {
		return gd;
	}
	public String getGareRetour() {
		return ga;
	}
	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public int getNb() {
		return nb;
	}
	public String getPt() {
		return pt;
	}
	public String getPrettyPt() {
		StringBuilder s = new StringBuilder(pt.replace(".", ","));
		s.append("€");
		return s.toString();
	}
	public String getHa() {
		return ha;
	}
	public String getHaa() {
		return haa;
	}
	public HorrairesAller getHorraireAller() {
		return horrairesA;
	}
	public HorrairesRetour getHorraireRetour() {
		return horrairesR;
	}
	public Date getDateAller() {
		return da;
	}
	public void setReservation(Reservation res) {
		reservation = res;
		pt = reservation.getMontantAsString();
		ref = reservation.refReservation;
	}
	public boolean hasValidReservation() {
		if( reservation == null ) return false;
		if( reservation.getRef().equals("ERREUR")) return false;
		return true;
	}
	public RequestParams setParams(Context cxt) {
		TreeMap<String, String> t = passagers.getParams(cxt);
		params = new RequestParams(t);
		params.put("rt", rt);
		params.put("nb", String.valueOf(passagers.size()));
		String _da = new SimpleDateFormat("d/M/yyyy", Locale.FRANCE).format(da);
		params.put("da", _da);
		params.put("ha", ha);
		params.put("haa", haa);
		params.put("nba", nba);
		params.put("nba", nba);
		params.put("version", "999.9");

//		params.put("nba", );
		if (horrairesR != null) {
			String _dr = new SimpleDateFormat("d/M/yyyy", Locale.FRANCE).format(da);
			params.put("dr", _dr);
			params.put("hr", hr);
			params.put("hra", hra);
			params.put("nbr", nbr);
		}
//		params.put("aa", "ne_pas_supprimer");
		return params;
	}
	
	public Billet(Parcel in) {
		passagers = in.readParcelable(Passagers.class.getClassLoader());
		gareA = in.readParcelable( GaresArrivee.class.getClassLoader());
		gareD = in.readParcelable( GaresDepart.class.getClassLoader());
		horrairesA = in.readParcelable( HorrairesAller.class.getClassLoader());
		horrairesR = in.readParcelable( HorrairesRetour.class.getClassLoader());
		reservation = in.readParcelable( Reservation.class.getClassLoader());
		if (horrairesR.heureAller().equals("empty")) horrairesR = null;
		da = new Date(in.readLong());
		dr = new Date(in.readLong());
		if (dr.getTime() == -1 ) dr = null;
		initBillet();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(passagers, flags);
		dest.writeParcelable(gareA, flags);
		dest.writeParcelable(gareD, flags);
		dest.writeParcelable(horrairesA, flags);

		if( horrairesR == null ) {
			horrairesR = new HorrairesRetour("empty", "empty", "empty");
		}
		dest.writeParcelable(horrairesR, flags);
		dest.writeParcelable(reservation, flags);
		dest.writeLong(da.getTime());
		if ( dr == null) {
			dr = new Date(-1);
		}
		dest.writeLong(dr.getTime());
	}
    public static final Parcelable.Creator<Billet> CREATOR = new Parcelable.Creator<Billet>() {
        @Override
		public Billet createFromParcel(Parcel in) {
            return new Billet(in);
        }
 
        @Override
		public Billet[] newArray(int size) {
            return new Billet[size];
        }
    };
}
