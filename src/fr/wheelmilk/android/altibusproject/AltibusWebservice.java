package fr.wheelmilk.android.altibusproject;

import com.loopj.android.http.RequestParams;

public class AltibusWebservice<T extends OnWebserviceListenner> {
	
	public AltibusWebservice(T parent, String url, RequestParams params) {
		AltibusRestClient.get(url, params, new GaresAsyncHttpResponseHandler(parent));
	}
}