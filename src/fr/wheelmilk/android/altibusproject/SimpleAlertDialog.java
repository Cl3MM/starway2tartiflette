package fr.wheelmilk.android.altibusproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

public class SimpleAlertDialog extends AlertDialog {
	private String mMessage;
	AlertDialog mDialog;
	private boolean isVisible = false;
	private String tag;
	
	protected SimpleAlertDialog(Context context, String _message) {
		super(context);
		mMessage = _message;
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   dismiss();
		           }
		       });
		builder.setMessage(mMessage);
		builder.setTitle(R.string.attentionTT);
		mDialog = builder.create();
	}
	protected SimpleAlertDialog(Context context, String _message, String btnOk, String btnCancel, DialogInterface.OnClickListener mListener) {
		super(context);
		mMessage = _message;
		AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar) );

		builder.setPositiveButton(btnOk, mListener);
		if (btnCancel != null ) {
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		               dismiss();
		           }
		       });
		}
		builder.setMessage(mMessage);
		builder.setTitle(R.string.attentionTT);
		mDialog = builder.create();
	}

	public void setTitle(String title) {
		mDialog.setTitle(title);
	}
	@Override
	public void show() {
		mDialog.show();
		isVisible = true;
	}
	public void setCancelableProperly(boolean b) {
		mDialog.setCancelable(b);		
	}
	public boolean isVisible() {
		return isVisible;
	}
	@Override
	public void dismiss() {
		mDialog.dismiss();
		isVisible = false;
	}
	public void setTag(String _tag) {
		tag = _tag;
	}
	public String getTag() {
		return tag;
	}
}