package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import fr.wheelmilk.android.altibusproject.models.GaresDataModel;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesParams;
import fr.wheelmilk.android.altibusproject.support.Config;

public class HorrairesPopUpActivity extends ActivityPopUpFactory {
	protected HorrairesParams params;
	protected String gareDepart;
	protected String gareArrivee;
	protected String dateAller;
	private   String prettyResult;
	protected HorrairesArrayAdapter horrairesArrayAdapter;
	private int code;

	public static class HorrairesMapping {
		
		public String depart;
		public String arrivee;
		public Object object;
		
		public HorrairesMapping(String _depart, String _arrivee) {
			depart 	= _depart;
			arrivee 	= _arrivee;
		}
		public String depart() {
			return depart.replace("H", ":");
		}
		public String arrivee() {
			return arrivee.replace("H", ":");
		}
		public Object object() {
			return object;
		}
	}
	@Override
	protected void populateListView() {
		ArrayList<HorrairesMapping> data = sortAndCreateArrayAdapter();
		this.horrairesArrayAdapter = buildHorrairesArrayAdpater(data);
		ListView listeDesGares = (ListView) findViewById(R.id.listDesGares);
		listeDesGares.setAdapter(horrairesArrayAdapter);
	}
	@Override
	protected void setSuccessfulResult(int position) {
		// Convertissons la position en objet horraire puis en String (ie: 11H40)
		HorrairesPopUpActivity.HorrairesMapping o = horrairesArrayAdapter.getItem(position);
		result = o.depart();
		setUpPrettyResult();
		returnCode 	= RESULT_OK; 
	}
	protected void setUpPrettyResult() {
		GaresDataModel resultFromWebService = altibusData.get(result);
		prettyResult = resultFromWebService.heureAller() + " - " + resultFromWebService.heureArrivee();
	}
	@Override
	protected void onPreFinish() {
		Intent data = new Intent();
		Bundle bb = new Bundle();
		bb.putParcelable("tag", altibusData.get(result));
		data.putExtra("result", prettyResult);
		data.putExtras(bb);
		this.setResult(returnCode, data);
	}

	private HorrairesArrayAdapter buildHorrairesArrayAdpater( ArrayList<HorrairesMapping> _horrairesArrayAdapter) {
		HorrairesArrayAdapter arrayAdapter = new HorrairesArrayAdapter(this, R.layout.popup_list_item, _horrairesArrayAdapter, popupColor);
		return arrayAdapter;
	}
	@Override
	protected void setPopUpLayout() {
		setContentView(R.layout.horraires_popup_activity);
	}
	@Override
	protected void addTextChangedListener() {
		TextView tvGareDepart = (TextView) findViewById(R.id.horrairesDlgTvGareDepart);
		TextView tvGareArrivee = (TextView) findViewById(R.id.horrairesDlgTvGareArrivee);
		TextView tvDateAller = (TextView) findViewById(R.id.horrairesDlgTvDateAller);
		TextView tvDateRetour = (TextView) findViewById(R.id.horrairesDlgTvDateRetour);

		if (popupColor == Color.parseColor(Config.POPUP_GREEN_COLOR)) {
			tvGareDepart.setTextColor(popupColor);
			tvGareArrivee.setTextColor(popupColor);
			tvDateAller.setTextColor(popupColor);
		}

		tvGareDepart.setText(params.gareAller());
		tvGareArrivee.setText(params.gareArrivee());
		tvDateAller.setText(params.prettyDateAller());
		if ( code == Config.HEURE_ALLER_CODE ) {
			LinearLayout llDateRetour = (LinearLayout) findViewById(R.id.llDateRetour);
			llDateRetour.setVisibility(View.GONE);
		} else {
			tvDateRetour.setText(params.prettyDateRetour());
			tvDateRetour.setTextColor(popupColor);
		}
	}
	@Override
	public void initialize( Bundle extras) {
		super.initialize(extras);
		params 	= extras.getParcelable("params");
		title   = extras.getString("title");
		code 	= extras.getInt("code");
	}
	@Override
	protected void startAsyncHTTPRequest() {
		RequestParams _params = new RequestParams();
		 _params.put("tip", "horaires");
		 _params.put("da", this.params.dateAller() );
		 if (params.dateRetour() != null) {
			 _params.put("dr", params.dateRetour() );
		 }
		 _params.put("rt", params.route() );
		 Log.v(this.getClass().toString(), _params.toString());
		new AltibusWebservice<HorrairesPopUpActivity>(this, "sw/altibus/data.aspx?", _params);
	}
	protected ArrayList<HorrairesMapping> sortAndCreateArrayAdapter() {
		//tri des horraires
		TreeMap<String, String> horrairesAller 		= new TreeMap<String, String>();
		TreeMap<String, String> horrairesRetour 	= new TreeMap<String, String>();

		// On remplit le TreeMap avec les horraires triés dans l'ordre (horrairesAller = { 12h30 => 13h00, 16h30 => 17h00 ... }
		for (GaresDataModel value: this.altibusData.values()) {
			if ( value.getClass().toString().equals(HorrairesAller.class.toString()) ) {
				horrairesAller.put(value.heureAller(), value.heureArrivee()); // + "::" + value.gareName()
			} else {
				horrairesRetour.put(value.heureAller(), value.heureArrivee()); // + "::" + value.gareName()
			}
		}

		// Si horrairesRetour n'est pas vide, alors on doit afficher les Retours
		if (code == Config.HEURE_RETOUR_CODE) {
			horrairesAller = horrairesRetour;
			TextView tvTitle = (TextView) findViewById(R.id.title);
			tvTitle.setText(title);
		}
		
		ArrayList<HorrairesPopUpActivity.HorrairesMapping> arrayAdapter = new ArrayList<HorrairesPopUpActivity.HorrairesMapping>();
		for (Entry<String, String> value : horrairesAller.entrySet()) {
			arrayAdapter.add( new HorrairesPopUpActivity.HorrairesMapping( value.getKey(), value.getValue()) );
		}
		return arrayAdapter;
	}
	@Override
	protected void addLayoutCustomizations() {
		if (popupColor == Color.parseColor(Config.POPUP_GREEN_COLOR)) {
			LinearLayout ll1 = (LinearLayout) findViewById(R.id.llBgToChange1);
			ll1.setBackgroundColor(popupColor);
			LinearLayout ll2 = (LinearLayout) findViewById(R.id.llBgToChange2);
			ll2.setBackgroundColor(popupColor);
		}
	}
}