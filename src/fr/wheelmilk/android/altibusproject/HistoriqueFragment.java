package fr.wheelmilk.android.altibusproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.stmt.DeleteBuilder;
import fr.wheelmilk.android.altibusproject.models.BilletDB;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HistoriqueFragment extends ListeBilletsFragment implements ActionMode.Callback, OnClickListener {
	
	private Object mMode;
	private String edit;
	private String delete;
	private BilletDB billetCourant;
	
	static HistoriqueFragment init(int val) {
		HistoriqueFragment page = new HistoriqueFragment();
		Bundle args = new Bundle();
		args.putInt("val", val);
//		args.putParcelable("pager", (Parcelable) pager);
		page.setArguments(args);

		return page;
	}
	@Override
	protected void addItemToLayoutOnCreate() {
		edit 	= getResources().getString(R.string.editer);
		delete 	= getResources().getString(R.string.supprimer);
		TextView tvMes = (TextView) layoutView.findViewById(R.id.tvMes);
		TextView tvBillets = (TextView) layoutView.findViewById(R.id.tvBillets);
		tvMes.setText("Mon");
//		tvMes.s
		tvBillets.setText("Historique");
	}
	@Override
	protected void onItemClicked(View v, int position) {
		Log.v(LOG_TAG, "item Clicked :" + position);
		Log.v(LOG_TAG, "Nom :" + billets.get(position).getNom());
		Log.v(LOG_TAG, "Prenom :" + billets.get(position).getPrenom());
		Log.v(LOG_TAG, "Nb :" + billets.get(position).getNb());
		
		billetCourant = billets.get(position);
    	mMode = getSherlockActivity().startActionMode(this);
	}
	@Override
	public void startLoadFromDatabase() {
		try { // get our dao
			billetsDao = getHelper().getBilletDataDao();
			Calendar cal = Calendar.getInstance();  
			cal.setTime(new Date());
//			cal.add(Calendar.DAY_OF_YEAR, 1);
			// query for all accounts that have that password
			billets = (ArrayList<BilletDB>) billetsDao.query(
							billetsDao.queryBuilder().orderBy("da", true).where()
			         .lt("da", cal.getTime())
			         .prepare());
		} catch (SQLException e) {
			e.printStackTrace();
			billets = new ArrayList<BilletDB>();
		}
	}

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        //Used to put dark icons on light action bar
//        boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;
    	Resources mRes = this.getResources();
        menu.add(delete)
//      .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
        .setIcon(mRes.getDrawable(R.drawable.ic_action_delete_passager_light))
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(edit)
        .setIcon(mRes.getDrawable(R.drawable.ic_action_edit_passager))
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    	if ( billetCourant != null ) {
			Log.v(this.getClass().toString(), item.getTitle().toString());
			Log.v(this.getClass().toString(), "Passager: " + billetCourant.toString());
			if ( edit.equals( item.getTitle().toString() ) ) { // On edite lance l'activité d'édition de passager
				Intent i = new Intent(this.getActivity(), BilletCompostagePopUp.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Bundle b = new Bundle();
				
				b.putParcelable("billetDB", billetCourant);
				i.putExtras(b);
				startActivityForResult(i, Config.COMPOSTAGE_BILLET_RETOUR_CODE);
				getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				billetCourant = null;
			} if ( delete.equals( item.getTitle().toString() ) ) {
				SimpleAlertDialog dlg = new SimpleAlertDialog(getActivity(), "Etes vous sur de vouloir supprimer ce billet ?\n\nLa suppression sera irréverssible",
						"Supprimer", "Annuler", this);
				dlg.setTitle("Attention");
				dlg.show();
			}
    	}
        mode.finish();
        return true;
	}
	private void deleteBillet(BilletDB _billet) {
		try {
			DeleteBuilder<BilletDB, Integer> deleteBuilder = billetsDao.deleteBuilder();
			deleteBuilder.where().eq("id", _billet.getId());
			deleteBuilder.delete();
			updateView();
		} catch (SQLException e) {
			e.printStackTrace();
			Helper.grilledRare(getActivity(), "Impossible de supprimer le billet : problème avec la base de données");
		}
	}
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		
	}
	@Override
	public void onClick(DialogInterface dlg, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (billetCourant != null) deleteBillet(billetCourant);
			billetCourant = null;
		}		
	}
}