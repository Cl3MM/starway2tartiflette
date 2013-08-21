package fr.wheelmilk.android.altibusproject;

import android.content.res.Resources;
import android.util.Log;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.Passager;

public class PassagerActionMode implements ActionMode.Callback {
	Resources mRes;
	Passager passager;
	
	public PassagerActionMode( Resources _res, Passager _passager) {
		mRes = _res;
		passager = _passager;
	}
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        //Used to put dark icons on light action bar
//        boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;

        menu.add(mRes.getString(R.string.supprimer))
//      .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
  .setIcon(mRes.getDrawable(R.drawable.ic_action_delete_passager_light))
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(mRes.getString(R.string.editer))
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
//    	Toast.makeText(mActivity, "Got click: " + item, Toast.LENGTH_SHORT).show();
//    	if (item.getTitleCondensed())
    	Log.v(this.getClass().toString(), item.getTitle().toString());
    	Log.v(this.getClass().toString(), "Passager: " + passager.toString());
    	
        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }
}