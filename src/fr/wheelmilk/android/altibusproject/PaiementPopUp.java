package fr.wheelmilk.android.altibusproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import fr.wheelmilk.android.altibusproject.support.Config;

public class PaiementPopUp extends SherlockActivity implements OnClickListener, DialogInterface.OnClickListener {
	int returnCode;
	SimpleAlertDialog mDialog;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edition_passagers);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// get Extras from bundle and setup class attributes
		initialize(getIntent().getExtras());
		RelativeLayout rlValid = (RelativeLayout) findViewById(R.id.rlValidPassagers);
		rlValid.setOnClickListener(this);

		mDialog = new SimpleAlertDialog(this, "Coucou !", getString(R.string.acceptAndPay), getString(R.string.cancel), this);
		mDialog.show();
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
		returnCode = RESULT_CANCELED;	
		finish();
		return false;
    }
	public void initialize( Bundle extras ) {
		if (extras == null) {
			returnCode = Config.EXTRA_FAILURE;
			finish();
		}
//		passager = extras.getParcelable("passager");
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	protected void onPreFinish() {
		Intent data = new Intent();
		Bundle b = new Bundle();
		
//		b.putParcelable("passager", passager);
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
	public void onClick(DialogInterface dialog, int which) {
		mDialog.dismiss();
		if (which == DialogInterface.BUTTON_POSITIVE) {
			
		}
	}
}
