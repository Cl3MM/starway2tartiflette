package fr.wheelmilk.android.altibusproject;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDataModel;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesParams;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;

public abstract class PageFactory extends SherlockFragment implements View.OnClickListener {

	protected String popupColor;

	int fragVal;
	TextView tvGareAller;
	TextView tvGareArrivee;
	TextView tvDateAller;
	TextView tvDateRetour;
	TextView tvHeureAller;
	TextView tvHeureRetour;
	LinearLayout llRetour;
	ToggleButton as;
	ToggleButton ar;
	TimetableMainOnClickListener onClick;
	protected View layoutView;
	protected Bundle savedBundle = null;

	public void setTagResult(Intent data, TextView tv) {
		String result = data.getExtras().getString("result");
		if ( result != null ) {
			GaresDataModel tag = data.getParcelableExtra("tag");
			tv.setText(result);
			tv.setTag(tag);
			// Remise à zéro des champs
			if (tv.equals(tvGareAller)) {
				tvGareArrivee.setText(getResources().getString(R.string.rechercherGareArrivee));
				tvGareArrivee.setTag(null);
			}
			resetTextViews();
		}
	}
	protected void resetTextViews() {
		tvHeureAller.setText(getResources().getString(R.string.rechercherhoraireAller));
		tvHeureRetour.setText(getResources().getString(R.string.rechercherhoraireRetour));
		tvHeureAller.setTag(null);
		tvHeureRetour.setTag(null);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String result = null;
		GaresDataModel tag = null;
		Date date = null;
		switch(resultCode) {
		case Config.WEBSERVICE_FAILLURE:
			Helper.grilledRare(getActivity(), getResources().getString(R.string.errorWebservice));
			break;
		case Config.EXTRA_FAILURE:
			Helper.grilledRare(getActivity(), getResources().getString(R.string.errorExtras));
			break;
		case Config.PAS_D_HORRAIRES:
//			Helper.grilledWellDone(getActivity(), getResources().getString(R.string.erreurPasDHorraire));
			break;
		case Config.SERIALIZATION_FAILURE:
			Helper.grilledWellDone(getActivity(), getResources().getString(R.string.erreurSerialisation));
			break;
		default:
			switch (requestCode) {
			case Config.GARE_ALLER_CODE:
				if (data != null) setTagResult(data, tvGareAller);
				break;
			case Config.GARE_ARRIVEE_CODE:
				if (data != null) setTagResult(data, tvGareArrivee);
				break;
			case Config.DATE_ALLER_CODE:
				date = (Date) data.getSerializableExtra("result");
				if ( date != null ) {
					tvDateAller.setText( Helper.prettifyDate(date, null) );
					tvDateAller.setTag(date);
					tvHeureAller.setText(getResources().getString(R.string.rechercherhoraireAller));
					tvHeureAller.setTag(null);
				}
				break;
			case Config.HEURE_ALLER_CODE:
				setHorrairesTvAfterPopUp(data, tvHeureAller);
				break;
			case Config.DATE_RETOUR_CODE:
				date = (Date) data.getSerializableExtra("result");
				if ( date != null ) {
					tvDateRetour.setText( Helper.prettifyDate(date, null) );
					tvDateRetour.setTag(date);
					tvHeureRetour.setText(getResources().getString(R.string.rechercherhoraireRetour));
					tvHeureRetour.setTag(null);					
				}
				break;
			case Config.HEURE_RETOUR_CODE:
				setHorrairesTvAfterPopUp(data, tvHeureRetour);
				break;
			}
		}	
	}

	private void setHorrairesTvAfterPopUp(Intent data, TextView tv) {
		if (data != null) {
			String result = data.getExtras().getString("result");
			if ( result != null ) {
				GaresDataModel tag = data.getParcelableExtra("tag");
				tv.setText(result);
				tv.setTag(tag);
			}
		}
	}

	static PageFactory init(int val) {
		new UnsupportedOperationException("Please implement this method in child classes");
		return null;
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if( bundle != null ) savedBundle = bundle;

		setUpPopUpColor();
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
	}
	protected void loadInstance(Bundle bundle) {
		Date da = null;
		if (bundle.getLong("dateA") != -1) {
			da = new Date(bundle.getLong("dateA"));
		}
		GaresDepart gd = bundle.getParcelable("gareAl");
		GaresArrivee ga = bundle.getParcelable("gareAr");
		HorrairesAller ha = bundle.getParcelable("horraireA");
		boolean allerSimple = bundle.getBoolean("allersimple");
		Date dr = null;
		if (bundle.containsKey("dateR")) { 
			dr = new Date(bundle.getLong("dateR"));
		}
		HorrairesRetour hr = null;
		if (bundle.containsKey("horraireR")) {
			hr = bundle.getParcelable("horraireR");
		}
		createNewSearchFromPageHorraires(gd, ga, da, ha, dr, hr, allerSimple);
		savedBundle = null;
	}

	protected void setUpPopUpColor() {
		popupColor = Config.POPUP_GREEN_COLOR;
	}
	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.consultation_horraires_layout,
				container, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflateDialog(inflater, container);

		layoutView.findViewById(R.id.llTimetableGareAller).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableGareArrivee).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableDateAller).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableHorraireAller).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableDateRetour).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableHorraireRetour).setOnClickListener(this);
		layoutView.findViewById(R.id.allerSimple).setOnClickListener(this);
		layoutView.findViewById(R.id.allerRetour).setOnClickListener(this);

		as = (ToggleButton) layoutView.findViewById(R.id.allerSimple);
		ar = (ToggleButton) layoutView.findViewById(R.id.allerRetour);
		tvGareAller = (TextView) layoutView.findViewById(R.id.tvTimetableGareAller);
		tvGareArrivee = (TextView) layoutView.findViewById(R.id.tvTimetableGareArrivee);
		tvDateAller = (TextView) layoutView.findViewById(R.id.tvTimetableDateAller);
		tvDateRetour = (TextView) layoutView.findViewById(R.id.tvTimetableDateRetour);
		tvHeureAller = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireAller);
		tvHeureRetour = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireRetour);

		as.setChecked(true);
		ar.setChecked(false);

		llRetour = (LinearLayout) layoutView.findViewById(R.id.llTimetableRetour);
		llRetour.setOnClickListener(this);
		llRetour.setVisibility(View.GONE);
		setDeveloppmentTestData();
		setUpChildrenPages();

		// restoring fragment state after orientation change
		if (savedBundle != null ) {
			loadInstance(savedBundle);
		}
		return layoutView;
	}
	protected void setUpChildrenPages() {
		// Add more layout stuffs to the default if needed 
		new UnsupportedOperationException("Please implement this method in child classes");
	}

	private void setDeveloppmentTestData() {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--  
		Date tomorrow = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--  
		Date afterTomorrow = cal.getTime();
		
		tvDateAller.setTag(tomorrow);
		tvDateAller.setText(Helper.prettifyDate(tomorrow, null));
		tvGareAller.setTag( new GaresDepart("AIX LES BAINS", 45.6884f, 5.9096f) );
		tvGareAller.setText("AIX LES BAINS");
		tvGareArrivee.setTag( new GaresArrivee("CHAMBERY", "CHA851") );
		tvGareArrivee.setText("CHAMBERY");
		tvHeureAller.setTag(new HorrairesAller("10H02", "10H40", "CHA91001"));
		tvHeureAller.setText("10H02 - 10H40");
		tvHeureRetour.setTag(new HorrairesRetour("10H02", "10H40", "CHA91001"));
		tvHeureRetour.setText("10H02 - 10H40");
		tvDateRetour.setTag(afterTomorrow);
		tvDateRetour.setText(Helper.prettifyDate(afterTomorrow, null));
	}

	protected void fadeInAnimation() {
		LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein));
	    lac.setDelay(0.05f);
	    llRetour.setLayoutAnimation(lac); 
	}
	protected void fadeOutAnimation() {
		LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout));
	    lac.setDelay(0.05f);
	    llRetour.setLayoutAnimation(lac); 
	}
	
    public class MyScaler extends ScaleAnimation {

        private View mView;
        private LayoutParams mLayoutParams;
        private int mMarginBottomFromY, mMarginBottomToY;
        private boolean mVanishAfter = false;

        public MyScaler(float fromX, float toX, float fromY, float toY, int duration, View view,
                boolean vanishAfter) {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
            	mView.setVisibility(View.VISIBLE);
                int newMarginBottom = mMarginBottomFromY
                        + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,
                    mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
            } else if (mVanishAfter) {
                mView.setVisibility(View.GONE);
            }
        }

    }

	protected void toggleAllerSimple() {
		if (as.isChecked() && ar.isChecked()) {
			as.setChecked(true);
			ar.setChecked(false);
//			fadeOutAnimation(); 
			disableRetourField();
			updateMontant();
//			llRetour.startAnimation(new LinearLayoutVerticalScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f, 500, llRetour, true));
		} else {
			as.setChecked(true);
			ar.setChecked(false);
		}
	}
	protected void disableRetourField() {
		llRetour.setVisibility(View.GONE);
		tvHeureRetour.setTag(null);
		tvHeureRetour.setText(getString(R.string.rechercherhoraireRetour));
		tvDateRetour.setTag(null);
		tvDateRetour.setText(getString(R.string.rechercheDateRetour));
	}
	protected void updateMontant() {
		new UnsupportedOperationException("Please implement this method in child classes");
	}
	protected void toggleAllerRetour() {
		if (as.isChecked() && ar.isChecked()) {
			as.setChecked(false);
			ar.setChecked(true);
//			fadeInAnimation();
			llRetour.setVisibility(View.VISIBLE);
//			llRetour.startAnimation(new LinearLayoutVerticalScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, 500, llRetour, false));
		} else {
			as.setChecked(false);
			ar.setChecked(true);
		}
	}
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		String gareAllerText = (String) this.tvGareAller.getText();
		String gareArriveeText = (String) this.tvGareArrivee.getText();

		// Arrghhh Android 14 suxxx, I had to convert the switch statement to if-else 
		// because of a problem caused by lookup of R.id.xxx
		//
		if (vid == R.id.allerSimple) {
			toggleAllerSimple();
		} else if (vid == R.id.allerRetour) {
			toggleAllerRetour();
		} else if (vid == R.id.llTimetableGareAller) {
			startGeolocSelector();
//			startGareAllerPopUpActivity();
		} else if (vid == R.id.llTimetableGareArrivee) {
			startGareArriveePopUpActivity(v, gareAllerText);
		} else if (vid == R.id.llTimetableDateAller) {
			startDateAllerPickerActivity(v, gareAllerText, gareArriveeText, Config.DATE_ALLER_CODE);
		} else if (vid == R.id.llTimetableDateRetour) {
			startDateAllerPickerActivity(v, gareAllerText, gareArriveeText, Config.DATE_RETOUR_CODE);
		} else if (vid == R.id.llTimetableHorraireAller) {
			startHorrairePopUpActivity(v, gareAllerText, gareArriveeText, Config.HEURE_ALLER_CODE);
		} else if (vid == R.id.llTimetableHorraireRetour) {
			startHorrairePopUpActivity(v, gareAllerText, gareArriveeText, Config.HEURE_RETOUR_CODE);
		} 
			else {
			Helper.grilledRare(getActivity(), "Erreur inconnue");
		}
	}

	protected void startGeolocSelector() {
		String title = getString(R.string.gareAcoteDlgTitle);
		String choix1 = getString(R.string.voirGaresACote);
		String choix2 = getString(R.string.voirToutesGares);
		String[] choix = new String[] { choix1, choix2 };

		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
		builder.setTitle(title);
		builder.setItems(choix,  new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				onGeolocMethodSelected(position);
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}
	protected void startHorrairePopUpActivity(View v, String gareAllerText, String gareArriveeText, int code) {
		if (this.tvGareAller.getTag() != null && !this.tvGareArrivee.getText().equals(getResources().getString(R.string.rechercherGare)) && this.tvGareArrivee.getTag() != null && this.tvDateAller.getTag() != null) {
			// On récupère les tags des différents champs pour construire les paramètres à envoyer par intent à l'activité
			Date da = (Date) tvDateAller.getTag();
			Date dr = null; 
			if (this.tvDateRetour.getTag() != null) dr = (Date) this.tvDateRetour.getTag();
			if (code == Config.HEURE_RETOUR_CODE && dr == null) {
				Helper.grilledRare(getActivity(), getResources().getString(R.string.erreurDateRetour));
			} else {
				GaresArrivee rt = (GaresArrivee) this.tvGareArrivee.getTag();
				
				// Construction des paramètres à envoyer
				HorrairesParams params = new HorrairesParams(da, dr, rt, gareAllerText, gareArriveeText);				
	
				// Création de l'intent
				Intent i = new Intent(this.getActivity(), HorrairesPopUpActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
	
				// Sérialisation des paramètres
				Bundle b = new Bundle();
				b.putParcelable("params", params);
				i.putExtras(b);
				i.putExtra("popupColor", popupColor);
				if (code == Config.HEURE_ALLER_CODE) {
					i.putExtra("title", getResources().getString(R.string.horrairesAller));
				} else {
					i.putExtra("title", getResources().getString(R.string.horraireRetour));
				}
				i.putExtra("code", code);
				//  Démarage de l'activité
				startActivityForResult(i, code);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		} else {
			Helper.grilledRare(getActivity(), getResources().getString(R.string.erreurChampsNonRemplis));
		}		
	}

	protected void startDateAllerPickerActivity(View v, String gareAllerText, String gareArriveeText, int code) {
		if (code == Config.DATE_ALLER_CODE) {
			tvDateRetour.setTag(null);
			tvDateRetour.setText(getResources().getString(R.string.rechercheDateRetour));
		}
		Intent i = new Intent(this.getActivity(), DatePickerPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("title", getResources().getString(R.string.dateDepart));
		i.putExtra("gareDepart", gareAllerText);
		i.putExtra("gareArrivee", gareArriveeText);
		i.putExtra("code", code);
		i.putExtra("popupColor", popupColor);
		i.putExtra("date", (Date) tvDateAller.getTag());
		startActivityForResult(i, code);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	protected void startGareAllerPopUpActivity(boolean geoloc) {
		Intent i = new Intent(getActivity(), GareAllerPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("title", getString(R.string.garesAllerTitle));
		i.putExtra("popupColor", popupColor);
		i.putExtra("geoloc", geoloc);
		startActivityForResult(i, Config.GARE_ALLER_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	protected void startGareArriveePopUpActivity(View v, String gareAllerText) {
		if (TextUtils.isEmpty(gareAllerText) || gareAllerText.equals(getString(R.string.rechercherGare)) || tvGareAller.getTag() == null) {
			Helper.grilledRare(getActivity(), getString(R.string.errorGareAller));
		} else {
			Intent i = new Intent(this.getActivity(), GareArriveePopUp.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			i.putExtra("title", getString(R.string.garesArriveeTitle));
			i.putExtra("gareDepart", gareAllerText);
			i.putExtra("popupColor", popupColor);
			startActivityForResult(i, Config.GARE_ARRIVEE_CODE);
			this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

	// Doit on chercher parmi les gares à proximité ?
	// Position 0 = GPS
	// Position 1 = toutes les gares
	protected void onGeolocMethodSelected(int position) {
		switch (position) {
		// oui
		case 0:
			geoLocate();
			break;
		// non
		case 1:
			startGareAllerPopUpActivity(false);
			break;
		}
	}

	protected void geoLocate() {
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
		boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		Log.v(getClass().toString(), "GPS :" + (statusOfGPS ? "ON" : "OFF"));
		if (statusOfGPS) startGareAllerPopUpActivity(true);
		else buildAlertMessageNoGps();
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
		builder.setMessage(
				"Souhaitez vous afficher l'écran des paramètres de Géolocalisation ?")
				.setCancelable(false)
				.setTitle("Votre GPS semble désactivé")
				.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick( final DialogInterface dialog, final int id) {
								startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("Non merci", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onSaveInstanceState(Bundle data) {
	   super.onSaveInstanceState(data);
	   data.putString("action", "nouvelAchat");
	   data.putParcelable("gareAl", (GaresDepart) tvGareAller.getTag());
	   data.putParcelable("gareAr", (GaresArrivee) tvGareArrivee.getTag());
	   data.putParcelable("horraireA", (HorrairesAller) tvHeureAller.getTag());
	   data.putBoolean("allersimple", as.isChecked());
	    Date da = (Date) tvDateAller.getTag();
	    Date dr = null;
	    if (da == null) data.putLong("dateA", -1 );
	    else data.putLong("dateA", da.getTime() );

		if (tvDateRetour.getTag() != null) {
			dr = (Date) tvDateRetour.getTag();
			data.putLong("dateR", dr.getTime());
		}

	    if ( tvHeureRetour.getTag() != null ) {
	    	data.putParcelable("horraireR", (HorrairesRetour) tvHeureRetour.getTag());
	    }	    
	}
	
	/***
	 * Remplit le fragment avec les données sauvegardées dans le bundle lors d'un changement d'orientation
	 * ou lorsque l'on clique sur "Acheter le billet" dans l'écran Horraires
	 * @param _gd
	 * @param _ga
	 * @param _da
	 * @param _ha
	 * @param _dr
	 * @param _hr
	 * @param allerSimple
	 */
	public void createNewSearchFromPageHorraires(GaresDepart _gd, GaresArrivee _ga, Date _da, 
		HorrairesAller _ha, Date _dr, HorrairesRetour _hr, boolean allerSimple) {
		tvGareAller.setTag(_gd);
		if (_gd == null) tvGareAller.setText(getString(R.string.rechercherGareDepart));
		else tvGareAller.setText(_gd.gareName());
		
		tvGareArrivee.setTag(_ga);
		if (_ga == null) tvGareArrivee.setText(getString(R.string.rechercherGareArrivee));
		else tvGareArrivee.setText(_ga.gareName());
		
		tvDateAller.setTag(_da);
		if (_da == null) tvDateAller.setText(getString(R.string.rechercheDateAller));
		else tvDateAller.setText(Helper.prettifyDate(_da, null));
		
		tvHeureAller.setTag(_ha);
		if (_dr == null) 
			tvHeureAller.setText(getString(R.string.rechercherhoraireAller));
		else {
			StringBuilder s = new StringBuilder(_ha.heureAller());
			s.append(" - ").append(_ha.heureArrivee());
			tvHeureAller.setText(s);
		}		
		
		if (allerSimple) {
			as.setChecked(true);
			ar.setChecked(false);
			disableRetourField();
		} else {
			as.setChecked(false);
			ar.setChecked(true);
			llRetour.setVisibility(View.VISIBLE);
		}

		tvHeureAller.setTag(_ha);
		if (_ha == null) 
			tvHeureAller.setText(getString(R.string.rechercherhoraireAller));
		else {
			StringBuilder s = new StringBuilder(_ha.heureAller());
			s.append(" - ").append(_ha.heureArrivee());
			tvHeureAller.setText(s);
		}		
		
		tvDateRetour.setTag(_dr);
		if (_dr == null) tvDateRetour.setText(getString(R.string.rechercheDateRetour));
		else {
			tvDateRetour.setText(Helper.prettifyDate(_dr, null));
			llRetour.setVisibility(View.VISIBLE);
		}
		tvHeureAller.setTag(_ha);
		if (_hr == null) 
			tvHeureRetour.setText(getString(R.string.rechercherhoraireRetour));
		else {
			StringBuilder s = new StringBuilder(_hr.heureAller());
			s.append(" - ").append(_hr.heureArrivee());
			tvHeureRetour.setText(s);
		}
	}
}