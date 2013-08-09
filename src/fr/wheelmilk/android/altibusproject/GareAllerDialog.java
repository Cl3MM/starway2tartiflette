package fr.wheelmilk.android.altibusproject;

import android.app.Activity;
import android.widget.TextView;

public class GareAllerDialog extends GaresDialog {

	public GareAllerDialog(Activity activity, TextView tv) {
		super(activity, tv, activity.getResources().getString(R.string.garesAllerTitle));
	}
	@Override
	public void prepareDialogFragment() {
		this.mDialog.show();
		new AltibusWebservice<GareAllerDialog>(this, "data.aspx?tip=gps7", null );
	}
}

//import android.app.Activity;
//import android.app.AlertDialog;
//import java.util.ArrayList;
//
//import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;
//
//import android.app.ProgressDialog;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//public class GareAllerDialog implements OnWebserviceListenner {
//	Activity activity;
//	TextView tv;
//
//	ArrayList<String> itemListNames;
//	ProgressDialog mDialog;
//	ArrayAdapter<String> arrayAdapter;
//	LayoutInflater inflater;
//	AlertDialog dialog;
//	private String onClickValue;
//
//	public GareAllerDialog(Activity activity, TextView tv) {
//		this.activity = activity;
//		this.tv = tv;
//		
//		this.mDialog = new ProgressDialog(activity);
//        this.mDialog.setMessage("Loading...");
//        this.mDialog.setCancelable(false);
//	}
//
//	public void updateTextViewString(String result) {
//		this.onClickValue = result;
//		Log.v("Altibus updateTextViewString: ", (String) this.tv.getText());
//		Log.v("Altibus updateTextViewString: ", this.onClickValue);
//		tv.setText(result);
//	}
//
//	public void prepareDialogFragment() {
//		this.mDialog.show();
//		new AltibusWebservice<GareAllerDialog>(this, "data.aspx?tip=gps7", null );
//	}
//
//	private AlertDialog createDialog() {
//		this.arrayAdapter = this.buildArrayAdpater(this.itemListNames);
//		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//		LayoutInflater inflater = activity.getLayoutInflater();
//
//		Log.v(DialogAdapterFactory.class.toString(), activity.getResources().getString(R.string.loading));
//		View layout = inflater.inflate(R.layout.gares_aller_dialog, null);
//		
//        ListView listeDesGares = (ListView) layout.findViewById(R.id.gareAllerList);
//        EditText filterGareAller = (EditText) layout.findViewById(R.id.filterGaresAller);
//
////        listeDesGares.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
////        listeDesGares.setStackFromBottom(true);
//
//        // Listener pour l'item sélectionné
//        listeDesGares.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				// On update le textview avec la valeur choisie et on ferme le dialog
//				updateTextViewString( arrayAdapter.getItem(position));
//				dialog.dismiss();
//			}
//		});
//        
//        // listener qui met à jour la liste quand l'utilisateur tape du texte
//        filterGareAller.addTextChangedListener(new TextWatcher() {
//            
//           @Override
//           public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//               arrayAdapter.getFilter().filter(cs);   
//           }
//            
//           @Override
//           public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                   int arg3) {
//           }
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//			}
//       });
//        
//        listeDesGares.setAdapter(arrayAdapter);
//        builder.setView(layout);
//		builder.setTitle(activity.getResources().getString(R.string.gareAcoteDlgTitle));
//		builder.setCancelable(true);
//
//		this.dialog = builder.create();
//		return this.dialog;
//		
//	}
//	private ArrayAdapter<String> buildArrayAdpater(ArrayList<String> arrayString) {
//		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, arrayString );
//		return arrayAdapter;
//	}
//	
//	// Waiting for Request to the webservice
//	@Override
//	public void onWebserviceSuccess(String xmlString) {
//		Log.v(DialogAdapterFactory.class.toString(), "Webservice success !!");
////		Toast.makeText(activity, "Webservice success !!", Toast.LENGTH_SHORT).show();
//		Log.v(DialogAdapterFactory.class.toString(), xmlString);
//		
//		AltibusDataModel altibusDataModel = new AltibusSerializer().serializeXml(xmlString);
//		
//		if (altibusDataModel != null ) {
//			this.mDialog.dismiss();
////			Toast.makeText(activity, "Serializer Success !", Toast.LENGTH_SHORT).show();
//			this.itemListNames = altibusDataModel.itemNames();
//			createDialog().show();
//		} else {
//			this.mDialog.dismiss();
//			Toast.makeText(activity, "Serializer faillure :(", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	@Override
//	public void onWebserviceFailure() {
//		this.mDialog.dismiss();
//		Log.v(DialogAdapterFactory.class.toString(), "Webservice faillure :(");
//		Toast.makeText(activity, "Webservice faillure :(", Toast.LENGTH_LONG).show();
//	}
//}