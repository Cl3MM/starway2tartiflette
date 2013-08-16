package fr.wheelmilk.android.altibusproject;

import fr.wheelmilk.android.altibusproject.support.Config;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageHorraires extends PageFactory {

	@Override
	protected void setUpPopUpColor() {
		popupColor = Config.POPUP_ORANGE_COLOR;
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