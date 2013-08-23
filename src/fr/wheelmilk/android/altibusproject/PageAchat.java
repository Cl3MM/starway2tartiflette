package fr.wheelmilk.android.altibusproject;

import com.loopj.android.http.RequestParams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

	protected void startAsyncHTTPRequest() {
		billet = new Billet( passagers, (GaresDepart) tvGareAller.getTag(), (GaresArrivee) tvGareArrivee.getTag(), 
				(java.util.Date) tvDateAller.getTag(), (HorrairesAller) tvHeureAller.getTag(), 
				(java.util.Date) tvDateRetour.getTag(), (HorrairesRetour) tvHeureRetour.getTag() );
		RequestParams params = billet.setParams(getActivity());
		Log.v(this.getClass().toString(), params.toString());
		AltibusRestClient.post("iphone/enregistrementReservation.aspx?", params, new GaresAsyncHttpResponseHandler(this));
	}
	
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.id.llPassagers) {
			startPassagerActivity();
		} else if (vid == R.id.rlButtonPaiement) {
			if (billet.hasValidReservation()) {
				mDialog = new SimpleAlertDialog(getActivity(), getString(R.string.cgv), getString(R.string.acceptAndPay), getString(R.string.cancel), this);
				mDialog.show();
			} else {
				Helper.grilledRare(getActivity(), getResources().getString(R.string.erreurChampsNonRemplis));
			}
		} else {
			super.onClick(v);
		}
	}

	private void startPassagerActivity() {
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
		startAsyncHTTPRequest();
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
		mDialog.dismiss();
		if (which == DialogInterface.BUTTON_POSITIVE) {
			startPaiementActivity();
		}
	}
}