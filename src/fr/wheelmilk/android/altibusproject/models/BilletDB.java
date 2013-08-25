package fr.wheelmilk.android.altibusproject.models;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class BilletDB extends Billet {
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
}
