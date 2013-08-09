package fr.wheelmilk.android.altibusproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockDialogFragment;

@SuppressLint({ "ValidFragment", "DefaultLocale" })
public class DatePickerFragment extends SherlockDialogFragment implements DatePickerDialog.OnDateSetListener {
	TextView tv;
	public DatePickerFragment(TextView tv) {
		this.tv = tv;	
	}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.gares_aller_dialog, null);
		View customTitle = inflater.inflate(R.layout.custom_dialog_title, null);
		TextView dlgTitle = (TextView) customTitle.findViewById(R.id.title);
		
		final Calendar c = Calendar.getInstance();
		// Save the date if it exists
		if (this.tv.getTag() != null) {
			c.setTime( (Date) this.tv.getTag() );			
		} //else {
    	// Use the current date as the default date in the picker
//    	final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dlgTitle.setText("Choisir une date aller");
        
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), this, year, month, day);
        dlg.setCustomTitle(customTitle);
        return dlg;
    }

    @Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
    	String strDate = day + " / " + (month+1) + " / " + year;
    	Date date;
		try {
			date = new SimpleDateFormat("d / M / yyyy", Locale.FRANCE).parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = null;
		}
		
		String prettyDate = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRANCE).format(date);
		String capitol   = Character.toString(prettyDate.charAt(0)).toUpperCase();
		String prettyDateCapitalized = capitol + prettyDate.substring(1,prettyDate.length());
		Log.v(DatePickerFragment.class.toString(), prettyDateCapitalized);
	
    	this.tv.setText(prettyDateCapitalized);
		Log.v(DatePickerFragment.class.toString(), date.toString());
		Log.v(DatePickerFragment.class.toString(), strDate);
   		this.tv.setTag(date);
    }
}
