package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;

import fr.wheelmilk.android.altibusproject.layout.AutoResizeTextView;
import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class DialogAdapterFactory implements OnWebserviceListenner {
	Activity activity;
	TimetableOnclickListener onClickListener = null;
	View timetableFragmentTextField;
	AltibusDataModel altibusDataModel;
	ArrayList<String> itemListNames;
	ProgressDialog mDialog;
	private String onClickValue;

	public DialogAdapterFactory(Activity activity) {
		this.activity = activity;
		this.mDialog = new ProgressDialog(activity);
        this.mDialog.setMessage("Loading...");
        this.mDialog.setCancelable(false);
	}

	public void updateTextViewString(String result) {
		this.onClickValue = result;
		Log.v("Altibus updateTextViewString: ",
				(String) ((AutoResizeTextView) this.timetableFragmentTextField)
						.getText());
		Log.v("Altibus updateTextViewString: ", this.onClickValue);
		((AutoResizeTextView) this.timetableFragmentTextField).setText(result);

	}

	// public AltibusWebservice
	private TimetableOnclickListener onClickListener() {
		TimetableOnclickListener onclicklistener = new TimetableOnclickListener(activity, this);
		return onclicklistener;
	}
	
	public void prepareDialogFragment() {
		this.mDialog.show();
		new AltibusWebservice<DialogAdapterFactory>(this, "data.aspx?tip=gps7", null );		
	}

	public void attachTextView(View rightTextView) {
		this.timetableFragmentTextField = rightTextView;
		((AutoResizeTextView) rightTextView).setTag(this.hashCode());
		rightTextView.setClickable(true);
		rightTextView.setOnClickListener(this.onClickListener());
	}

	private AlertDialog createDialog() {
		final ArrayAdapter<String> arrayAdapter = this.buildArrayAdpater(this.itemListNames);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		Log.v(DialogAdapterFactory.class.toString(), "Creating Dialog...");
		
		builder.setTitle("Choisissez une gare").setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int position) {
						// The 'which' argument contains the index position of the selected item
						updateTextViewString( itemListNames.get(position));
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		return dialog;
		
	}
	private ArrayAdapter<String> buildArrayAdpater(ArrayList<String> arrayString) {
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, arrayString );
		return arrayAdapter;
	}
	
	// Waiting for Request to the webservice
	@Override
	public void onWebserviceSuccess(String xmlString) {
		Log.v(DialogAdapterFactory.class.toString(), "Webservice success !!");
		Toast.makeText(activity, "Webservice success !!", Toast.LENGTH_SHORT).show();
		Log.v(DialogAdapterFactory.class.toString(), xmlString);
		
		AltibusDataModel altibusDataModel = new AltibusSerializer(AltibusDataModel.class).serializeXml(xmlString);
		
		if (altibusDataModel != null ) {
			this.mDialog.dismiss();
			Toast.makeText(activity, "Serializer Success !", Toast.LENGTH_SHORT).show();
			this.itemListNames = altibusDataModel.itemNames();
			createDialog().show();
		} else {
			this.mDialog.dismiss();
			Toast.makeText(activity, "Serializer faillure :(", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onWebserviceFailure() {
		this.mDialog.dismiss();
		Log.v(DialogAdapterFactory.class.toString(), "Webservice faillure :(");
		Toast.makeText(activity, "Webservice faillure :(", Toast.LENGTH_LONG).show();
	}
}
