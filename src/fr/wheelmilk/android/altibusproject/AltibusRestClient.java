package fr.wheelmilk.android.altibusproject;

import android.util.Log;

import com.loopj.android.http.*;

public class AltibusRestClient {
//	  private static final String BASE_URL = "http://www.altibus.com/sw/altibus/";
	  private static final String BASE_URL = "http://www.altibus.com/";
	  // sw/altibus/
// data.aspx?tip=gps7
	  //http://www.altibus.com/iphone/enregistrementReservation.aspx
//	  http://www.altibus.com/iphone/enregistrementReservation.aspx?tel=&aa=ne_pas_supprimer&version=1&adresse=&nom1=tortue&cp=&prenom=Bob&nom=Marley&age1=14&age=28&adresse2=Adresse 2&email=&pays=Pays&ville=Ville&prenom1=bernard

	  private static AsyncHttpClient client = new AsyncHttpClient();

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  Log.v("AltibusRestClient", getAbsoluteUrl(url)+params.toString() );
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }

	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  Log.v("AltibusRestClient", getAbsoluteUrl(url)+params.toString() );
		  client.post(getAbsoluteUrl(url), params, responseHandler);
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }
}
