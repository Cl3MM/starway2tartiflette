package fr.wheelmilk.android.altibusproject;

import android.view.View;
import android.widget.LinearLayout;

public class GareAllerPopUp extends ActivityPopUpFactory {
//	protected static final String popupColor = Config.POPUP_GREEN_COLOR;

	@Override
	protected void startAsyncHTTPRequest() {
		new AltibusWebservice<ActivityPopUpFactory>(this, "data.aspx?tip=gps7", null);
	}

	@Override
	protected void addLayoutCustomizations() {
		LinearLayout llHorrairesDepart = (LinearLayout) findViewById(R.id.llHorrairesDepart);
		llHorrairesDepart.setVisibility(View.GONE);
		LinearLayout ll1 = (LinearLayout) findViewById(R.id.llBgToChange2);
		ll1.setBackgroundColor(popupColor);

	}
}