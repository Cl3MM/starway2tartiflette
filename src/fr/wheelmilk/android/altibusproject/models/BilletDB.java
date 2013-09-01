package fr.wheelmilk.android.altibusproject.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class BilletDB implements Parcelable {

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
	@DatabaseField(dataType = DataType.DATE_LONG)
	Date dateValidation;
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

	public BilletDB() { }
	
	public BilletDB(Billet billet, boolean isRetour) {
		ga = billet.gareA.ga;
		gd = billet.gareD.gd;
		rt = billet.gareA.rt;
		aller = isRetour;
		age = billet.passagers.get(0).getAge();
		nom = billet.passagers.get(0).getNom();
		prenom = billet.passagers.get(0).getPrenom();
		nbEnfant = billet.passagers.getNombreEnfants();
		nbAdulte = billet.passagers.getNombreAdultes();
		nb = billet.passagers.size();	
		if (isRetour) {
			ha = billet.horrairesR.hr;
			haa = billet.horrairesR.hra;
		} else {
			ha = billet.horrairesA.ha;
			haa = billet.horrairesA.haa;
		}
		da = setDateAllerWithHorraire(billet, isRetour, haa);

		pt = billet.reservation.getMontantAsString();
		ref = billet.reservation.getRef();
	}

	public String getDateDepartBillet() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(da);
		cal.set(Calendar.HOUR_OF_DAY, heureArriveeOfTheDay(ha));
		cal.set(Calendar.MINUTE, minuteArriveeOfTheDay(ha));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return dateFormatter.format(cal.getTime());
		
	}
	private Date setDateAllerWithHorraire(Billet b, boolean isRetour, String horraire) {
		Date date = (isRetour) ? b.dr : b.da;

//		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S", Locale.FRANCE);
//		dateFormatter.format(date);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);
		
//		Log.v(getClass().toString(), "Ancienne Date : " + dateFormatter.format(cal.getTime()));
		cal.set(Calendar.HOUR_OF_DAY, heureArriveeOfTheDay(horraire));
		cal.set(Calendar.MINUTE, minuteArriveeOfTheDay(horraire));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Log.v(getClass().toString(), "Nouvelle Date incluant l'arrivée : " + cal.getTime());
		return cal.getTime();
	}
	
	public int heureArriveeOfTheDay(String horraire) {
		if(horraire.contains(":")) horraire = horraire.replace(":", "H");
		int index = horraire.indexOf("H");
		String heure = horraire.substring(0, index);
		return Integer.valueOf(heure);
	}
	public int minuteArriveeOfTheDay(String horraire) {
		if(horraire.contains(":")) horraire = horraire.replace(":", "H");
		int index = horraire.indexOf("H");
		String min = horraire.substring(index+1, horraire.length());
		return Integer.valueOf(min);
	}
	public Date getDateAller() {
		return da;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append("ga=").append(ga);
		sb.append(", ").append("gd=").append(gd);
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

//		if (aller) {
			sb.append(", ").append("da=").append(da);
			sb.append(", ").append("ha=").append(ha);
			sb.append(", ").append("haa=").append(haa);
//		} else {
//			sb.append(", ").append("da=").append(da);
//			sb.append(", ").append("ha=").append(hr);
//			sb.append(", ").append("haa=").append(hra);
//		}
		return sb.toString();
	}
	public void setDateValidation(Date _dateValidation) {
		dateValidation = _dateValidation;
	}
	public String getDateValidation() {
		if (dateValidation != null) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.FRANCE);
			StringBuilder strDateValidation = new StringBuilder( "Le " );
			strDateValidation.append(dateFormatter.format(dateValidation));
			return strDateValidation.toString();
		} else {
			return "Billet échu mais non composté";
		}
	}
	public int getNbEnfants() {
		return nbEnfant;
	}
	public int getNbAdultes() {
		return nbAdulte;
	}
	public int getId() {
		return id;
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
	public String getTypeBillet() {

        StringBuilder s = new StringBuilder();
        if (nbEnfant > 0) {
        	s.append(String.valueOf(nbEnfant));
        	s.append(" BILLET");
        	if (nbEnfant > 1) s.append("S");
        	s.append(" -26 ans");
        }
        if (nbAdulte > 0) {
        	if(nbEnfant>0) s.append(" / ");
        	s.append(String.valueOf(nbAdulte));
        	s.append(" BILLET");
        	if (nbAdulte > 1) s.append("S");
        	s.append(" +26 ans");
        }
        return s.toString();
	}
	public void setValide(boolean b) {
		valide = b;
	}
//	public String getDateDepartBillet() {
//		Log.v(getClass().toString(), "Nouvelle da : " + da);
//		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.FRANCE);
//		return dateFormatter.format(da);
//	}
	public boolean isValide() {
		return valide;
	}
	public String getGareDepart() {
		return gd;
	}
	public String getGareRetour() {
		return ga;
	}
	public String getGareArriveeBillet() {
		StringBuilder s = new StringBuilder("Départ : ");
		s.append(ga);
		return s.toString();
	}
	public String getRef() {
		return ref;
	}
	public String getRefForBillet() {
		StringBuilder s = new StringBuilder("#");
		s.append(ref);
		return s.toString();
	}
	public String getFullName() {
		return Helper.capitalizeFirstLetter(prenom) + " " + Helper.capitalizeFirstLetter(nom);
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
	
	public BilletDB(Parcel in) {
		id = in.readInt();
		nbEnfant = in.readInt();
		nbAdulte = in.readInt();
		nb = in.readInt();
		valide = (in.readInt() == 0) ? false : true;
		aller = (in.readInt() == 0) ? false : true;
		age = in.readInt();
		nom = in.readString();
		prenom = in.readString();
		ref = in.readString();
		pt = in.readString();
		ha = in.readString();
		haa = in.readString();
		rt = in.readString();
		gd = in.readString();
		ga = in.readString();
		da = new Date(in.readLong());
		dateValidation = new Date(in.readLong());
		if (dateValidation.getTime() == new Date(-1).getTime() ) dateValidation = null; 
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(nbEnfant);
		dest.writeInt(nbAdulte);
		dest.writeInt(nb);
		dest.writeInt(valide ? 1 : 0);
		dest.writeInt(aller ? 1 : 0);
		dest.writeInt(age);

		dest.writeString(nom);
		dest.writeString(prenom);
		dest.writeString(ref);
		dest.writeString(pt);
		dest.writeString(ha);
		dest.writeString(haa);
		dest.writeString(rt);
		dest.writeString(gd);
		dest.writeString(ga);
		dest.writeLong(da.getTime());
		if (dateValidation != null)	dest.writeLong(dateValidation.getTime());
		else {
			Calendar cal = Calendar.getInstance();
			dest.writeLong(new Date(-1).getTime() );
		}
	}
    public static final Parcelable.Creator<BilletDB> CREATOR = new Parcelable.Creator<BilletDB>() {
        @Override
		public BilletDB createFromParcel(Parcel in) {
            return new BilletDB(in);
        }
 
        @Override
		public BilletDB[] newArray(int size) {
            return new BilletDB[size];
        }
    };
}
