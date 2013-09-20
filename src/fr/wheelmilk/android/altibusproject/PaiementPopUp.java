package fr.wheelmilk.android.altibusproject;

import java.sql.SQLException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.RequestParams;

import fr.wheelmilk.android.altibusproject.models.Billet;
import fr.wheelmilk.android.altibusproject.models.BilletDB;
import fr.wheelmilk.android.altibusproject.models.ResultatPaiement;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.DatabaseHelper;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class PaiementPopUp extends SherlockActivity implements OnClickListener, OnWebserviceListenner, DialogInterface.OnClickListener {
	private final String LOG_TAG = getClass().getSimpleName();
	private DatabaseHelper databaseHelper = null;
	Dao<BilletDB, Integer> billetsDao;

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
	RequestParams params;
	private ResultatPaiement resultatPaiement;
	RelativeLayout rlLoading;
	private boolean isProceedingPayment = false;
	
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
		rlLoading = (RelativeLayout) findViewById(R.id.rlLoading);
		rlLoading.setVisibility(View.GONE);

		rlBtnValidate.setOnClickListener(this);
		StringBuilder s = new StringBuilder(getString(R.string.montantTransaction));
		s.append(" ");
		s.append(billet.getMontant());
		tvMontant.setText(s);
		tvRefReservation.setText(billet.getRefReservation());
		
		setTestData();
	}
	private void setTestData() {
		etAnnee.setText("13");
		etMois.setText("12");
		etCode.setText("666");
		etNumeroCarte.setText("1234567891012131");
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		try {
			billetsDao = getHelper().getBilletDataDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			billetsDao = null;
		}
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
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
		if (vid == R.id.rlBtnValidate) {
			isProceedingPayment = true;
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
			// https://www.altibus.com/iphone/cybermut/etp1.aspx
			findViewById(R.id.rlBtnValidate).setOnClickListener(null);
			params = new RequestParams();
			params.put("nc", cb);
			if (annee.length() == 2) params.put("av", "20" + annee);
			else params.put("av", annee);
			params.put("mv", mois);
			params.put("c", code);
			params.put("r", billet.getRefReservation());
			
			Log.v(this.getClass().toString(), params.toString());
			rlLoading.setVisibility(View.VISIBLE);
			AltibusRestClient.postSSL("iphone/cybermut/etp1.aspx?", params, new GaresAsyncHttpResponseHandler(this));
		} else {
			isProceedingPayment = false;
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
	@Override
	public void onWebserviceSuccess(String xmlString) {
		Log.v(getClass().toString(), xmlString);
		resultatPaiement = (ResultatPaiement) new AltibusSerializer(ResultatPaiement.class).serializeXmlToObject(xmlString);
		if (resultatPaiement != null) {
			afterPaymentValidation();
			Log.v(this.getClass().toString(), "Numéro de reservation: " +  resultatPaiement.getReference());
		} else {
			rlLoading.setVisibility(View.GONE);
			Helper.grilledRare(this, getString(R.string.erreurSerialisation));
			Log.v(this.getClass().toString(), "Serializer faillure :(");
		}
	}
	private void afterPaymentValidation() {
		if (resultatPaiement.getCdr() == -5 ) { // Paiement OK
			returnCode = RESULT_OK;
			saveBilletToDB();
			mDialog = new SimpleAlertDialog(this, getString(R.string.paymentSuccess), getString(R.string.ok), null, this);
			mDialog.setTitle("Paiement Effectué");
		} else { // Paiement Foiré :(
			returnCode = Config.PAYMENT_REFUSED;
			mDialog = new SimpleAlertDialog(this, getString(R.string.paymentRefused), getString(R.string.ok), null, this);
			mDialog.setTitle("Erreur");
		}
		rlLoading.setVisibility(View.GONE);
		mDialog.setCancelableProperly(false);
		mDialog.show();		
	}
	private void saveBilletToDB() {

		Log.v(getClass().toString(), "Billet :" + billet.toString());
		if (billetsDao == null) {
			Helper.grilledRare(this, "Impossible de sauvegarder le billet dans la base de données");
			Log.v(getClass().toString(), "Impossible de sauvegarder le billet dans la base de données");
			Log.v(getClass().toString(), "Billet : " + billet.toString());
		} else {
			try {
				Log.v(getClass().toString(), "Saving ticket to DB...");
				BilletDB billetDB = new BilletDB(billet, false);
				billetsDao.create(billetDB);
				
				// Creation Billet Retour
				if(billet.hasHorraireRetour()) {
					Log.v(getClass().toString(), "Saving return ticket to DB...");
					billetDB = new BilletDB(billet, true);
					billetsDao.create(billetDB);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onWebserviceFailure() {
		Log.v(getClass().toString(), "Failure :'(");
	}
	@Override
	public void onClick(DialogInterface dlg, int which) {
//		notifyBilletFragment();
		mDialog.dismiss();
		finish();		
	}
	@Override
	public void onBackPressed() {
		Log.v(getClass().toString(), "onBackPressed.....");
		if( !isProceedingPayment && mDialog == null) super.onBackPressed();
		else {
			returnCode = RESULT_CANCELED;
			finish();
		}
	}
	private void notifyBilletFragment() {
	    Intent intent = new Intent("billetCreated");
	    sendLocationBroadcast(intent);
	}

	private void sendLocationBroadcast(Intent intent){
		Log.v(getClass().toString(), "Sending Update broadcast");
	    intent.putExtra("action", "update");
	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
}
