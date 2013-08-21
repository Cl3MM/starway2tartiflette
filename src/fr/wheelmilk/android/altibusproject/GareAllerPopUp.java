package fr.wheelmilk.android.altibusproject;

import com.loopj.android.http.RequestParams;

import android.view.View;
import android.widget.LinearLayout;

public class GareAllerPopUp extends ActivityPopUpFactory {
//	protected static final String popupColor = Config.POPUP_GREEN_COLOR;

	@Override
	protected void startAsyncHTTPRequest() {
		RequestParams params = new RequestParams();
		params.put("tip", "gps7");
		new AltibusWebservice<ActivityPopUpFactory>(this, "sw/altibus/data.aspx?", params);
	}

	@Override
	protected void addLayoutCustomizations() {
		LinearLayout llHorrairesDepart = (LinearLayout) findViewById(R.id.llHorrairesDepart);
		llHorrairesDepart.setVisibility(View.GONE);
		LinearLayout ll1 = (LinearLayout) findViewById(R.id.llBgToChange2);
		ll1.setBackgroundColor(popupColor);

	}
}