package fr.wheelmilk.android.altibusproject;

import fr.wheelmilk.android.altibusproject.layout.AutoResizeTextView;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class SuperDialogAdapter {

	Activity activity;
	View.OnClickListener onClickListener = null;
	View timetableFragmentTextField;
	private String onClickValue;

	public SuperDialogAdapter(Activity activity) {
		this.activity = activity;
		this.onClickListener = this.createOnClickListener();
	}

	public OnClickListener onclicklistener() {
		return this.onClickListener;
	}

	public String onClickValue() {
		return this.onClickValue;
	}

	public void updateTextViewString(String result) {
		this.onClickValue = result;
		Log.v("Altibus updateTextViewString: ",
				(String) ((AutoResizeTextView) this.timetableFragmentTextField)
						.getText());

		((AutoResizeTextView) this.timetableFragmentTextField).setText(result);
		Log.v("Altibus updateTextViewString: ", this.onClickValue);
	}
	private OnClickListener createOnClickListener() {
		new UnsupportedOperationException("Not implemented yet");
		return null;
	}
}
