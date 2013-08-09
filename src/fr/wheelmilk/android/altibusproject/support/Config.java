package fr.wheelmilk.android.altibusproject.support;

import fr.wheelmilk.android.altibusproject.R;

public class Config {

	public static final boolean CALENDAR_VIEW 	= false;
	
	public static final int WEBSERVICE_FAILLURE = -100;
	public static final int EXTRA_FAILURE 		= -200;
	public static final int PAS_D_HORRAIRES 	= -300;
	public static final int SERIALIZATION_FAILURE = -400;

	public static final int GARE_ALLER_CODE 	= 100;
	public static final int GARE_ARRIVEE_CODE 	= 200;
	public static final int DATE_ALLER_CODE 	= 300;
	public static final int HEURE_ALLER_CODE 	= 400;
	public static final int DATE_RETOUR_CODE 	= 500;
	public static final int HEURE_RETOUR_CODE 	= 600;
	
	public static final String[] CONTENT = new String[] { "Réservation",
		"Horraires", "Mes billets", "Historique"
	};
	
//	public static final String[] CONTENT = new String[] { "Calendar", "Camera", "Alarms", "Location" };
    public static final int[] ICONS = new int[] {
            R.drawable.ic_action_acheter_billets,
            R.drawable.ic_action_horraires,
            R.drawable.ic_action_mes_billets,
            R.drawable.ic_action_historique,
    };
}
