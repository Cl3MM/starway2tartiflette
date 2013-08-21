package fr.wheelmilk.android.altibusproject;

import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;
import fr.wheelmilk.android.altibusproject.models.AltibusDataReservation;
import fr.wheelmilk.android.altibusproject.models.Billet;
import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.models.Passagers;
import fr.wheelmilk.android.altibusproject.models.Reservation;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class PageAchat extends PageFactory implements OnWebserviceListenner {
	private TextView tvPassagers;
	Passagers passagers;
	Billet billet;
	protected TextView tvMontant;
	private AltibusDataReservation reservation;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Config.PASSAGERS_RETOUR_CODE) {
			Helper.grilledRare(getActivity(), "Passagers Sélectionnés !!!");
			if (data != null) setPassagersResult(data, tvPassagers);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	public void setPassagersResult(Intent data, TextView tv) {
		passagers = data.getParcelableExtra("passagers");
		billet = new Billet( passagers, (GaresArrivee) tvGareArrivee.getTag(), 
				(java.util.Date) tvDateAller.getTag(), (HorrairesAller) tvHeureAller.getTag(), 
				(java.util.Date) tvDateRetour.getTag(), (HorrairesRetour) tvHeureRetour.getTag() );
		tv.setText(String.valueOf(passagers.size()));
		tv.setTag(passagers);
		startAsyncHTTPRequest();
	}
	
	protected void startAsyncHTTPRequest() {

		RequestParams params = billet.setParams(getActivity());
//		RequestParams params = passagers.getParams(getActivity());
		Log.v(this.getClass().toString(), params.toString());
//		params = setParams(params);
//		new AltibusWebservice<PageAchat>(this, "data.aspx?", params);
//		http://www.altibus.com/iphone/enregistrementReservation.aspx
		AltibusRestClient.post("iphone/enregistrementReservation.aspx?", params, new GaresAsyncHttpResponseHandler(this));
	}
	
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.id.llPassagers) {
			startPassagerActivity();
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
	@Override
	protected void setUpChildrenPages() {
		// Add more layout stuffs to the default if needed 
		layoutView.findViewById(R.id.llPassagers).setOnClickListener(this);
		tvPassagers = (TextView) layoutView.findViewById(R.id.tvPassagers);
		tvMontant = (TextView) layoutView.findViewById(R.id.tvMontant);
	}
	static PageAchat init(int val) {
		PageAchat page = new PageAchat();
		Bundle args = new Bundle();
		args.putInt("val", val);
		page.setArguments(args);
		return page;
	}
	
	@Override
	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.achat_billets_fragment, container, false);
	}
	@Override
	public void onWebserviceSuccess(String xmlString) {
		Log.v(this.getClass().toString(), xmlString);
		reservation = (AltibusDataReservation) new AltibusSerializer().serializeXmlToObject(xmlString);

		if (reservation != null) {
			Log.v(this.getClass().toString(), "Montant: " + reservation.getMontant());
			tvMontant.setText( reservation.getMontant() );
		} else {
			// Problème de sérialisation
			Log.v(this.getClass().toString(), "Serializer faillure :(");
		}
	}
	@Override
	public void onWebserviceFailure() {
		Log.v(this.getClass().toString(), "Webservice Failure :(");
		
	}
}
