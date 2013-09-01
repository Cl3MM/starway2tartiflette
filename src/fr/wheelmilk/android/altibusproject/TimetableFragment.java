package fr.wheelmilk.android.altibusproject;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TimetableFragment extends AbstractGenericFragment { //, TtAllerDlg.TtAllerDlgListener {

	private int GARE_ALLER_CODE = 10;

	@Override
	protected void popUpFragment() {
		Intent i = new Intent(this.getActivity(), GareAllerPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("title", getActivity().getResources().getString(R.string.garesAllerTitle));
		  
		startActivityForResult(i, GARE_ALLER_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity();
		if (resultCode == Activity.RESULT_OK && requestCode == GARE_ALLER_CODE) {
	      if (data.hasExtra("result")) {
	    	  String result = data.getExtras().getString("result");
	  		if (result != null ) Log.v(this.getClass().toString(), result);
			else Log.v(this.getClass().toString(), "result is null: canceled?");
	    	  if (result.equals("WebserviceFaillure")) {
	    		  Toast.makeText(this.getActivity(), getResources().getString(R.string.errorWebservice), Toast.LENGTH_LONG).show();
	    	  } else {
	    		  this.tvGareAller.setText(result);
	    	  }
	      }
	    }
	  }
//	protected void popUpFragment() {
//	PopUpFragment newFragment = new PopUpFragment();
//	FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//	transaction.replace(R.id.rlTimetableMain, newFragment);
//	transaction.addToBackStack(null);
//	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//	transaction.commit();
//}

	static TimetableFragment init(int val) {
		TimetableFragment timeTableFragment = new TimetableFragment();
		// Supply val input as an argument.
		Bundle args = new Bundle();
		args.putInt("val", val);
		timeTableFragment.setArguments(args);
		return timeTableFragment;
	}
	
	@Override
	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.consultation_horraires_layout, container, false);
	}
//
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
//		View layoutView = inflater.inflate(R.layout.consultation_horraires_layout, container, false);
//		layoutView.findViewById(R.id.llTimetableGareAller).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableGareArrivee).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableDateAller).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableHorraireAller).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableDateRetour).setOnClickListener(this);
//		layoutView.findViewById(R.id.llTimetableHorraireRetour).setOnClickListener(this);
//		layoutView.findViewById(R.id.allerSimple).setOnClickListener(this);
//		layoutView.findViewById(R.id.allerRetour).setOnClickListener(this);
//
//		this.as = (ToggleButton) layoutView.findViewById(R.id.allerSimple);
//		this.ar = (ToggleButton) layoutView.findViewById(R.id.allerRetour);
//		this.tvGareAller = (TextView) layoutView.findViewById(R.id.tvTimetableGareAller);
//		this.tvGareArrivee = (TextView) layoutView.findViewById(R.id.tvTimetableGareArrivee);
//		this.tvDateAller = (TextView) layoutView.findViewById(R.id.tvTimetableDateAller);
//		this.tvDateRetour = (TextView) layoutView.findViewById(R.id.tvTimetableDateRetour);
//		this.tvHeureAller = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireAller);
//		this.tvHeureRetour = (TextView) layoutView.findViewById(R.id.tvTimetableHorraireRetour);
//
//		this.as.setChecked(true);
//		this.ar.setChecked(false);
//		
//		this.llRetour = (LinearLayout) layoutView.findViewById(R.id.llTimetableRetour);
////		this.llDateRetour = (LinearLayout) layoutView.findViewById(R.id.llTimetableDateRetour);
////		this.llHeureRetour = (LinearLayout) layoutView.findViewById(R.id.llTimetableHorraireRetour);
////		llDateRetour.setOnClickListener(this);
////		llHeureRetour.setOnClickListener(this);
//		llRetour.setOnClickListener(this);
//		
//		llRetour.setVisibility(View.GONE);
////		llHeureRetour.setVisibility(View.GONE);
//		return layoutView;
//	}
//	protected void fadeInAnimation() {
//		Animation fadeIn = new AlphaAnimation(0, 1);
//		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//		fadeIn.setDuration(600);
//
//		AnimationSet animation = new AnimationSet(false); //change to false
//		animation.addAnimation(fadeIn);
//		  LayoutAnimationController controller = new LayoutAnimationController(animation, 0.25f);
//		  llRetour.setLayoutAnimation(controller);
//	}
//	
//	protected void fadeOutAnimation() {
//		Animation fadeOut = new AlphaAnimation(1, 0);
//		fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
//		fadeOut.setStartOffset(1000);
//		fadeOut.setDuration(600);
//
//		AnimationSet animation = new AnimationSet(false); //change to false
//		animation.addAnimation(fadeOut);
//		LayoutAnimationController controller = new LayoutAnimationController(animation, 0.25f);
//		llRetour.setLayoutAnimation(controller);
////		llHeureRetour.setLayoutAnimation(controller);
//	}
//	@Override
//	public void onClick(View v) {
//		int vid = v.getId();
//		DialogFragment newFragment;
//
//		String gareAllerText = (String) this.tvGareAller.getText();
//		String gareArriveeText = (String) this.tvGareArrivee.getText();
//		String dateAllerText = (String) this.tvDateAller.getText();
//		String heureAllerText = (String) this.tvHeureAller.getText();
//
//		switch(vid) {
//		case R.id.allerSimple: // setChecked(false) = orange
//			if (as.isChecked() && ar.isChecked()) {
//				as.setChecked(true);
//				ar.setChecked(false);
//		 		fadeOutAnimation();
////				llDateRetour.setVisibility(View.GONE);
//				llRetour.setVisibility(View.GONE);
//			} else {
//				as.setChecked(true);
//				ar.setChecked(false);
//			}
//			break;
//		case R.id.allerRetour: // setChecked(true) = rouge
//			if (as.isChecked() && ar.isChecked()) {
//				as.setChecked(false);
//				ar.setChecked(true);
//				fadeInAnimation();
////				llDateRetour.setVisibility(View.VISIBLE);
//				llRetour.setVisibility(View.VISIBLE);
//			} else {
//				as.setChecked(false);
//				ar.setChecked(true);
//			}
//			break;
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
//			newFragment = new DatePickerFragment(this.tvDateAller);
//		    newFragment.show(getActivity().getSupportFragmentManager(), "DateAller");
//		    break;
//		case R.id.llTimetableDateRetour:
//			newFragment = new DatePickerFragment(this.tvDateRetour);
//		    newFragment.show(getActivity().getSupportFragmentManager(), "DateRetour");
//		    break;
//		case R.id.llTimetableHorraireAller:
//			displayHorrairesDialog(gareAllerText, gareArriveeText, this.tvHeureAller);
//			break;
//		case R.id.llTimetableHorraireRetour:
//			displayHorrairesDialog(gareAllerText, gareArriveeText, this.tvHeureRetour);
//			break;			
//		default:
//			Toast.makeText(v.getContext(), "Erreur inconnue :(", Toast.LENGTH_SHORT).show();
//			break;
//		}
//	}
//	
//	// Vérifie les champs Départ, Arrivée, Date aller et/ou Date retour et affiche le dialog pour choisir les horraires
//	protected void displayHorrairesDialog(String gareAllerText, String gareArriveeText, TextView resultTv) {
//		// est ce que les champs obligatoires ont été remplis ?
//		if (this.tvGareAller.getTag() != null && !this.tvGareArrivee.getText().equals(getActivity().getResources().getString(R.string.rechercherGare)) && this.tvGareArrivee.getTag() != null && this.tvDateAller.getTag() != null) {
//			Date da = (Date) this.tvDateAller.getTag();
//			Date dr = null; 
//			if (this.tvDateRetour.getTag() != null) dr = (Date) this.tvDateRetour.getTag();
//
//			GaresArrivee rt = (GaresArrivee) this.tvGareArrivee.getTag();
//			HorrairesParams params = new HorrairesParams(da, dr, rt, gareAllerText, gareArriveeText);				
//			Log.v(this.getClass().toString(), this.tvGareAller.getTag().toString());
//			HorrairesDialog dlg = new HorrairesDialog(getActivity(), resultTv, params );
//			dlg.prepareDialogFragment();
//		} else {
//			Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.erreurChampsNonRemplis), Toast.LENGTH_LONG);
//			TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
//			if( tv != null) tv.setGravity(Gravity.CENTER);
//			toast.show();
//		}		
//	}
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

	//	@Override
	public View PloponCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.achat_billets_fragment,
				container, false);
//		View layoutView = inflater.inflate(R.layout.timetable_fragment,
//				container, false);

		// View gareDepartText = layoutView.findViewById(R.id.gareDepartText);
		// ((TextView) gareDepartText).setText(R.string.gareDepart);
		//
		// View gareDepartChoice =
		// layoutView.findViewById(R.id.gareDepartChoice);
		// ((AutoResizeTextView)
		// gareDepartChoice).setText(R.string.rechercherGare);
		//
		// View gareRetourText = layoutView.findViewById(R.id.gareRetourText);
		// ((TextView) gareRetourText).setText(R.string.gareRetour);
		//
		// View gareRetourChoice =
		// layoutView.findViewById(R.id.gareRetourChoice);
		// ((AutoResizeTextView)
		// gareRetourChoice).setText(R.string.rechercherGare);
		//
		// View TextView01 = layoutView.findViewById(R.id.TextView01);
		// ((TextView) TextView01).setText(R.string.horraireAller);
		// View AutoResizeTextView01 =
		// layoutView.findViewById(R.id.AutoResizeTextView01);
		// ((AutoResizeTextView)
		// AutoResizeTextView01).setText(R.string.rechercherHorraire);
		//
		// View TextView02 = layoutView.findViewById(R.id.TextView02);
		// ((TextView) TextView02).setText(R.string.horraireRetour);
		// View SingleLineTextView02 =
		// layoutView.findViewById(R.id.SingleLineTextView02);
		// ((AutoResizeTextView)
		// SingleLineTextView02).setText(R.string.rechercherHorraire);

		// List<Object> listFragments = new ArrayList<Object>();
		// listFragments.add(new
		// TimetableListFragment(R.string.gareDepart,R.string.rechercherGare,
		// ));

//		View.OnClickListener onclicklistener = new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(
//						getActivity());
//				Log.v("Altibus onClick: ", "Creating Dialog");
//
//				// 2. Chain together various setter methods to set the dialog
//				// characteristics
//				builder.setTitle("Choisissez une gare").setItems(
//						new String[] { "This", "Is", "This", "Is", "This",
//								"This", "Is", "This", "Is", "This", "Is",
//								"This", "Is", "This", "Is", "This", "Is",
//								"Demo" },
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int position) {
//								// The 'which' argument contains the index
//								// position
//								// of the selected item
//								Log.v("Altibus onClickListener Result: ",
//										"position: " + position);
//							}
//						});
//
//				// 3. Get the AlertDialog from create()
//				AlertDialog dialog = builder.create();
//				dialog.show();
//			}
//		};

//		Map mapFragments = new TreeMap();
//		mapFragments.put(R.string.gareDepart, R.string.rechercherGare);
//		mapFragments.put(R.string.gareRetour, R.string.rechercherGare);
//		mapFragments.put(R.string.horraireAller, R.string.rechercherHorraire);
//		mapFragments.put(R.string.horraireRetour, R.string.rechercherHorraire);

		LinearLayout testTimetableLinearLayout = (LinearLayout) layoutView
				.findViewById(R.id.timetableLinearLayout);

		try {
			TimetableFragmentFieldFactory timetableFragmentField = new TimetableFragmentFieldFactory( layoutView, getActivity(), R.string.gareDepart, R.string.rechercherGare, DialogAdapterFactory.class);
			View layout = timetableFragmentField.getTimetableFragmentFieldView();

			Log.v(TimetableFragment.class.toString(), "Layout crée....");
//			timetableFragmentField = new TimetableFragmentFieldFactory( layoutView, getActivity(), R.string.gareRetour, R.string.rechercherGare, DialogAdapterFactory.class);
//			View layout1 = timetableFragmentField.getTimetableFragmentFieldView();
//
//			timetableFragmentField = new TimetableFragmentFieldFactory( layoutView, getActivity(), R.string.horraireAller, R.string.rechercherHorraire, DialogAdapterFactory.class);
//			View layout2 = timetableFragmentField.getTimetableFragmentFieldView();
//
//			timetableFragmentField = new TimetableFragmentFieldFactory( layoutView, getActivity(), R.string.horraireRetour, R.string.rechercherHorraire, DialogAdapterFactory.class);
//			View layout3 = timetableFragmentField.getTimetableFragmentFieldView();
//
			testTimetableLinearLayout.addView(layout);
//			testTimetableLinearLayout.addView(layout1);
//			testTimetableLinearLayout.addView(layout2);
//			testTimetableLinearLayout.addView(layout3);
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("AltibusProject: try catch", "C'est la merde 1");
		} catch (java.lang.InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("AltibusProject: try catch", "C'est la merde 2");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("AltibusProject: try catch", "C'est la merde 3");
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("AltibusProject: try catch", "C'est la merde 4");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("AltibusProject: try catch", "C'est la merde 5");
		}



//		Iterator iter = mapFragments.entrySet().iterator();
//
//		while (iter.hasNext()) {
//			Map.Entry mEntry = (Map.Entry) iter.next();
//
//			View testTimetableFragmentField = getLayoutInflater(
//					savedInstanceState).inflate(
//					R.layout.timetable_fragment_field, null);
//			View leftTextView = testTimetableFragmentField
//					.findViewById(R.id.timetableFragmentTextField);
//			View rightTextView = testTimetableFragmentField
//					.findViewById(R.id.timetableFragmentSearchField);
//			String leftText = getResources().getString(
//					(Integer) mEntry.getKey());
//			String rightText = getResources().getString(
//					(Integer) mEntry.getValue());
//
//			// setting textview
//			((TextView) leftTextView).setText(leftText);
//			((AutoResizeTextView) rightTextView).setText(rightText);
//			rightTextView.setClickable(true);
//			rightTextView.setOnClickListener(onclicklistener);
//
//			testTimetableLinearLayout.addView(testTimetableFragmentField);
//		}

		return layoutView;

	}

}