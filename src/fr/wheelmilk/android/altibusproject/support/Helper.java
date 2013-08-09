package fr.wheelmilk.android.altibusproject.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class Helper {

	// Affiche un Toast court et centré
	public static void grilledRare(Activity activity, String ficelle) {
		Toast toast = Toast.makeText(activity, ficelle, Toast.LENGTH_SHORT);
		TextView v = (TextView) toast.getView().findViewById( android.R.id.message);
		if (v != null)
			v.setGravity(Gravity.CENTER);
		toast.show();
	}
	// Affiche un Toast long et centré
	public static void grilledWellDone(Activity activity, String ficelle) {
		Toast toast = Toast.makeText(activity, ficelle, Toast.LENGTH_LONG);
		TextView v = (TextView) toast.getView().findViewById( android.R.id.message);
		if (v != null)
			v.setGravity(Gravity.CENTER);
		toast.show();
	}
	
	public static String capitalizeFirstLetter(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	public static String prettifyDate(Date date, String format) {
		if (format == null) format = "EEEE d MMMM yyyy";
		String tmpDate = new SimpleDateFormat(format, Locale.FRANCE).format(date);
		return capitalizeFirstLetter(tmpDate.substring(0, 1).toUpperCase() + tmpDate.substring(1));
	}
}
