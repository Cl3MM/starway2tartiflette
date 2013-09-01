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

import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

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

	public void setTagResult(Intent data, TextView tv) {
		String result = data.getExtras().getString("result");
		if ( result != null ) {
			GaresDataModel tag = data.getParcelableExtra("tag");
			tv.setText(result);
			tv.setTag(tag);
			Log.v(this.getClass().toString(), "Je suis REMET A ZERO !!!");
			// Remise à zéro des champs
			if (tv.equals(tvGareAller)) {
				tvGareArrivee.setText(getResources().getString(R.string.rechercherGare));
				tvGareArrivee.setTag(null);
			}
			resetTextViews();
		}
	}
	protected void resetTextViews() {
		tvHeureAller.setText(getResources().getString(R.string.rechercherhoraire));
		tvHeureRetour.setText(getResources().getString(R.string.rechercherhoraire));
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
					tvHeureAller.setText(getResources().getString(R.string.rechercherhoraire));
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
					tvHeureRetour.setText(getResources().getString(R.string.rechercherhoraire));
					tvHeureRetour.setTag(null);					
				}
				break;
			case Config.HEURE_RETOUR_CODE:
				setHorrairesTvAfterPopUp(data, tvHeureRetour);
				break;
			}
		}
		int i = 2;		
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
		setUpPopUpColor();
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
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
		setUpChildrenPages();
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
		tvGareAller.setTag( new GaresDepart("AIX LES BAINS", 45.6884f, 5.9096f) );
		tvGareAller.setText("AIX LES BAINS");
		tvGareArrivee.setTag( new GaresArrivee("CHAMBERY", "CHA851") );
		tvGareArrivee.setText("CHAMBERY");
		tvHeureAller.setTag(new HorrairesAller("10H02", "10H40", "CHA91001"));
		tvHeureRetour.setTag(new HorrairesRetour("10H02", "10H40", "CHA91001"));
		tvDateRetour.setTag(afterTomorrow);
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
			llRetour.setVisibility(View.GONE);
			tvHeureRetour.setTag(null);
			tvHeureRetour.setText(getString(R.string.rechercherhoraire));
			tvDateRetour.setTag(null);
			tvDateRetour.setText(getString(R.string.rechercherhoraire));
			updateMontant();
//			llRetour.startAnimation(new LinearLayoutVerticalScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f, 500, llRetour, true));
		} else {
			as.setChecked(true);
			ar.setChecked(false);
		}
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
		} 
			else {
			Helper.grilledRare(getActivity(), "Erreur inconnue");
		}
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
			tvDateRetour.setText(getResources().getString(R.string.rechercheDate));
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
	protected void startGareAllerPopUpActivity() {
		Intent i = new Intent(getActivity(), GareAllerPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("title", getString(R.string.garesAllerTitle));
		i.putExtra("popupColor", popupColor);
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