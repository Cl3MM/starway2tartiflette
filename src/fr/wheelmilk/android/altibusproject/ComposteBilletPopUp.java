package fr.wheelmilk.android.altibusproject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import fr.wheelmilk.android.altibusproject.support.Config;

public class ComposteBilletPopUp  extends SherlockActivity implements OnClickListener {

	private TextView tvNom;
	private TextView tvTypeBillet;
	private ImageView iQrCode;
	private TextView tvGareArrivee;
	private TextView tvDateAller;
	private TextView tvReference;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.billet_non_composte);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// get Extras from bundle and setup class attributes
		initialize(getIntent().getExtras());

		RelativeLayout rlValid = (RelativeLayout) findViewById(R.id.rlValidPassagers);
		rlValid.setOnClickListener(this);
		
		tvNom = (TextView) findViewById(R.id.tvNom); 
		tvTypeBillet = (TextView) findViewById(R.id.tvTypeBillet); 
		iQrCode = (ImageView) findViewById(R.id.iQrCode); 
		tvGareArrivee = (TextView) findViewById(R.id.tvGareArrivee);
		tvDateAller = (TextView) findViewById(R.id.tvDateAller);
		tvReference = (TextView) findViewById(R.id.tvReference);
		tvDateAller = (TextView) findViewById(R.id.tvDateAller);

	}
	
	public void initialize( Bundle extras ) {
		if (extras == null) {
			returnCode = Config.EXTRA_FAILURE;
			finish();
		}
		passager = extras.getParcelable("passager");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
