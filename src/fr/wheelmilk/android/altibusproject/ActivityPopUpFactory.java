package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;
import fr.wheelmilk.android.altibusproject.models.GaresDataModel;
import fr.wheelmilk.android.altibusproject.support.Config;

public abstract class ActivityPopUpFactory extends SherlockActivity implements OnWebserviceListenner {
	protected LinearLayout llHeaderProgress;
	protected String title;
	protected int popupColor;
	protected String result = null;
	protected LinearLayout llPopupContent;
	protected ListView listeDesGares;
	protected PopUpArrayAdapter arrayAdapter;
	protected ArrayList<String> itemListNames;
	protected int returnCode;
	protected HashMap<String, GaresDataModel> altibusData;

	protected void startAsyncHTTPRequest() {
		new UnsupportedOperationException("Please implement this method in subclasses");
	}

	public void initialize( Bundle extras ) {
		if (extras == null) {
			returnCode = Config.EXTRA_FAILURE;
			finish();
		}
		title = extras.getString("title");
		String color = extras.getString("popupColor");
		popupColor = Color.parseColor( color );
	}
	protected void setSuccessfulResult(int position) {
//		result 		= arrayAdapter.items.get(position);
		result 		= arrayAdapter.getItem(position);
		returnCode 	= RESULT_OK; 
	}
	protected void setPopUpLayout() {
		setContentView(R.layout.popup_activity);
	}
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setPopUpLayout();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// get Extras from bundle and setup class attributes
		initialize(getIntent().getExtras());
		// on lance la requête au webservice
		startAsyncHTTPRequest();

		llHeaderProgress = (LinearLayout) findViewById(R.id.llHeaderProgress);
		llPopupContent = (LinearLayout) findViewById(R.id.llPopupContent);
		// on affiche la progressbar en attendant la réponse du serveur
		showProgressBar();

		TextView dlgTitle = (TextView) findViewById(R.id.title);
		dlgTitle.setText(title);

		listeDesGares = (ListView) findViewById(R.id.listDesGares);
//		LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.fadein));
//	    lac.setDelay(0.02f);
//	    listeDesGares.setLayoutAnimation(lac);  
		listeDesGares.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				setSuccessfulResult(position);
				finish();
			}
		});
		addTextChangedListener();
		addLayoutCustomizations();
	}
	// Permet l'ajout d'éléments au layout dans la classe fille
	protected void addLayoutCustomizations() {
		new UnsupportedOperationException("Please implement this method in child classes");
	}

	protected void addTextChangedListener() {
		EditText filterGareAller = (EditText) findViewById(R.id.filterGaresAller);
		// listener qui met à jour la liste quand l'utilisateur tape du texte
		filterGareAller.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				arrayAdapter.getFilter().filter(cs.toString().replace("\n", ""));
			}
			@Override
			public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) { }
			@Override
			public void afterTextChanged(Editable arg0) { }
		});
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
		finish();
		return false;
    }
	protected void onPreFinish() {
		Intent data = new Intent();
		Bundle b = new Bundle();
		b.putParcelable("tag", altibusData.get(result));
		data.putExtra("result", result);
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

	private PopUpArrayAdapter buildArrayAdpater( ArrayList<String> _arrayString) {
		PopUpArrayAdapter _arrayAdapter = new PopUpArrayAdapter(this,
		R.layout.popup_list_item, _arrayString);
		return _arrayAdapter;
	}

	protected void populateListView() {
		arrayAdapter = buildArrayAdpater(itemListNames);
		ListView listeDesGares = (ListView) findViewById(R.id.listDesGares);
		listeDesGares.setAdapter(arrayAdapter);
		listeDesGares.setEmptyView(findViewById(R.id.tvEmptyList));
	}

	protected void showProgressBar() {
		llHeaderProgress.setVisibility(View.VISIBLE);
		llPopupContent.setVisibility(View.GONE);
	}

	protected void dismissProgressBar() {
		llHeaderProgress.setVisibility(View.GONE);
		llPopupContent.setVisibility(View.VISIBLE);
	}

	@Override
	public void onWebserviceSuccess(String xmlString) {

		AltibusDataModel altibusDataModel = new AltibusSerializer(AltibusDataModel.class).serializeXml(xmlString);

		if (altibusDataModel != null) {
			// Le xml est sérialisé
			itemListNames = altibusDataModel.itemNames();
			altibusData = altibusDataModel.objects();
			populateListView();
			dismissProgressBar();
		} else if ( this instanceof HorrairesPopUpActivity) {
			itemListNames = new ArrayList<String>();
			altibusData = new HashMap<String, GaresDataModel>(); 
			// HorraireDialog = Pas d'horrraire ce jour
			returnCode = Config.PAS_D_HORRAIRES;
			populateListView();
			dismissProgressBar();
		} else {
			// Problème de sérialisation
			Log.v(this.getClass().toString(), "Serializer faillure :(");
			returnCode = Config.SERIALIZATION_FAILURE;
			finish();
		}
	}

	@Override
	public void onWebserviceFailure() {
		returnCode = Config.WEBSERVICE_FAILLURE;
		finish();
	}
}