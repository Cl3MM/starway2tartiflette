package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.antidots.android.altibus.R;

import fr.wheelmilk.android.altibusproject.models.GaresArrivee;
import fr.wheelmilk.android.altibusproject.models.GaresDepart;
import fr.wheelmilk.android.altibusproject.models.HorrairesAller;
import fr.wheelmilk.android.altibusproject.models.HorrairesRetour;
import fr.wheelmilk.android.altibusproject.models.Passager;
import fr.wheelmilk.android.altibusproject.models.Passagers;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class PassagersPopUp extends SherlockActivity implements OnClickListener, ActionMode.Callback {
	RelativeLayout rlAddPassager;
	RelativeLayout rlValidPassagers;
	ListView lvPassagers;
	PassagersArrayAdapter aaPassagers;
	Passagers passagers;
	protected ActionMode mMode;
	Passager passagerCourant;
	protected String delete;
	protected String edit;
	private UserPref preferences;
	private int returnCode;
	boolean displayFillUpMessage = true;
	
	public void initialize( Bundle extras ) {
		if (extras == null) { // Pas d'extras : nouveau passagers !
			Passager passagerPrincipal = new Passager(preferences.nom, preferences.prenom, preferences.age);
			passagerPrincipal.createPassagerPrincipal(preferences.adresse, preferences.adresse2, preferences.codePostal, 
					preferences.ville, preferences.pays, preferences.telephone, preferences.email);
			passagers = new Passagers();
			passagers.add(passagerPrincipal);
			 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), passagerPrincipal.toString());
		 } else {
			passagers = extras.getParcelable("passagers");
		 }
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		// On récupère le pref manager pour remplir le passager principal avec les valeur saisies dans "mon compte"
		preferences = getUserPreferences();

		initialize(getIntent().getExtras());
		setContentView(R.layout.passagers_popup);

		if( bundle != null ) {
			 if (Config.DEBUG == 1) Log.v(getClass().toString(), "Loading instance...");
			passagers = bundle.getParcelable("passagers");
			 if (Config.DEBUG == 1) Log.v(getClass().toString(), "Nombre de passagers: " + passagers.size());
		}

		edit 	= getResources().getString(R.string.editer);
		delete 	= getResources().getString(R.string.supprimer);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		rlAddPassager = (RelativeLayout) findViewById(R.id.rlButtonAdd);
		rlAddPassager.setOnClickListener(this);

		rlValidPassagers = (RelativeLayout) findViewById(R.id.rlValidPassagers);
		rlValidPassagers.setOnClickListener(this);

		lvPassagers = (ListView) findViewById(R.id.lvPassagers);
 
		lvPassagers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				onItemClicked(v, position);
			}
		});

		aaPassagers = new PassagersArrayAdapter(this, R.layout.passagers_list_item, passagers);
		lvPassagers.setAdapter(aaPassagers);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 11) {
			// Change la couleur du texte de l'action bar pour annuler le theme light
			int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			TextView Tv = (TextView)findViewById(titleId);

			Tv.setTextColor(getResources().getColor(R.color.table_background));			
		}
	}
	public void displayPassagerCourant() {
		if (passagerCourant == null) {
			 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "Passager: Aucun");
		} else {
			 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "Passager: " + passagerCourant.toString());
		}
	}
	protected void onItemClicked(View v, int position) {
		 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "Position: " + position);
		if (position == 0 ) { // Passager principal, on lance l'écran des paramètres
			startActivity(new Intent(this, UserPreferences.class));
		} else {
			displayPassagerCourant();
			passagerCourant = passagers.get(position);
	    	passagerCourant.setPosition(position);
			displayPassagerCourant();

			AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar) );
			builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
		          }
		       });

			builder.setItems(R.array.editPassager, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
            	   dialog.dismiss();
	               passagerSelectAction(which);
	           }
		    });
			builder.setTitle(R.string.passagerDlgEditionTitle);
			AlertDialog dlg = builder.create();
			dlg.show();
		}
	}

	protected void passagerSelectAction(int which) {
		 if (Config.DEBUG == 1) Log.v(getClass().toString(), "Position: " + String.valueOf(which));
    	displayPassagerCourant();
    	if ( passagerCourant != null ) {
    		if ( which == 0 ) { // On edite lance l'activité d'édition de passager
    			Intent i = new Intent(this, EditionPassager.class);
    			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
				Bundle b = new Bundle();
				b.putParcelable("passager", passagerCourant);
				i.putExtras(b);
    			startActivityForResult(i, Config.EDITION_PASSAGERS_RETOUR_CODE);
    			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    		} else { // On supprime le passager de la liste et on raffraichi
    			passagers.remove(passagerCourant.getPosition());
    			refreshListView();
    		}
    	}
    	passagerCourant = null;
	}

	private class UserPref {
		String nom;
		String prenom;
		String age;
		String adresse;
		String adresse2;
		String ville;
		String codePostal;
		String pays;
		String telephone;
		String email;
	}
	private UserPref getUserPreferences() {
		UserPref p = new UserPref();
		// On récupère le pref manager pour remplir le passager principal avec les valeur saisies dans "mon compte"
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		p.nom = prefs.getString("prefUserLastName", "");
		p.prenom = prefs.getString("prefUserFirstName", "");
		p.age = prefs.getString("prefUserAge", "-1");

		p.adresse = prefs.getString("prefUserAddress", getResources().getString(R.string.pref_user_adresse));
		p.adresse2 = prefs.getString("prefUserAddress2", getResources().getString(R.string.pref_user_adresse2));
		p.ville = prefs.getString("prefUserVille", getResources().getString(R.string.pref_user_ville));
		p.codePostal = prefs.getString("prefUserCodePostal", getResources().getString(R.string.pref_user_code_postal));
		p.pays = prefs.getString("prefUserCountry", getResources().getString(R.string.pref_user_countries));
		p.telephone = prefs.getString("prefUserTelephone", getResources().getString(R.string.pref_user_telephone));
		p.email = prefs.getString("prefUserEmail", getResources().getString(R.string.pref_user_email));

		return p;
	}
	private UserPref getDefaultPreferences() {
		UserPref p = new UserPref();
		p.nom = getResources().getString(R.string.saisirNom);
		p.prenom = getResources().getString(R.string.pref_user_last_name_summary);
		p.age = "-1";

		return p;
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
			int size = passagers.size();
			if (size < 12) { // Pas plus de 12 passagers
				// Ajout d'un nouveau passager dans la liste
				if (displayFillUpMessage) {
					Helper.grilledWellDone(this, getResources().getString(R.string.displayFillUpMessage));
					displayFillUpMessage = false;
				}
				passagers.add(new Passager( getDefaultPreferences().nom, getDefaultPreferences().prenom, getDefaultPreferences().age) );
				refreshListView();
			} else {
				Helper.grilledRare(this, getResources().getString(R.string.pasPlusdeDouze));
			}
		} // Validation des passagers et retour à l'activité précédente 
		  else if (vid == R.id.rlValidPassagers) { 
			if (passagers.isValid(getResources())) { // Si tous les infos passagers ont été rempli correctement
				 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "onClick: Passagers valides");
				returnCode = RESULT_OK;
				finish();
			} else {
				SimpleAlertDialog dialog = new SimpleAlertDialog(this, passagers.getErrorMessages());
				dialog.show();
			}
		}	
	}
	
	protected void onPreFinish() {
		Intent data = new Intent();
		Bundle b = new Bundle();
		
		b.putParcelable("passagers", passagers);
		data.putExtras(b);
		setResult(returnCode, data);
	}
	@Override
	public void finish() {
		if (returnCode == RESULT_OK) onPreFinish();
		else setResult(returnCode);
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		preferences = getUserPreferences();
		Passager passagerPrincipal = new Passager(preferences.nom, preferences.prenom, preferences.age);
		passagerPrincipal.createPassagerPrincipal(preferences.adresse, preferences.adresse2, preferences.codePostal, 
				preferences.ville, preferences.pays, preferences.telephone, preferences.email);
		passagers.set(0, passagerPrincipal);
		refreshListView();
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
		getSupportMenuInflater().inflate(R.menu.passagers, menu);
    	MenuItem item = menu.findItem(R.id.item_text);
    	TextView itemText = (TextView) item.getActionView().findViewById(R.id.text);
    	if (passagerCourant != null) {
    		int position = passagerCourant.getPosition() + 1;
    		StringBuilder s = new StringBuilder("Passager ");
    		s.append( String.valueOf(position));
    		s.append(" selectionné");
        	itemText.setText(s.toString());
    	} else itemText.setText("");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    	displayPassagerCourant();
    	if ( passagerCourant != null ) {
    		 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), item.getTitle().toString());
    		 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "Passager: " + passagerCourant.toString());
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
    	 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "onActionItemClicked 2");
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
    	 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "onDestroyActionMode");
		refreshListView();
		displayPassagerCourant();
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode) {
		case Config.EXTRA_FAILURE:
			Helper.grilledRare(this, getResources().getString(R.string.errorExtras));
			break;
		case RESULT_CANCELED:
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
		passagers.set(p.getPosition(), p);
		refreshListView();
		passagerCourant = null;
	}
	@Override
	public void onSaveInstanceState(Bundle data) {
	   super.onSaveInstanceState(data);
	   data.putParcelable("passagers", passagers);
	 if (Config.DEBUG == 1)    Log.v(getClass().toString(),"Saving Passagers instance");
	}
}
