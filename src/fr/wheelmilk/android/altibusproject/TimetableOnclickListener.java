package fr.wheelmilk.android.altibusproject;

import android.app.Activity;
import android.view.View;

public class TimetableOnclickListener implements View.OnClickListener {

	DialogAdapterFactory parent;
	Activity activity;

	public TimetableOnclickListener(Activity activity,
			DialogAdapterFactory parent) {
		this.activity = activity;
		this.parent = parent;
	}

	@Override
	public void onClick(View v) {
		this.parent.prepareDialogFragment();
	}
}