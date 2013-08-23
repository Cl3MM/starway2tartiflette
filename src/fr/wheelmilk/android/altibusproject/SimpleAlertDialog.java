package fr.wheelmilk.android.altibusproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleAlertDialog extends AlertDialog {
	private String mMessage;
	AlertDialog mDialog;
	
	protected SimpleAlertDialog(Context context, String _message) {
		super(context);
		mMessage = _message;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

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
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setPositiveButton(btnOk, mListener);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               dismiss();
		           }
		       });
		builder.setMessage(mMessage);
		builder.setTitle(R.string.attentionTT);
		mDialog = builder.create();
	}
	@Override
	public void show() {
		mDialog.show();
	}
	@Override
	public void dismiss() {
		mDialog.dismiss();
	}
}