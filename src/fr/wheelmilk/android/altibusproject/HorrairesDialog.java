package fr.wheelmilk.android.altibusproject;

import java.util.Map.Entry;
import java.util.TreeMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import fr.wheelmilk.android.altibusproject.models.GaresDataModel;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesParams;

public class HorrairesDialog extends GaresDialog implements View.OnClickListener {

	HorrairesParams params;
	
	public HorrairesDialog(Activity activity, TextView tv, HorrairesParams params) {
		super(activity, tv, activity.getResources().getString(R.string.tabhorraires));
		this.params = params;
	}
	// http://www.altibus.com/sw/altibus/data.aspx?tip=garesarrivee7&gd=
//	http://www.altibus.com/sw/altibus/data.aspx?tip=horaires&da=03/08/2013&dr=03/08/2013&rt=CHA861
	@Override
	public void prepareDialogFragment() {
		this.mDialog.show();
		RequestParams params = new RequestParams();
		 params.put("tip", "horaires");
		 params.put("da", this.params.dateAller() );
		 if (this.params.dateRetour() != null) {
			 params.put("dr", this.params.dateRetour() );
		 }
		 params.put("rt", this.params.route() );
		 Log.v(this.getClass().toString(), params.toString());
		new AltibusWebservice<HorrairesDialog>(this, "data.aspx?", params);
	}
	@Override
	public void updateTextViewString(String result) {
		this.onClickValue = result;
		Log.v(this.getClass().toString(), this.onClickValue);
		
		String ha = this.altibusData.get(result).heureAller();
		String haa = this.altibusData.get(result).heureArrivee();
		Log.v(this.getClass().toString(), this.altibusData.get(result).toString());

		tv.setText(ha + " - " + haa);
		tv.setTag(altibusData.get(result));
	}
	@Override
	protected AlertDialog createDialog() {
		this.arrayAdapter 			= this.buildArrayAdpater(this.itemListNames);
//		AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(activity, R.style.myBackgroundStyle) );
		AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(activity,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar) );
		LayoutInflater inflater 	= activity.getLayoutInflater();

		View layout 				= inflater.inflate(R.layout.horraires_dialog_layout, null);
		View customTitle 			= inflater.inflate(R.layout.custom_dialog_title, null);
		
		TextView gAller 			= (TextView) layout.findViewById(R.id.horrairesDlgTvGareDepart);
		TextView gArrivee 			= (TextView) layout.findViewById(R.id.horrairesDlgTvGareArrivee);
		TextView dateAller 			= (TextView) layout.findViewById(R.id.horrairesDlgTvDateAller);
		LinearLayout sv 			= (LinearLayout) layout.findViewById(R.id.horrairesScrollView);
		TextView dlgTitle 			= (TextView) customTitle.findViewById(R.id.title);

		gAller.setText(this.params.gareAller());
		gArrivee.setText(this.params.gareArrivee());
        dlgTitle.setText(this.title);
        dateAller.setText(this.params.prettyDateAller());

		//tri des horraires
		TreeMap<String, String>horrairesAller 	= new TreeMap<String, String>();
		TreeMap<String, String>horrairesRetour 	= new TreeMap<String, String>();
		
		// TODO : Créer un type générique à passer en argument pour factoriser le code 
		// On remplit le TreeMap avec les horraires triés dans l'ordre (horrairesAller = { 12h30 => 13h00, 16h30 => 17h00 ... }
		for (GaresDataModel value: this.altibusData.values()) {
			Log.v(this.getClass().toString(), value.getClass().toString());
			if ( value.getClass().toString().equals(HorrairesAller.class.toString()) ) {
//				Log.v(this.getClass().toString(), "Processing Horraires Aller");
				horrairesAller.put(value.heureAller(), value.heureArrivee()); // + "::" + value.gareName()
			} else {
//				Log.v(this.getClass().toString(), "Processing Horraires Retour");
				horrairesRetour.put(value.heureAller(), value.heureArrivee()); // + "::" + value.gareName()
			}
		}

		// Si horrairesRetour n'est pas vide, alors on doit afficher les Retours
		if (!horrairesRetour.isEmpty()) {
			horrairesAller = horrairesRetour;
			dateAller.setText(this.params.prettyDateRetour());
		}
		// Pour chaque horraire : 
		//   - on inflate le layout horraires_single_line, 
		//   - on remplit avec les bonnes données (ha, hr)
		//   - on tague le textview horraireAllerTv avec un objet HorraireAller
		//   - on ajoute un onClick listenner pour traiter la réponse
		//   - on ajoute le layout au linear layout qui contient les horraires (sv)
		for (Entry<String, String> value : horrairesAller.entrySet()) {
//			Log.v(this.getClass().toString(), value.getKey() + " - " + value.getValue());

		    View listItemLayout = inflater.inflate(R.layout.horraires_single_line, null);
			TextView ha 	= (TextView) listItemLayout.findViewById(R.id.horraireAllerTv);
			TextView haa 	= (TextView) listItemLayout.findViewById(R.id.horraireArriveeTv);
			LinearLayout ll = (LinearLayout) listItemLayout.findViewById(R.id.llHorraireSingleLineRoot);

			ll.setOnClickListener(this);
			ha.setText(value.getKey());
			haa.setText(value.getValue());
//			ha.setTag(this.altibusData.get(value.getKey()));
			ha.setTag(this);
//			Log.v(this.getClass().toString(), this.altibusData.get(value.getKey()).toString());

		    sv.addView(listItemLayout);
		}

        builder.setCustomTitle(customTitle);
        builder.setView(layout);
		builder.setCancelable(true);

		this.dialog = builder.create();
		return this.dialog;
		
	}
	@Override
	public void onClick(View v) {

		TextView ha 	= (TextView) v.findViewById(R.id.horraireAllerTv);
		Log.v(this.getClass().toString(), (String) ha.getText());
//		HorrairesDialog horraireDlg = (HorrairesDialog) ha.getTag();
		this.dialog.dismiss();
		this.updateTextViewString((String) ha.getText());
		
	}
}
