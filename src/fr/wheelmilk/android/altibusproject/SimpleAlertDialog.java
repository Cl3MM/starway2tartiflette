package fr.wheelmilk.android.altibusproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class SimpleAlertDialog extends AlertDialog {
	private String mMessage;
	AlertDialog mDialog;
	
	protected SimpleAlertDialog(Context context, String _message) {
		super(context);
		mMessage = _message;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   dismiss();
		           }
		       });
//		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//		           public void onClick(DialogInterface dialog, int id) {
//		               // User cancelled the dialog
//		           }
//		       });
		builder.setMessage(mMessage);
		builder.setTitle(R.string.attentionTT);
		mDialog = builder.create();
	}

	public void show() {
		mDialog.show();
	}
	public void dismiss() {
		mDialog.dismiss();
	}
	
//    static SimpleAlertDialog newInstance(String _message) {
//    	SimpleAlertDialog dlg = new SimpleAlertDialog();
//
//        // Supply num input as an argument.
//        Bundle args = new Bundle();
//        args.putString("message", _message);
//        dlg.setArguments(args);
//
//        return dlg;
//    }
    
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//    	super.onCreate(savedInstanceState);
//    	mMessage = getArguments().getString("message");
//
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(mMessage)
//               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                   public void onClick(DialogInterface dialog, int id) {
//                       dismiss();
//                   }
//               });
////               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
////                   public void onClick(DialogInterface dialog, int id) {
////                       // User cancelled the dialog
////                   }
////               });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }
}