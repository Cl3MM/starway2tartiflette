package fr.wheelmilk.android.altibusproject;

import android.app.Activity;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.HashMap;

import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;
import fr.wheelmilk.android.altibusproject.models.GaresDataModel;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


abstract class GaresDialog implements OnWebserviceListenner {
	Activity activity;
	TextView tv;

	ArrayList<String> itemListNames;
	ProgressDialog mDialog;
	ArrayAdapter<String> arrayAdapter;
	LayoutInflater inflater;
	AlertDialog dialog;
	String title;
	protected String onClickValue;
	protected HashMap<String, GaresDataModel> altibusData;

	public GaresDialog(Activity activity, TextView tv, String title) {
		this.activity = activity;
		this.tv = tv;
		this.title = title;
		this.mDialog = new ProgressDialog(activity);
        this.mDialog.setMessage("Loading...");
        this.mDialog.setCancelable(false);
	}

	public void updateTextViewString(String result) {
		this.onClickValue = result;
		Log.v("Altibus updateTextViewString: ", (String) this.tv.getText());
		Log.v("Altibus updateTextViewString: ", this.onClickValue);
		tv.setText(result);
		tv.setTag(altibusData.get(result));
	}

	public void prepareDialogFragment() {
		new UnsupportedOperationException("Not implemented yet");
//		this.mDialog.show();
//		new AltibusWebservice<GaresDialog>(this, "data.aspx?tip=gps7", null );
	}

	protected AlertDialog createDialog() {
		this.arrayAdapter = this.buildArrayAdpater(this.itemListNames);
//		AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light_Dialog_NoActionBar) );

		AlertDialog.Builder builder = new AlertDialog.Builder( activity);

		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.gares_aller_dialog, null);
		View customTitle = inflater.inflate(R.layout.custom_dialog_title, null);

		TextView dlgTitle = (TextView) customTitle.findViewById(R.id.title);
        ListView listeDesGares = (ListView) layout.findViewById(R.id.gareAllerList);
        EditText filterGareAller = (EditText) layout.findViewById(R.id.filterGaresAller);
        dlgTitle.setText(this.title);

        // Listener pour l'item sélectionné
        listeDesGares.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// On update le textview avec la valeur choisie et on ferme le dialog
				updateTextViewString( arrayAdapter.getItem(position));
				dialog.dismiss();
			}
		});

        // listener qui met à jour la liste quand l'utilisateur tape du texte
        filterGareAller.addTextChangedListener(new TextWatcher() {

           @Override
           public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
               arrayAdapter.getFilter().filter(cs);   
           }

           @Override
           public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                   int arg3) {
           }

			@Override
			public void afterTextChanged(Editable arg0) {
			}
       });
        
        listeDesGares.setAdapter(arrayAdapter);
        builder.setCustomTitle(customTitle);
        builder.setView(layout);
		builder.setCancelable(true);

		this.dialog = builder.create();

		return this.dialog;
	}

	protected ArrayAdapter<String> buildArrayAdpater(ArrayList<String> arrayString) {
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.activity, R.layout.simple_list_view_item_layout, arrayString );
		return arrayAdapter;
	}

	// Waiting for Request to the webservice
	@Override
	public void onWebserviceSuccess(String xmlString) {
//		Log.v(DialogAdapterFactory.class.toString(), "Webservice success !!");
//		Toast.makeText(activity, "Webservice success !!", Toast.LENGTH_SHORT).show();
		Log.v(this.getClass().toString(), xmlString);
		Log.v(this.getClass().toString(), (this.getClass().toString().equalsIgnoreCase(HorrairesDialog.class.toString())) ? "Am I an instance of HorrairesDialog? TRUE" : "Am I an instance of HorrairesDialog? FALSE");
		AltibusDataModel altibusDataModel = new AltibusSerializer().serializeXml(xmlString);
		Log.v(this.getClass().toString(), "Je suis une instance de " + this.getClass());

		if (altibusDataModel != null ) {
			// Le xml est sérialisé
//			Toast.makeText(activity, "Serializer Success !", Toast.LENGTH_SHORT).show();
			this.itemListNames = altibusDataModel.itemNames();
			this.altibusData = altibusDataModel.objects();
			Log.v(this.getClass().toString(), "Preparing dialog");
			AlertDialog d = createDialog();
			Log.v(this.getClass().toString(), "Showing dialog");
			this.mDialog.dismiss();
			d.show();
			Log.v(this.getClass().toString(), "Dialog shown");
			
		} else if (this.getClass().toString().equalsIgnoreCase(HorrairesDialog.class.toString())) {
			this.mDialog.dismiss();
			// HorraireDialog = Pas d'horrraire ce jour
			Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.erreurPasDHorraire), Toast.LENGTH_LONG);
			TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
			if( v != null) v.setGravity(Gravity.CENTER);
			toast.show();
//			Toast.makeText(activity, activity.getResources().getString(R.string.erreurPasDHorraire), Toast.LENGTH_SHORT).show();
		} else {
			this.mDialog.dismiss();

			// Problème de sérialisation
			Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.erreurSerialisation), Toast.LENGTH_SHORT);
			TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
			if( v != null) v.setGravity(Gravity.CENTER);
			toast.show();
			Log.v(this.getClass().toString(), "Serializer faillure :(");
		}
	}

	@Override
	public void onWebserviceFailure() {
		this.mDialog.dismiss();
		Log.v(this.getClass().toString(), "Webservice faillure :(");
		Toast.makeText(activity, activity.getResources().getString(R.string.errorWebservice), Toast.LENGTH_LONG).show();
	}
}