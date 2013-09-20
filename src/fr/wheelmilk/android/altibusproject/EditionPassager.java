package fr.wheelmilk.android.altibusproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.Passager;
import fr.wheelmilk.android.altibusproject.support.Config;

public class EditionPassager extends SherlockActivity implements OnClickListener {
	
	int returnCode;
	Passager passager;
	EditText etNom;
	EditText etPrenom;
	TextView etAge;
	

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edition_passagers);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 11) {
			// Change la couleur du texte de l'action bar pour annuler le theme light
			int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView Tv = (TextView)findViewById(titleId);

			Tv.setTextColor(getResources().getColor(R.color.table_background));			
		}

		// get Extras from bundle and setup class attributes
		initialize(getIntent().getExtras());

		RelativeLayout rlValid = (RelativeLayout) findViewById(R.id.rlValidPassagers);
		rlValid.setOnClickListener(this);
		
		etNom = (EditText) findViewById(R.id.etNom); 
		etPrenom = (EditText) findViewById(R.id.etPrenom); 
		etAge = (TextView) findViewById(R.id.etAge); 
		
		etAge.setOnClickListener(this);
		
		etNom.setHint(getResources().getString(R.string.saisirNom));
		etPrenom.setHint(getResources().getString(R.string.saisirPrenom));
		etAge.setHint(getResources().getString(R.string.clickAge));

		if( passager.isValid(getResources())) {
			etNom.setText(passager.getNom());
			etPrenom.setText(passager.getPrenom());
			etAge.setText(passager.getAgeAsString());			
		}
	}
	
	public void initialize( Bundle extras ) {
		if (extras == null) {
			returnCode = Config.EXTRA_FAILURE;
			finish();
		}
		passager = extras.getParcelable("passager");
	}
	protected void onPreFinish() {
		Intent data = new Intent();
		Bundle b = new Bundle();
		
		b.putParcelable("passager", passager);
		data.putExtras(b);
		setResult(returnCode, data);
	}
	@Override
	public void finish() {
		if (returnCode == RESULT_OK) onPreFinish();
		else setResult(returnCode);
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
		returnCode = RESULT_CANCELED;	
		finish();
		return false;
    }
	private CharSequence[] generateAgeList() {
		CharSequence[] cs = new CharSequence[121];
		for(int i = 0; i < 121; i++) {
			cs[i] = String.valueOf(i);
		}
		return cs;
	}
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		
		if (vid == R.id.etAge) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
			final CharSequence[] countries = generateAgeList();
			builder.setTitle("Sélectionnez votre Age").setItems(countries, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	                   // The 'which' argument contains the index position
	                   // of the selected item
	            	   etAge.setText(countries[which]);
	               }
	        });
			final AlertDialog alert = builder.create();
			alert.show();
		} else if (vid == R.id.rlValidPassagers) {
			// update Passager with textview content
			passager.setAge(etAge.getText().toString());
			passager.setNom(etNom.getText().toString());
			passager.setPrenom(etPrenom.getText().toString());
			
			if (passager.isValid(getResources())) { // Si le passager saisi est valide alors on retourne à l'activité précédente
				returnCode = RESULT_OK;
				finish();
			} else {
				SimpleAlertDialog dialog = new SimpleAlertDialog(this, passager.errorMessages());
				dialog.show();
			}
		}
	}
}
