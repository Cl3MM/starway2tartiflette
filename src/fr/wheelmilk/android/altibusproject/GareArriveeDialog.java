package fr.wheelmilk.android.altibusproject;

import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.widget.TextView;

public class GareArriveeDialog extends GaresDialog {

	String gareDepart;
	
	public GareArriveeDialog(Activity activity, TextView tv, String params) {
		super(activity, tv, activity.getResources().getString(R.string.garesArriveeTitle));
		this.gareDepart = params;
	}
	// http://www.altibus.com/sw/altibus/data.aspx?tip=garesarrivee7&gd=
	@Override
	public void prepareDialogFragment() {
		this.mDialog.show();
		RequestParams params = new RequestParams();
		 params.put("tip", "garesarrivee7");
		 params.put("gd", this.gareDepart);
		new AltibusWebservice<GareArriveeDialog>(this, "data.aspx?", params);
	}
}
