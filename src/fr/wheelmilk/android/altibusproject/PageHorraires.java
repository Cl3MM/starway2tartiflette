package fr.wheelmilk.android.altibusproject;

import java.util.Date;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageHorraires extends PageFactory {

	@Override
	protected void setUpPopUpColor() {
		popupColor = Config.POPUP_ORANGE_COLOR;
	}
	@Override
	protected void setUpChildrenPages() {
		layoutView.findViewById(R.id.tvBtnAcheterAPartirDesHorraires).setOnClickListener(this);
	}
	static PageHorraires init(int val) {
		PageHorraires page = new PageHorraires();
		Bundle args = new Bundle();
		args.putInt("val", val);
		page.setArguments(args);
		return page;
	}
	
	@Override
	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.consultation_horraires_layout, container, false);
	}
	
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.id.tvBtnAcheterAPartirDesHorraires) {
			Log.v(getClass().toString(), "Bouton Acheter à partir des horraires cliqué");
		    if (tvGareAller.getTag() != null && !this.tvGareArrivee.getText().equals(getResources().getString(R.string.rechercherGare)) && 
		    		tvGareArrivee.getTag() != null && tvDateAller.getTag() != null) {
		    	sendInfoToPageAchat();
		    }else {
		    	Helper.grilledRare(getActivity(), getActivity().getResources().getString(R.string.erreurChampsNonRemplis));
		    }
		} else {
			super.onClick(v);
		}
	}

	private void sendInfoToPageAchat() {
		notifyBilletFragment();
	}
	private void notifyBilletFragment() {
	    Intent intent = new Intent("nouvelAchat");
	    sendLocationBroadcast(intent);
	}

	private void sendLocationBroadcast(Intent intent){
		Log.v(getClass().toString(), "Sending Update broadcast");

		intent.putExtra("action", "nouvelAchat");
	    intent.putExtra("gareAl", (GaresDepart) tvGareAller.getTag());
	    intent.putExtra("gareAr", (GaresArrivee) tvGareArrivee.getTag());
	    intent.putExtra("horraireA", (HorrairesAller) tvHeureAller.getTag());
	    intent.putExtra("allersimple", as.isChecked());
	    Date da = (Date) tvDateAller.getTag();
	    Date dr = null;
	    intent.putExtra("dateA", da.getTime() );

		if (tvDateRetour.getTag() != null) {
			dr = (Date) tvDateRetour.getTag();
			intent.putExtra("dateR", dr.getTime());
		}

	    if ( tvHeureRetour.getTag() != null ) {
	    	intent.putExtra("horraireR", (HorrairesRetour) tvHeureRetour.getTag());
	    }
	    
	    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
	}
}