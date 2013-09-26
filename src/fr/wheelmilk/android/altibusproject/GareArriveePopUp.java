package fr.wheelmilk.android.altibusproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.antidots.android.altibus.R;
import com.loopj.android.http.RequestParams;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.Stations;
import fr.wheelmilk.android.altibusproject.support.Config;

public class GareArriveePopUp extends ActivityPopUpFactory {
	private String gareDepart;
	private boolean isLoadingStation = false;
	private boolean isStation = false;

	@Override
	public void initialize( Bundle extras) {
		super.initialize(extras);
		gareDepart = extras.getString("gareDepart");
	}
	@Override
	protected void startHorairesAsyncHTTPRequest() {
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
	
	@Override
	protected void setSuccessfulResult(int position) {
		super.setSuccessfulResult(position);
		startStationAsyncHTTPRequest();
	}
	protected void startStationAsyncHTTPRequest() {
		showProgressBar();
		isLoadingStation  = true;
		RequestParams params = new RequestParams();
		params.put("tip", "station7");
		params.put("gd", gareDepart );
		params.put("ga", result );
		AltibusRestClient.get("sw/altibus/data.aspx?", params, new GaresAsyncHttpResponseHandler(this));
	}
	@Override
	protected void onPreFinish() {
		if (returnCode == RESULT_OK) {
			Intent data = new Intent();
			Bundle b = new Bundle();
			GaresArrivee ga = (GaresArrivee) altibusData.get(result);
			ga.setIsStation(isStation);
			b.putParcelable("tag", ga);
			data.putExtra("result", result);
			data.putExtras(b);
			setResult(returnCode, data);
		} else setResult(returnCode);
	}
	@Override
	public void finish() {
		if (!isLoadingStation) {
			super.finish();
		}
	}
	@Override
	public void onWebserviceSuccess(String xmlString) {
		if(xmlString.contains("<stations>")) {
			dismissProgressBar();
			isLoadingStation = false;
			 if (Config.DEBUG == 1) Log.v(getClass().toString(), xmlString);
			Stations stations = (Stations) new AltibusSerializer(Stations.class).serializeXmlToObject(xmlString);
			if(stations != null) {
				isStation  = stations.isStation();
				 if (Config.DEBUG == 1) Log.v(getClass().toString(), "YEAHHH !!! Check de la station !!!!");
				 if (Config.DEBUG == 1) Log.v(getClass().toString(), "isStation: " + (isStation ? "TRUE" : "FALSE"));
			}
			finish();
		} else {
			super.onWebserviceSuccess(xmlString);
		}
	}

	@Override
	public void onWebserviceFailure() {
		dismissProgressBar();
		returnCode = Config.WEBSERVICE_FAILLURE;
		isLoadingStation = false;
		finish();
	}
}
