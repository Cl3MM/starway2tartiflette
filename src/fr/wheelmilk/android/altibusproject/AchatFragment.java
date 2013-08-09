package fr.wheelmilk.android.altibusproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AchatFragment extends AbstractGenericFragment { //, TtAllerDlg.TtAllerDlgListener {
//	int fragVal;
//	TextView tvGareAller; 
//	TextView tvGareArrivee; 
//	TextView tvDateAller;
//	TextView tvDateRetour;
//	TextView tvHeureAller;
//
//	TimetableMainOnClickListener onClick;

	static AchatFragment init(int val) {
		AchatFragment achatFragment = new AchatFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt("val", val);
		achatFragment.setArguments(args);
		return achatFragment;
	}
	@Override
	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.achat_billets_fragment, container, false);
	}
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		this.onClick = new TimetableMainOnClickListener();
//		
//		View layoutView = inflater.inflate(R.layout.achat_billets_fragment, container, false);
//		layoutView.findViewById(R.id.llTimetableGareAller).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableGareArrivee).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableDateAller).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableHorraireAller).setOnClickListener(this);
//		
//		this.tvGareAller = (TextView) layoutView.findViewById(R.id.tvTimetableGareAller);
//		this.tvGareArrivee = (TextView) layoutView.findViewById(R.id.tvTimetableGareArrivee);
//		this.tvDateAller = (TextView) layoutView.findViewById(R.id.tvTimetableDateAller);
////		this.tvDateRetour = (TextView) layoutView.findViewById(R.id.tvTimetableGareAller);
//		this.tvHeureAller = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireAller);
//		return layoutView;
//	}
//	@Override
//	public void onClick(View v) {
//		int vid = v.getId();
//
//		String gareAllerText = (String) this.tvGareAller.getText();
//		String gareArriveeText = (String) this.tvGareArrivee.getText();
//		String dateAllerText = (String) this.tvDateAller.getText();
//		String heureAllerText = (String) this.tvHeureAller.getText();
//
//		switch(vid) {
//		case R.id.llTimetableGareAller:
//			new TtAllerDlg(getActivity(), this).show();
//			break;
//		case R.id.llTimetableGareArrivee:
//		
//			if ( TextUtils.isEmpty(gareAllerText) || gareAllerText.equals(getActivity().getString(R.string.rechercherGare) ) ) {
//				Toast.makeText(v.getContext(), getActivity().getString(R.string.errorGareAller), Toast.LENGTH_SHORT).show();				
//			} else {
//				GareArriveeDialog dlg = new GareArriveeDialog(getActivity(), this.tvGareArrivee, gareAllerText );
//				dlg.prepareDialogFragment();
//			}
//			break;
//		case R.id.llTimetableDateAller:
//				DialogFragment newFragment = new DatePickerFragment(this.tvDateAller);
//			    newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
//			break;
//		case R.id.llTimetableHorraireAller:
//			// est ce que les champs obligatoires ont été rempli ?
//
//			if (this.tvGareAller.getTag() != null && !this.tvGareArrivee.getText().equals(getActivity().getResources().getString(R.string.rechercherGare)) && this.tvGareArrivee.getTag() != null && this.tvDateAller.getTag() != null) {
//				Date da = (Date) this.tvDateAller.getTag();
//				Date dr = null; 
////				if (this.tvDateRetour.getTag() != null) {
////					dr = (Date) this.tvDateRetour.getTag();
////				}
//				GaresArrivee rt = (GaresArrivee) this.tvGareArrivee.getTag();
//				HorrairesParams params = new HorrairesParams(da, dr, rt, gareAllerText, gareArriveeText);				
////				Toast.makeText(v.getContext(), this.tvGareAller.getTag().toString(), Toast.LENGTH_SHORT).show();
//				Log.v(this.getClass().toString(), this.tvGareAller.getTag().toString());
//				HorrairesDialog dlg = new HorrairesDialog(getActivity(), this.tvHeureAller, params );
//				dlg.prepareDialogFragment();
//			} else {
//				Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.erreurChampsNonRemplis), Toast.LENGTH_LONG);
//				TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
//				if( tv != null) tv.setGravity(Gravity.CENTER);
//				toast.show();
//			}
//			break;
//			default:
//				Toast.makeText(v.getContext(), "Erreur inconnue :(", Toast.LENGTH_SHORT).show();
//				break;
//		}
//	}
//	
//	// Doit on chercher parmi les gares à proximité ?
//	// Position 0 = GPS
//	// Position 1 = toutes les gares
//	public void onGAMethodSelected(int position) {
//		switch(position) {
//		// oui
//		case 0:
//			Toast.makeText(getActivity(), "En attente de géolocalisation", Toast.LENGTH_SHORT).show();
//			break;
//		// non
//		case 1:
//			// reset du champ texte de la gare arrivée pour éviter les erreurs de saisie
//			this.tvGareArrivee.setText(getActivity().getResources().getString(R.string.rechercherGare));
//			GareAllerDialog dlg = new GareAllerDialog(getActivity(), this.tvGareAller );
//			dlg.prepareDialogFragment();
//			break;
//		}
//	}
}