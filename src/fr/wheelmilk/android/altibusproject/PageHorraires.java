package fr.wheelmilk.android.altibusproject;

import fr.wheelmilk.android.altibusproject.support.Config;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageHorraires extends PageFactory {

	@Override
	protected void startGareAllerPopUpActivity() {
		Intent i = new Intent(this.getActivity(), GareAllerPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("title", getActivity().getResources().getString(R.string.garesAllerTitle));
		  
		startActivityForResult(i, Config.GARE_ALLER_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
}