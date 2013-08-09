package fr.wheelmilk.android.altibusproject;

import android.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GaresAsyncHttpResponseHandler<T extends OnWebserviceListenner> extends AsyncHttpResponseHandler {
	T parent;
	
	public GaresAsyncHttpResponseHandler(T parent) {
		this.parent = parent;
	}

    @Override
    public void onSuccess(String xmlString) {
    	Log.v(GaresAsyncHttpResponseHandler.class.toString(), "Webservice Success !!");
    	parent.onWebserviceSuccess(xmlString);
    }

    @Override
    public void onFailure(Throwable e, String response) {
    	// Response failed :(
    	Log.v(GaresAsyncHttpResponseHandler.class.toString(), "Webservice Faillure !!");
    	parent.onWebserviceFailure();
    }
}

/// Original

//public class GareAllerAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
//	DialogAdapterFactory parent;
//	
//	public GareAllerAsyncHttpResponseHandler(DialogAdapterFactory parent) {
//		this.parent = parent;
//	}
//
//
//    @Override
//    public void onSuccess(String xmlString) {
//    	Log.v(GareAllerAsyncHttpResponseHandler.class.toString(), "Webservice Success !!");
//    	parent.onWebserviceSuccess(xmlString);
//    }
//
//    @Override
//    public void onFailure(Throwable e, String response) {
//    	// Response failed :(
//    	Log.v(GareAllerAsyncHttpResponseHandler.class.toString(), "Webservice Faillure !!");
//    	parent.onWebserviceFailure();
//    }
//}