package fr.wheelmilk.android.altibusproject;

import java.util.Calendar;
import java.util.Date;

import com.loopj.android.http.RequestParams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.wheelmilk.android.altibusproject.models.AltibusDataReservation;
import fr.wheelmilk.android.altibusproject.models.Billet;
import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.models.Passagers;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class PageAchat extends PageFactory implements OnWebserviceListenner, DialogInterface.OnClickListener {
	private TextView tvPassagers;
	Passagers passagers = new Passagers();
	Billet billet = new Billet();
	boolean firstRun = true;
	protected TextView tvMontant;
	private AltibusDataReservation reservation;
	SimpleAlertDialog mDialog;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Config.PASSAGERS_RETOUR_CODE) {
			if (data != null) setPassagersResult(data, tvPassagers);
		} else if (requestCode == Config.PAIEMENT_RETOUR_CODE) {
			Log.v(this.getClass().toString(), "Yeah, billet acheté !!!");
			notifyBilletFragment();
			harReset();
			Log.v(this.getClass().toString(), "Hard Reset done...");
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	public void setPassagersResult(Intent data, TextView tv) {
		passagers = data.getParcelableExtra("passagers");
		tv.setText(String.valueOf(passagers.size()));
		tv.setTag(passagers);
//		startAsyncHTTPRequest();
	}
	private void notifyBilletFragment() {
	    Intent intent = new Intent("billetCreated");
	    sendLocationBroadcast(intent);
	}

	private void sendLocationBroadcast(Intent intent){
		Log.v(getClass().toString(), "Sending Update broadcast");
	    intent.putExtra("action", "update");
	    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}

	protected void startAsyncHTTPRequest() {
		layoutView.findViewById(R.id.rlLoading).setVisibility(View.VISIBLE);
		billet = new Billet( passagers, (GaresDepart) tvGareAller.getTag(), (GaresArrivee) tvGareArrivee.getTag(), 
				(java.util.Date) tvDateAller.getTag(), (HorrairesAller) tvHeureAller.getTag(), 
				(java.util.Date) tvDateRetour.getTag(), (HorrairesRetour) tvHeureRetour.getTag() );
		RequestParams params = billet.setParams(getActivity());
		AltibusRestClient.post("iphone/enregistrementReservation.aspx?", params, new GaresAsyncHttpResponseHandler(this));
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		Log.v(getClass().toString(), "Vid: "+vid);
		Log.v(getClass().toString(), "rlBtnNew: "+R.id.rlButtonNew);
		if (vid == R.id.llPassagers) {
			startPassagerActivity();
		} else if (vid == R.id.rlButtonPaiement) {
			if (billet.hasValidReservation()) {
				mDialog = new SimpleAlertDialog(getActivity(), getString(R.string.cgv), getString(R.string.acceptAndPay), getString(R.string.cancel), this);
				mDialog.setTitle(getString(R.string.cgvTitle));
				mDialog.setCancelableProperly(false);
				mDialog.setTag("cgv");
				mDialog.show();
			} else {
				Helper.grilledRare(getActivity(), getResources().getString(R.string.erreurChampsNonRemplis));
			}
		} else if(vid == R.id.rlButtonNew) {
			mDialog = new SimpleAlertDialog(getActivity(), getString(R.string.effectuerNouvelleRecherche), getString(R.string.ok), getString(R.string.cancel), this);
			mDialog.setTitle(getString(R.string.avertissement));
			mDialog.setCancelableProperly(false);
			mDialog.setTag("hardReset");
			mDialog.show();
		} else {
			super.onClick(v);
		}
	}
//	1377546388586
//update billetdb
//set da = 1377546388586
//where id IN (1,2,3);

	private void startPassagerActivity() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, -1);
		Log.v("Spéciale CLEM", "MEGA DATE LONG " + c.getTime().getTime());
		Log.v("Spéciale CLEM", "MEGA DATE " + c.getTime());
		Intent i = new Intent(getActivity(), PassagersPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (tvPassagers.getTag() != null) {
			Bundle b = new Bundle();
			b.putParcelable("passagers", (Passagers) tvPassagers.getTag());
			i.putExtras(b);
		}
		startActivityForResult(i, Config.PASSAGERS_RETOUR_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);	
	}
	
	private void startPaiementActivity() {
		Bundle b = new Bundle();
		b.putParcelable("billet", billet);
		Intent i = new Intent(getActivity(), PaiementPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtras(b);
		startActivityForResult(i, Config.PAIEMENT_RETOUR_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);	
	}
	
	@Override
	protected void setUpChildrenPages() {
		// Add more layout stuffs to the default if needed 
		layoutView.findViewById(R.id.llPassagers).setOnClickListener(this);
		layoutView.findViewById(R.id.rlButtonPaiement).setOnClickListener(this);
		layoutView.findViewById(R.id.rlButtonNew).setOnClickListener(this);
		tvPassagers = (TextView) layoutView.findViewById(R.id.tvPassagers);
		tvMontant = (TextView) layoutView.findViewById(R.id.tvMontant);
		tvMontant.setTextColor(getResources().getColor(R.color.greenFont));
		tvMontant.setText("");
	}
	static PageAchat init(int val) {
		PageAchat page = new PageAchat();
		Bundle args = new Bundle();
		args.putInt("val", val);
		page.setArguments(args);
		return page;
	}
	@Override
	protected void updateMontant() {
		if (tvHeureAller.getTag() != null && !passagers.isEmpty()) {
			startAsyncHTTPRequest();
		} else {
			tvMontant.setText(getString(R.string.emptyString));
			tvMontant.setTag(null);
		}
	}
	@Override
	protected void resetTextViews() {
		super.resetTextViews();
		tvMontant.setText(getResources().getString(R.string.emptyString));
		tvMontant.setTag(null);
	}
	@Override
	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.achat_billets_fragment, container, false);
	}
	@Override
	public void onWebserviceSuccess(String xmlString) {
		Log.v(this.getClass().toString(), xmlString);
		reservation = (AltibusDataReservation) new AltibusSerializer(AltibusDataReservation.class).serializeXmlToObject(xmlString);
		layoutView.findViewById(R.id.rlLoading).setVisibility(View.GONE);
		if (reservation != null) {
			billet.setReservation(reservation.reservation);
			Log.v(this.getClass().toString(), "Montant: " + reservation.getMontant());
			tvMontant.setText( reservation.getPrettyMontant() );
		} else {
			tvMontant.setText( "" );
			// Problème de sérialisation
			Log.v(this.getClass().toString(), "Serializer faillure :(");
		}
	}
	@Override
	public void onWebserviceFailure() {
		layoutView.findViewById(R.id.rlLoading).setVisibility(View.GONE);
		Log.v(this.getClass().toString(), "Webservice Failure :(");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (firstRun) {
			firstRun = false;
		} else {
			if( passagers.isValid(getResources()) && tvGareAller.getTag() != null && tvGareArrivee.getTag() != null && tvDateAller.getTag() != null && tvHeureAller.getTag() != null ) {
				startAsyncHTTPRequest();
			}
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		Log.v(getClass().toString(), "Dialog class: " + dialog.getClass());
		if (mDialog != null && mDialog.isVisible()) {
			if (mDialog.getTag().equals("cgv")) {
				Log.v(getClass().toString(), "Launching Payment...");
				mDialog.dismiss();
				if (which == DialogInterface.BUTTON_POSITIVE) {
					startPaiementActivity();
				}
			} else if (mDialog.getTag().equals("hardReset")) {
				Log.v(getClass().toString(), "Resetting fields...");
				if (which == DialogInterface.BUTTON_POSITIVE) {
					mDialog.dismiss();
					harReset();
				}
			}
		}
	}
	public void harReset() {
		resetTextViews();
		tvGareAller.setTag(null);
		tvGareAller.setText(getString(R.string.rechercheGare));
		tvGareArrivee.setTag(null);
		tvGareArrivee.setText(getString(R.string.rechercheGare));
		tvDateAller.setTag(null);
		tvDateAller.setText(getString(R.string.rechercheDate));
		tvDateRetour.setTag(null);
		tvDateRetour.setText(getString(R.string.rechercheDate));
		reservation = null;
		billet = new Billet();
	}
	
	public void createNewSearchFromPageHorraires(GaresDepart _gd, GaresArrivee _ga, Date _da, HorrairesAller _ha, Date _dr, HorrairesRetour _hr) {
		tvGareAller.setTag(_gd);
		tvGareAller.setText(_gd.gareName());
		tvGareArrivee.setTag(_ga);
		tvGareArrivee.setText(_ga.gareName());
		tvDateAller.setTag(_da);
		tvDateAller.setText(Helper.prettifyDate(_da, null));
		tvDateRetour.setTag(_dr);
		tvHeureAller.setTag(_ha);
		StringBuilder s = new StringBuilder(_ha.heureAller());
		s.append(" - ").append(_ha.heureArrivee());
		
		tvHeureAller.setText(s);
		
		if (_dr != null) {
			tvDateRetour.setText(Helper.prettifyDate(_dr, null));
			llRetour.setVisibility(View.VISIBLE);
		} else {
			tvDateRetour.setText(getString(R.string.rechercheDate));			
		}
		tvHeureRetour.setTag(_hr);
		if (_hr != null) {
			s = new StringBuilder(_hr.heureAller());
			s.append(" - ").append(_hr.heureArrivee());
			tvHeureRetour.setText(s.toString());
		} else {
			tvHeureRetour.setText(getString(R.string.rechercherHorraire));
		}
		reservation = null;
	}
}