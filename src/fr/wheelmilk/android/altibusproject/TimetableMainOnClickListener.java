package fr.wheelmilk.android.altibusproject;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class TimetableMainOnClickListener implements View.OnClickListener {

	DialogAdapterFactory parent;
	Activity activity;

//	public TimetableMainOnClickListener(Activity activity,
//			DialogAdapterFactory parent) {
//		this.activity = activity;
//		this.parent = parent;
//	}

	@Override
	public void onClick(View v) {
		String tag = v.getTag().toString();
		if (tag.equals("depart")) {
			Toast.makeText(v.getContext(), tag + " clicked !", Toast.LENGTH_SHORT).show();
		} else if (tag.equals("arrivee")) {
			Toast.makeText(v.getContext(), tag + " clicked !", Toast.LENGTH_SHORT).show();
		} else if (tag.equals("horraireAller")) {
			Toast.makeText(v.getContext(), tag + " clicked !", Toast.LENGTH_SHORT).show();
		} else if ( tag.equals("dateAller")) {
			Toast.makeText(v.getContext(), tag + " clicked !", Toast.LENGTH_SHORT).show();
		} else {
			// something went wrong
			Toast.makeText(v.getContext(), "Erreur inconnue :(", Toast.LENGTH_SHORT).show();			
		}
		Toast.makeText(v.getContext(), tag + " clicked !", Toast.LENGTH_SHORT).show();
	}
}