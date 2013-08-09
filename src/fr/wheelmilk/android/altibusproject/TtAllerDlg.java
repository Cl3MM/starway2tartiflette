package fr.wheelmilk.android.altibusproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class TtAllerDlg<T extends AbstractGenericFragment> {

	Context mContext;
	T ttf;
	TimetableFragment ttf2;
	Dialog dialog;
	String title;
	String choix1;
	String choix2;
	String[] choix;

	public TtAllerDlg(Context mContext, final T ttf) {

		title = mContext.getResources().getString(R.string.gareAcoteDlgTitle);
		choix1 = mContext.getResources().getString(R.string.voirGaresACote);
		choix2 = mContext.getResources().getString(R.string.voirToutesGares);
		choix = new String[] { choix1, choix2 };
		this.ttf = ttf;
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setItems(choix, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				ttf.onGAMethodSelected(position);
			}
		});
		this.dialog = builder.create();
	}

	
	public void show() {
		this.dialog.show();
	}

	// public TtAllerDlg(Context mContext, final TimetableFragment ttf) {
	//
	// title = mContext.getResources().getString(R.string.gareAcoteDlgTitle);
	// choix1 = mContext.getResources().getString(R.string.voirGaresACote);
	// choix2 = mContext.getResources().getString(R.string.voirToutesGares);
	// choix = new String[] { choix1, choix2 };
	// this.ttf2 = ttf;
	// AlertDialog.Builder builder = new AlertDialog.Builder(new
	// ContextThemeWrapper(mContext,
	// android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
	// builder.setTitle(title);
	// builder.setItems(choix, new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int position) {
	// ttf.onGAMethodSelected(position);
	// }
	// });
	// this.dialog = builder.create();
	// }
	// public TtAllerDlg(Context mContext, final TimetableFragment ttf) {
	//
	// title = mContext.getResources().getString(R.string.gareAcoteDlgTitle);
	// choix1 = mContext.getResources().getString(R.string.voirGaresACote);
	// choix2 = mContext.getResources().getString(R.string.voirToutesGares);
	// choix = new String[] { choix1, choix2 };
	// this.ttf = ttf;
	// AlertDialog.Builder builder = new AlertDialog.Builder(new
	// ContextThemeWrapper(mContext,
	// android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
	// builder.setTitle(title);
	// builder.setItems(choix, new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int position) {
	// ttf.onGAMethodSelected(position);
	// }
	// });
	// this.dialog = builder.create();
	// }

}