package fr.wheelmilk.android.altibusproject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.Billet;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HistoriqueFragment extends ListeBilletsFragment implements ActionMode.Callback {
	
	private Object mMode;
	private String edit;
	private String delete;
	private Billet billetCourant;
	
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
	}
	
	@Override
	public void startLoadFromDatabase() {
		try { // get our dao
			billetsDao = getHelper().getBilletDataDao();
			Calendar cal = Calendar.getInstance();  
			cal.setTime(new Date());
//			cal.add(Calendar.DAY_OF_YEAR, 1);
			// query for all accounts that have that password
			billets = (ArrayList<Billet>) billetsDao.query(
							billetsDao.queryBuilder().orderBy("da", true).where()
			         .lt("da", cal.getTime())
			         .prepare());
		} catch (SQLException e) {
			e.printStackTrace();
			billets = new ArrayList<Billet>();
		}
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
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		
	}
}