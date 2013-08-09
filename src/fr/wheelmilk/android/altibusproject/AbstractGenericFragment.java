package fr.wheelmilk.android.altibusproject;

import java.util.Date;
import com.actionbarsherlock.app.SherlockFragment;
import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.HorrairesParams;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public abstract class AbstractGenericFragment extends SherlockFragment implements View.OnClickListener { //, TtAllerDlg.TtAllerDlgListener {
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

	static AbstractGenericFragment init(int val) {
		new UnsupportedOperationException("Not implemented yet");
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
	}

	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.consultation_horraires_layout, container, false);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.onClick = new TimetableMainOnClickListener();

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

		return layoutView;
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
	
	protected void fadeOutAnimation() {
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
		fadeOut.setStartOffset(1000);
		fadeOut.setDuration(600);

		AnimationSet animation = new AnimationSet(false); //change to false
		animation.addAnimation(fadeOut);
		LayoutAnimationController controller = new LayoutAnimationController(animation, 0.25f);
		llRetour.setLayoutAnimation(controller);
	}
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		DialogFragment newFragment;

		String gareAllerText = (String) this.tvGareAller.getText();
		String gareArriveeText = (String) this.tvGareArrivee.getText();
		String dateAllerText = (String) this.tvDateAller.getText();
		String heureAllerText = (String) this.tvHeureAller.getText();

		
		if (vid == R.id.allerSimple) {
			if (as.isChecked() && ar.isChecked()) {
				as.setChecked(true);
				ar.setChecked(false);
		 		fadeOutAnimation();
				llRetour.setVisibility(View.GONE);
			} else {
				as.setChecked(true);
				ar.setChecked(false);
			}
		} else if (vid == R.id.allerRetour) {
			if (as.isChecked() && ar.isChecked()) {
				as.setChecked(false);
				ar.setChecked(true);
				fadeInAnimation();
				llRetour.setVisibility(View.VISIBLE);
			} else {
				as.setChecked(false);
				ar.setChecked(true);
			}
		} else if (vid == R.id.llTimetableGareAller) {
			new TtAllerDlg(getActivity(), this).show();
		} else if (vid == R.id.llTimetableGareArrivee) {
			if ( TextUtils.isEmpty(gareAllerText) || gareAllerText.equals(getActivity().getString(R.string.rechercherGare) ) ) {
				Toast.makeText(v.getContext(), getActivity().getString(R.string.errorGareAller), Toast.LENGTH_SHORT).show();				
			} else {
				GareArriveeDialog dlg = new GareArriveeDialog(getActivity(), this.tvGareArrivee, gareAllerText );
				dlg.prepareDialogFragment();
			}
		} else if (vid == R.id.llTimetableDateAller) {
			newFragment = new DatePickerFragment(this.tvDateAller);
			newFragment.show(getActivity().getSupportFragmentManager(), "DateAller");
		} else if (vid == R.id.llTimetableDateRetour) {
			newFragment = new DatePickerFragment(this.tvDateRetour);
			newFragment.show(getActivity().getSupportFragmentManager(), "DateRetour");
		} else if (vid == R.id.llTimetableHorraireAller) {
			displayHorrairesDialog(gareAllerText, gareArriveeText, this.tvHeureAller);
		} else if (vid == R.id.llTimetableHorraireRetour) {
			displayHorrairesDialog(gareAllerText, gareArriveeText, this.tvHeureRetour);
		} else {
			Toast.makeText(v.getContext(), "Erreur inconnue :(", Toast.LENGTH_SHORT).show();
		}
	}
	
	// Vérifie les champs Départ, Arrivée, Date aller et/ou Date retour et affiche le dialog pour choisir les horraires
	protected void displayHorrairesDialog(String gareAllerText, String gareArriveeText, TextView resultTv) {
		// est ce que les champs obligatoires ont été remplis ?
		if (this.tvGareAller.getTag() != null && !this.tvGareArrivee.getText().equals(getActivity().getResources().getString(R.string.rechercherGare)) && this.tvGareArrivee.getTag() != null && this.tvDateAller.getTag() != null) {
			Date da = (Date) this.tvDateAller.getTag();
			Date dr = null; 
			if (this.tvDateRetour.getTag() != null) dr = (Date) this.tvDateRetour.getTag();

			GaresArrivee rt = (GaresArrivee) this.tvGareArrivee.getTag();
			HorrairesParams params = new HorrairesParams(da, dr, rt, gareAllerText, gareArriveeText);				
			Log.v(this.getClass().toString(), this.tvGareAller.getTag().toString());
			HorrairesDialog dlg = new HorrairesDialog(getActivity(), resultTv, params );
			dlg.prepareDialogFragment();
		} else {
			Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.erreurChampsNonRemplis), Toast.LENGTH_LONG);
			TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
			if( tv != null) tv.setGravity(Gravity.CENTER);
			toast.show();
		}		
	}
	// Doit on chercher parmi les gares à proximité ?
	// Position 0 = GPS
	// Position 1 = toutes les gares
	public void onGAMethodSelected(int position) {
		switch(position) {
		// oui
		case 0:
			geoLocate();
		// non
		case 1:
			popUpFragment();
		}
	}
	protected void geoLocate() {
		Toast.makeText(getActivity(), "En attente de géolocalisation", Toast.LENGTH_SHORT).show();
	}
	protected void popUpFragment() {
		// reset du champ texte de la gare arrivée pour éviter les erreurs de saisie
		this.tvGareArrivee.setText(getActivity().getResources().getString(R.string.rechercherGare));
		GareAllerDialog dlg = new GareAllerDialog(getActivity(), this.tvGareAller );
		dlg.prepareDialogFragment();		
	}

}