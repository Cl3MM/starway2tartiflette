package fr.wheelmilk.android.altibusproject;

import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.app.SherlockFragment;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDataModel;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesParams;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


public abstract class PageFactory extends SherlockFragment implements View.OnClickListener { // , TtAllerDlg.TtAllerDlgListener {

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

	protected void startGareAllerPopUpActivity() {
		new UnsupportedOperationException("Please implement this method in child classes");
	}

	public void setTagResult(Intent data, TextView tv) {
		String result = data.getExtras().getString("result");
		if ( result != null ) {
			GaresDataModel tag = data.getParcelableExtra("tag");
			tv.setText(result);
			tv.setTag(tag);
			if (tv.equals(tvGareAller)) {
				tvGareArrivee.setText(getResources().getString(R.string.rechercherGare));
				tvGareArrivee.setTag(null);
			}
		}
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
			Helper.grilledWellDone(getActivity(), getResources().getString(R.string.erreurPasDHorraire));
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
	}

	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.consultation_horraires_layout,
				container, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		onClick = new TimetableMainOnClickListener();

		View layoutView = inflateDialog(inflater, container);

		layoutView.findViewById(R.id.llTimetableGareAller).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableGareArrivee).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableDateAller).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableHorraireAller).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableDateRetour).setOnClickListener(this);
		layoutView.findViewById(R.id.llTimetableHorraireRetour).setOnClickListener(this);
		layoutView.findViewById(R.id.allerSimple).setOnClickListener(this);
		layoutView.findViewById(R.id.allerRetour).setOnClickListener(this);

		this.as = (ToggleButton) layoutView.findViewById(R.id.allerSimple);
		this.ar = (ToggleButton) layoutView.findViewById(R.id.allerRetour);
		this.tvGareAller = (TextView) layoutView.findViewById(R.id.tvTimetableGareAller);
		this.tvGareArrivee = (TextView) layoutView.findViewById(R.id.tvTimetableGareArrivee);
		this.tvDateAller = (TextView) layoutView.findViewById(R.id.tvTimetableDateAller);
		this.tvDateRetour = (TextView) layoutView.findViewById(R.id.tvTimetableDateRetour);
		this.tvHeureAller = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireAller);
		this.tvHeureRetour = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireRetour);

		this.as.setChecked(true);
		this.ar.setChecked(false);

		this.llRetour = (LinearLayout) layoutView.findViewById(R.id.llTimetableRetour);
		llRetour.setOnClickListener(this);
		llRetour.setVisibility(View.GONE);
		setDeveloppmentTestData();
		return layoutView;
	}
	private void setDeveloppmentTestData() {
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--  
		Date tomorrow = cal.getTime();  
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--  
		Date afterTomorrow = cal.getTime();
		
		tvDateAller.setTag(tomorrow);
		tvDateRetour.setTag(afterTomorrow);
		
		tvGareAller.setTag( new GaresDepart("AIX LES BAINS", 45.6884f, 5.9096f) );
		tvGareAller.setText("AIX LES BAINS");
		tvGareArrivee.setTag( new GaresArrivee("CHAMBERY", "CHA851") );
		tvGareArrivee.setText("CHAMBERY");
		tvHeureAller.setTag(new HorrairesAller("10H02", "10H40", "CHA91001"));
		tvHeureRetour.setTag(new HorrairesRetour("10H02", "10H40", "CHA91001"));
	}

	protected void fadeInAnimation() {
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setDuration(600);

		AnimationSet animation = new AnimationSet(false); //change to false
		animation.addAnimation(fadeIn);
		LayoutAnimationController controller = new LayoutAnimationController(animation, 0.25f);
		llRetour.setLayoutAnimation(controller);
	}
	protected void toggleAllerSimple() {
		if (as.isChecked() && ar.isChecked()) {
			as.setChecked(true);
			ar.setChecked(false);
//			fadeInAnimation();
			llRetour.setVisibility(View.GONE);
		} else {
			as.setChecked(true);
			ar.setChecked(false);
		}
	}
	protected void toggleAllerRetour() {
		if (as.isChecked() && ar.isChecked()) {
			as.setChecked(false);
			ar.setChecked(true);
//			fadeInAnimation();
			llRetour.setVisibility(View.VISIBLE);
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
		
		
		// Arrghhh Android 14 suxxx, I have to convert the switch statement to if-else 
		// because of a problem cause by lookup of R.id.xxx
		//
		if (vid == R.id.allerSimple) {
			toggleAllerSimple();
		} else if (vid == R.id.allerRetour) {
			toggleAllerRetour();
		} else if (vid == R.id.llTimetableGareAller) {
			// TODO : adapter la classe TtAllerDlg
//			new TtAllerDlg(getActivity(), this).show();
			startGareAllerPopUpActivity();
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
		} else {
			Helper.grilledRare(getActivity(), "Erreur inconnue");
		}
	}

	protected void startHorrairePopUpActivity(View v, String gareAllerText, String gareArriveeText, int code) {
		if (this.tvGareAller.getTag() != null && !this.tvGareArrivee.getText().equals(getResources().getString(R.string.rechercherGare)) && this.tvGareArrivee.getTag() != null && this.tvDateAller.getTag() != null) {
			// On récupère les tags des différents champs pour construire les paramètres à envoyer par intent à l'activité
			Date da = (Date) tvDateAller.getTag();
			Date dr = null; 
			if (this.tvDateRetour.getTag() != null) dr = (Date) this.tvDateRetour.getTag();
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
			if (code == Config.HEURE_ALLER_CODE) {
				i.putExtra("title", getResources().getString(R.string.horrairesAller));
			} else {
				i.putExtra("title", getResources().getString(R.string.horraireRetour));
			}
			i.putExtra("code", code);
			//  Démarage de l'activité
			startActivityForResult(i, code);
			getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		} else {
			Helper.grilledRare(getActivity(), getResources().getString(R.string.erreurChampsNonRemplis));
		}		
	}

	protected void startDateAllerPickerActivity(View v, String gareAllerText, String gareArriveeText, int code) {
		Intent i = new Intent(this.getActivity(), DatePickerPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("title", getResources().getString(R.string.dateDepart));
		i.putExtra("gareDepart", gareAllerText);
		i.putExtra("gareArrivee", gareArriveeText);
		i.putExtra("code", code);
		i.putExtra("date", (Date) tvDateAller.getTag());
		startActivityForResult(i, code);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	protected void startGareArriveePopUpActivity(View v, String gareAllerText) {
		if (TextUtils.isEmpty(gareAllerText) || gareAllerText.equals(getActivity().getString(R.string.rechercherGare))) {
			Helper.grilledRare(getActivity(), getResources().getString(R.string.errorGareAller));
		} else {
			Intent i = new Intent(this.getActivity(), GareArriveePopUp.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
			i.putExtra("title", getResources().getString(R.string.garesArriveeTitle));
			i.putExtra("gareDepart", gareAllerText);
			startActivityForResult(i, Config.GARE_ARRIVEE_CODE);
			this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

	// Vérifie les champs Départ, Arrivée, Date aller et/ou Date retour et
	// affiche le dialog pour choisir les horraires
	protected void displayHorrairesDialog(String gareAllerText,
			String gareArriveeText, TextView resultTv) {
		// est ce que les champs obligatoires ont été remplis ?
		if (this.tvGareAller.getTag() != null
				&& !this.tvGareArrivee.getText().equals( getResources().getString(R.string.rechercherGare))
				&& this.tvGareArrivee.getTag() != null
				&& this.tvDateAller.getTag() != null) {
			Date da = (Date) this.tvDateAller.getTag();
			Date dr = null;
			if (this.tvDateRetour.getTag() != null)
				dr = (Date) this.tvDateRetour.getTag();

			GaresArrivee rt = (GaresArrivee) this.tvGareArrivee.getTag();
			HorrairesParams params = new HorrairesParams(da, dr, rt, gareAllerText, gareArriveeText);
			Log.v(this.getClass().toString(), this.tvGareAller.getTag().toString());
			HorrairesDialog dlg = new HorrairesDialog(getActivity(), resultTv,params);
			dlg.prepareDialogFragment();
		} else {
			Helper.grilledRare(getActivity(), getResources().getString(R.string.erreurChampsNonRemplis));
		}
	}

	// Doit on chercher parmi les gares à proximité ?
	// Position 0 = GPS
	// Position 1 = toutes les gares
	protected void onGAMethodSelected(int position) {
		switch (position) {
		// oui
		case 0:
			geoLocate();
			// non
		case 1:
			startGareAllerPopUpActivity();
		}
	}

	protected void geoLocate() {
		Helper.grilledRare(getActivity(), "En attente de Géolocalisation...");
	}
}