package fr.wheelmilk.android.altibusproject;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;

import fr.wheelmilk.android.altibusproject.support.Config;

public class GareArriveePopUp extends ActivityPopUpFactory {
	private String gareDepart;

	@Override
	public void initialize( Bundle extras) {
		super.initialize(extras);
		gareDepart = extras.getString("gareDepart");
	}
	@Override
	protected void startAsyncHTTPRequest() {
		RequestParams params = new RequestParams();
		params.put("tip", "garesarrivee7");
		params.put("gd", this.gareDepart);
		new AltibusWebservice<GareArriveePopUp>(this, "sw/altibus/data.aspx?", params);
	}
	@Override
	protected void addLayoutCustomizations() {
		TextView horrairesTvGareDepart = (TextView) findViewById(R.id.horrairesTvGareDepart);
		horrairesTvGareDepart.setText(gareDepart);
		horrairesTvGareDepart.setTextColor(popupColor);
		LinearLayout ll1 = (LinearLayout) findViewById(R.id.llBgToChange1);
		LinearLayout ll2 = (LinearLayout) findViewById(R.id.llBgToChange2);
		ll1.setBackgroundColor(popupColor);
		ll2.setBackgroundColor(popupColor);
		if (popupColor == Color.parseColor(Config.POPUP_ORANGE_COLOR)) {
			listeDesGares.setSelector(getResources().getDrawable(R.drawable.orange_horraire_list_selector));
		}
	}
}
