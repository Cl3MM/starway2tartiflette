package fr.wheelmilk.android.altibusproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.RequestParams;

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
	RelativeLayout rlBtnValidate;
	
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
		etMois = (EditText) findViewById(R.id.etMois);
		etNumeroCarte = (EditText) findViewById(R.id.etNumeroCarte);
		rlBtnValidate = (RelativeLayout) findViewById(R.id.rlBtnValidate);
		
		rlBtnValidate.setOnClickListener(this);
		StringBuilder s = new StringBuilder(getString(R.string.montantTransaction));
		s.append(" ");
		s.append(billet.getMontant());
		tvMontant.setText(s);
		tvRefReservation.setText(billet.getRefReservation());
		
//		etNumeroCarte.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//			}
//			@Override
//			public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
//			@Override
//			public synchronized void afterTextChanged(Editable s) {
//				String str = etNumeroCarte.getText().toString();
//				Log.v(this.getClass().toString(), str);
//				etNumeroCarte.setText( formatText(s) );
//			}
//			private Editable formatText(Editable text) {
//			    int sep1Loc = 3;
//			    int sep2Loc = 8;
//			    int sep3Loc = 14;
//			    int sep4Loc = 20;
//
//			    if(text.length()==sep1Loc) {
//			    	text.append('-');
//			    	return text;
//			    }
//
//			    if(text.length()==sep2Loc) {
//			    	text.append('-');
//			    	return text;
//			    }
//
//			    if(text.length()==sep3Loc) {
//				    text.append('-');
//			    	return text;
//			    }
//
//			    if(text.length() > sep4Loc) {
//			    	return text;
//			    }
//			    
//			    return text;
//			}
//		});
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
	public void onClick(View v) {
		int vid = v.getId();
		Log.v(this.getClass().toString(), v.toString());
		Log.v(this.getClass().toString(), "ID: " + v.getId());
		if (vid == R.id.rlBtnValidate) {
			startPaiement();
		}
		
	}
	private void startPaiement() {
		String annee = etAnnee.getText().toString();
		String mois = etMois.getText().toString();
		String code = etCode.getText().toString();
		String cb = etNumeroCarte.getText().toString();
		
		ErrorMessage e = isValid(cb, annee, mois, code);
		if (e.isValid) {
			RequestParams params = billet.setParams(getActivity());
			Log.v(this.getClass().toString(), params.toString());
			AltibusRestClient.post("iphone/enregistrementReservation.aspx?", params, new GaresAsyncHttpResponseHandler(this));
		} else {
			SimpleAlertDialog dlg = new SimpleAlertDialog(this, e.messages.toString());
			dlg.show();
		}
		
	}
	private class ErrorMessage {
		StringBuilder messages = new StringBuilder();
		boolean isValid = true;
	}
	private ErrorMessage isValid(String cb, String annee, String mois, String code) {

		ErrorMessage e = new ErrorMessage();
		
		if(TextUtils.isEmpty(cb) || (cb.length() != 16) ) {
			e.isValid = false;
			e.messages.append(getResources().getString(R.string.erreurCB)).append("\n");
		}
		if(TextUtils.isEmpty(mois) || (mois.length() > 2) ) {
			e.isValid = false;
			e.messages.append(getResources().getString(R.string.erreurMois)).append("\n");
		}
		if(TextUtils.isEmpty(annee) || ( annee.length() != 2 && annee.length() != 4 ) ) {
			e.isValid = false;
			e.messages.append(getResources().getString(R.string.erreurAnnee)).append("\n");
		}
		if(TextUtils.isEmpty(code) || (code.length() != 3) ) {
			e.isValid = false;
			e.messages.append(getResources().getString(R.string.erreurCode)).append("\n");
		}
		
		return e;
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
