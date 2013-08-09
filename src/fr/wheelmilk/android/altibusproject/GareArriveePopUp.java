package fr.wheelmilk.android.altibusproject;

import android.os.Bundle;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;

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
		new AltibusWebservice<GareArriveePopUp>(this, "data.aspx?", params);
	}
	@Override
	protected void addLayoutCustomizations() {
		TextView horrairesTvGareDepart = (TextView) findViewById(R.id.horrairesTvGareDepart);
		horrairesTvGareDepart.setText(gareDepart);
	}
}
