package fr.wheelmilk.android.altibusproject;

import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageAchat extends PageFactory {
	private TextView tvPassagers;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Config.PASSAGERS_RETOUR_CODE) {
			Helper.grilledRare(getActivity(), "Passagers Sélectionnés !!!");
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
		
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
		Intent i = new Intent(this.getActivity(), PassagersPopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.putExtra("popupColor", popupColor);
		startActivityForResult(i, Config.PASSAGERS_RETOUR_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);	
	}
	@Override
	protected void setUpChildrenPages() {
		// Add more layout stuffs to the default if needed 
		layoutView.findViewById(R.id.llPassagers).setOnClickListener(this);
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
}
