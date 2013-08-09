package fr.wheelmilk.android.altibusproject;

public interface OnWebserviceListenner {
    public void onWebserviceSuccess(String xmlString);
    public void onWebserviceFailure();
}
