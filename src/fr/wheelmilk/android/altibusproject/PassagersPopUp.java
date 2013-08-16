package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.models.GaresDataModel;
import fr.wheelmilk.android.altibusproject.models.Passager;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class PassagersPopUp extends SherlockActivity implements OnClickListener, ActionMode.Callback {
	RelativeLayout rlAddPassager;
	RelativeLayout rlValidPassagers;
	ListView lvPassagers;
	PassagersArrayAdapter aaPassagers;
	ArrayList<Passager> passagers;
	protected ActionMode mMode;
	Passager passagerCourant;
	protected String delete;
	protected String edit;
	int counter;
	

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.passagers_popup);

		edit 	= getResources().getString(R.string.editer);
		delete 	= getResources().getString(R.string.supprimer);

		// On récupère le pref manager pour remplir le passager principal avec les valeur saisies dans "mon compte"
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String nom = prefs.getString("prefUserLastName", getResources().getString(R.string.pref_user_last_name_summary));
		String prenom = prefs.getString("prefUserFirstName", getResources().getString(R.string.pref_user_last_name_summary));
		String age = prefs.getString("prefUserAge", "prout");
		
		// TODO :
		// Faire une verif pour éviter les paramètres par défaut.
		// Ouvrir les preferencesActivity pour permettre à l'utilisateur de remplir ses infos

		Passager passagerPrincipal = new Passager(nom, prenom, age);
		passagers = new ArrayList<Passager>();
		passagers.add(passagerPrincipal);
		Log.v(this.getClass().toString(), passagerPrincipal.toString());
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		rlAddPassager = (RelativeLayout) findViewById(R.id.rlButtonAdd);
		rlAddPassager.setOnClickListener(this);

		rlAddPassager = (RelativeLayout) findViewById(R.id.rlValidPassagers);
		rlAddPassager.setOnClickListener(this);

		lvPassagers = (ListView) findViewById(R.id.lvPassagers);
 
		lvPassagers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
//				PassagerActionMode am = new PassagerActionMode( v.getResources(), passagers.get(position) );
//				mMode = startActionMode(am);
				onItemClicked(v, position);
			}
		});

		aaPassagers = new PassagersArrayAdapter(this, R.layout.passagers_list_item, passagers);
		lvPassagers.setAdapter(aaPassagers);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}
	public void displayPassagerCourant() {
		if (passagerCourant == null) {
			Log.v(this.getClass().toString(), "Passager: Aucun");
		} else {
			Log.v(this.getClass().toString(), "Passager: " + passagerCourant.toString());
		}
	}
	protected void onItemClicked(View v, int position) {
		Log.v(this.getClass().toString(), "Position: " + position);
		if (position == 0 ) { // Passager principal, on lance l'écran des paramètres
			startActivity(new Intent(this, UserPreferences.class));
		} else {
//			RelativeLayout rlRoot = (RelativeLayout) v.findViewById(R.id.rlRoot);
//			v.setSelected(true);
	    	Log.v(this.getClass().toString(), "onItemClicked 2");
			displayPassagerCourant();
			passagerCourant = passagers.get(position);
	    	passagerCourant.setPosition(position);
	    	Log.v(this.getClass().toString(), "onItemClicked 3");
			displayPassagerCourant();
	    	mMode = startActionMode(this);
//			finish();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getSupportMenuInflater().inflate(R.menu.passagers, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
		finish();
		return false;
    }
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.id.rlButtonAdd) {
			// Ajout d'un nouveau passager dans la liste
			Helper.grilledRare(this, "Merci de remplir les données du nouveau passager");
			Resources mRes = getResources();
			String defaultNom =  mRes.getString(R.string.saisirNom);
			String defaultPrenom =  mRes.getString(R.string.saisirPrenom);
			String defaultAge =  mRes.getString(R.string.saisirAge);
					
			passagers.add(new Passager( defaultNom, defaultPrenom, defaultAge ) );
			counter++;
			aaPassagers.notifyDataSetChanged();
			lvPassagers.invalidate();
			Log.v(this.getClass().toString(), "Get count: " + aaPassagers.getCount());
//			lvPassagers.invalidate();
		}
		
	}
	@Override
	protected void onStart () {
		super.onStart();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String nom = prefs.getString("prefUserLastName", getResources().getString(R.string.pref_user_last_name_summary));
		String prenom = prefs.getString("prefUserFirstName", getResources().getString(R.string.pref_user_last_name_summary));
		String age = prefs.getString("prefUserAge", "25");

		Passager passagerPrincipal = new Passager(nom, prenom, age, 0);
//		if (passagers == null) passagers = new Passager();
		passagers.set(0, passagerPrincipal);
		aaPassagers.notifyDataSetChanged();
	}
	
	//
	//
	// Action mode Callbacks goes under here
	//
	//
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
    	Log.v(this.getClass().toString(), "onActionItemClicked 1");
		displayPassagerCourant();

//    	Toast.makeText(mActivity, "Got click: " + item, Toast.LENGTH_SHORT).show();
//    	if (item.getTitleCondensed())
    	if ( passagerCourant != null ) {
    		Log.v(this.getClass().toString(), item.getTitle().toString());
    		Log.v(this.getClass().toString(), "Passager: " + passagerCourant.toString());
    		if ( edit.equals( item.getTitle().toString() ) ) { // On edite lance l'activité d'édition de passager
    			Intent i = new Intent(this, EditionPassager.class);
    			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
    			
				Bundle b = new Bundle();
				b.putParcelable("passager", passagerCourant);
				i.putExtras(b);
//    			i.putExtra("code", Config.EDITION_PASSAGERS_RETOUR_CODE);
    			startActivityForResult(i, Config.EDITION_PASSAGERS_RETOUR_CODE);
    			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    		} else { // On supprime le passager de la liste et on raffraichi
    			passagers.remove(passagerCourant.getPosition());
    			refreshListView();
    		}
    	}    	
    	Log.v(this.getClass().toString(), "onActionItemClicked 2");
		displayPassagerCourant();
    	passagerCourant = null;
        mode.finish();
        return true;
    }
    public void refreshListView() {
		aaPassagers.notifyDataSetChanged();
		lvPassagers.invalidate();
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
    	mMode = null;
    	Log.v(this.getClass().toString(), "onDestroyActionMode");
		displayPassagerCourant();
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode) {
		case Config.EXTRA_FAILURE:
			Helper.grilledRare(this, getResources().getString(R.string.errorExtras));
			break;
		default:
			switch(requestCode) {
			case Config.EDITION_PASSAGERS_RETOUR_CODE:
				updatePassagers(data);			
			}
		}
	}
	private void updatePassagers(Intent data) {
		Passager p = data.getParcelableExtra("passager");
		// TODO : comparer les passagers pour updater
		// TODO : rafraichir que le passager modifié
		passagers.set(p.getPosition(), p);
		refreshListView();
		passagerCourant = null;
	}
}
