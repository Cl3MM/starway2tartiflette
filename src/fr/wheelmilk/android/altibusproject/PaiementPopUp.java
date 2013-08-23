package fr.wheelmilk.android.altibusproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.Billet;
import fr.wheelmilk.android.altibusproject.support.Config;

public class PaiementPopUp extends SherlockActivity implements OnClickListener {
	int returnCode;
	SimpleAlertDialog mDialog;
	Billet billet;
	TextView tvRefReservation;
	TextView tvMontant;
	EditText etNumeroCarte;
	EditText etMois;
	EditText etAnnee;
	EditText etCode;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.paiement_popup);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		initialize(getIntent().getExtras());

		tvRefReservation = (TextView) findViewById(R.id.tvRefReservation);
		tvMontant = (TextView) findViewById(R.id.tvMontant);
		etAnnee = (EditText) findViewById(R.id.etAnnee);
		etCode = (EditText) findViewById(R.id.etCodeCB);
		etNumeroCarte = (EditText) findViewById(R.id.etNumeroCarte);
		
		StringBuilder s = new StringBuilder(getString(R.string.montantTransaction));
		s.append(" ");
		s.append(billet.getMontant());
		tvMontant.setText(s);
		tvRefReservation.setText(billet.getRefReservation());
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
		returnCode = RESULT_CANCELED;	
		finish();
		return false;
    }
	public void initialize( Bundle extras ) {
		if (extras == null) {
			returnCode = Config.EXTRA_FAILURE;
			finish();
		}
		billet = extras.getParcelable("billet");
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	protected void onPreFinish() {
		Intent data = new Intent();
		Bundle b = new Bundle();
		
//		b.putParcelable("passager", passager);
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
}
