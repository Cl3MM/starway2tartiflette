package fr.wheelmilk.android.altibusproject;

import com.loopj.android.http.RequestParams;

import fr.wheelmilk.android.altibusproject.support.Config;

import android.graphics.Color;
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

		if (popupColor == Color.parseColor(Config.POPUP_ORANGE_COLOR)) {
			listeDesGares.setSelector(getResources().getDrawable(R.drawable.orange_horraire_list_selector));
		}
	}
}