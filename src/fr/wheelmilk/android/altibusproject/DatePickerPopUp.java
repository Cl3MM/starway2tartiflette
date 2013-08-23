package fr.wheelmilk.android.altibusproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.R;
import fr.wheelmilk.android.altibusproject.support.Config;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DatePickerPopUp extends SherlockActivity implements View.OnClickListener, OnDateChangedListener {
	private String title;
	private String gareDepart;
	private String gareArrivee;
	private int popupColor;
	private Date date = null;
	private int code;
	protected final int DATE_ALLER_CODE 	= 300;
	protected final int HEURE_ALLER_CODE 	= 400;
	protected final int DATE_RETOUR_CODE 	= 500;

	public void initialize( Bundle extras) {
		gareDepart 	= extras.getString("gareDepart");
		gareArrivee = extras.getString("gareArrivee");
		title 		= extras.getString("title");
		date	   	= (Date) extras.getSerializable("date");
		code	   	= extras.getInt("code");
		String color = extras.getString("popupColor");
		popupColor = Color.parseColor( color );
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.date_picker_popup);
		initialize(getIntent().getExtras());

		// Changing background color depending on the Intent's popupColor argument
		LinearLayout llTitleUp = (LinearLayout) findViewById(R.id.llTitleContainer);
		llTitleUp.setBackgroundColor(popupColor);
		LinearLayout llTitleDown = (LinearLayout) findViewById(R.id.llTitleLayoutDown);
		llTitleDown.setBackgroundColor(popupColor);
		
		DatePicker dp = (DatePicker) findViewById(R.id.datePickerPopUp);
		TextView tvTitle = (TextView) findViewById(R.id.title);
		TextView tvDepart = (TextView) findViewById(R.id.horrairesTvGareDepart);
		TextView tvArrivee = (TextView) findViewById(R.id.horrairesTvGareArrivee);
		Button btn = (Button) findViewById(R.id.btnDatePicker);
		
		tvDepart.setTextColor(popupColor);
		tvArrivee.setTextColor(popupColor);
		
		if ( code == Config.DATE_ALLER_CODE ) {
			LinearLayout llDateAller = (LinearLayout) findViewById(R.id.llDateAller);
			llDateAller.setVisibility(View.GONE);
		} else {
			TextView dateAllerTv = (TextView) findViewById(R.id.dateAllerTv);
			if (date != null) {
				String tmpDate = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRANCE).format(date);
				String pretyDate = tmpDate.substring(0, 1).toUpperCase() + tmpDate.substring(1);
				dateAllerTv.setTextColor(popupColor);
				dateAllerTv.setText(pretyDate);
			}
			tvTitle.setText(getResources().getString(R.string.dateRetour));
		}

		tvDepart.setText(this.gareDepart);
		tvArrivee.setText(this.gareArrivee);
		btn.setOnClickListener( this );
		
		// Changing button drawable if we are in a PageAchat
		if (popupColor == Color.parseColor( Config.POPUP_GREEN_COLOR ) ) {
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			if (currentapiVersion >= 11) {
				btn.setBackground(getResources().getDrawable(R.drawable.white_and_green_button));
			}
						
		}
		
		btn.setText(getResources().getString(R.string.choisirCetteDate));
		final Calendar c = Calendar.getInstance();
		// Save the date if it exists
		if (this.date != null) {
			c.setTime( date );			
		}

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // initialize the date to avoid null pointer exceptions
        date = c.getTime();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 11) {
			dp.setMinDate(new Date().getTime() - 1000);
			dp.setCalendarViewShown(Config.CALENDAR_VIEW);
			tvTitle.setText(title);
		}

		dp.init(year, month, day, this);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        return true;
	    default: return super.onOptionsItemSelected(item);  
	    }
	}
	@Override
	public void onClick(View v) {
		Log.v(this.getClass().toString(), date.toString());
		finish();
	}
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("result", date);
		setResult(RESULT_OK, data);
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void onDateChanged(DatePicker v, int year, int month, int day) {
		DatePicker dp = (DatePicker) v.findViewById(R.id.datePickerPopUp);
		Calendar cal = Calendar.getInstance();
		cal.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
		date = cal.getTime();		
	}
}