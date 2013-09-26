package fr.wheelmilk.android.altibusproject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Locale;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.antidots.android.altibus.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import fr.wheelmilk.android.altibusproject.models.BilletDB;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.DatabaseHelper;
import fr.wheelmilk.android.altibusproject.support.Helper;

public class BilletCompostagePopUp extends SherlockActivity implements OnClickListener, DialogInterface.OnClickListener {

	protected DatabaseHelper databaseHelper = null;
	protected int colorBlack = 0xFF000000;
	Dao<BilletDB, Integer> billetsDao;

	BilletDB billet;
	protected TextView tvNom;
	protected TextView tvTypeBillet;
	protected ImageView iQrCode;
	protected ImageView iClickableInfo;
	protected TextView tvGareArrivee;
	protected TextView tvGareDepart;
	protected TextView tvDateAller;
	protected TextView tvReference;
	RelativeLayout rlBtnComposte;
	protected int returnCode;
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.billet_non_composte);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// get Extras from bundle and setup class attributes
		initialize(getIntent().getExtras());

		rlBtnComposte = (RelativeLayout) findViewById(R.id.rlBtnComposte);
		
		tvNom = (TextView) findViewById(R.id.tvNom); 
		tvTypeBillet = (TextView) findViewById(R.id.tvTypeBillet); 
		iQrCode = (ImageView) findViewById(R.id.iQrCode); 
		iClickableInfo = (ImageView) findViewById(R.id.iClickableInfo);
		iClickableInfo.setOnClickListener(this);
		tvGareArrivee = (TextView) findViewById(R.id.tvGareArrivee);
		tvGareDepart = (TextView) findViewById(R.id.tvGareDepart);
		tvDateAller = (TextView) findViewById(R.id.tvDateAller);
		tvReference = (TextView) findViewById(R.id.tvReference);
		TextView tvNonValide = (TextView) findViewById(R.id.tvNonValide);

		tvNom.setText(billet.getFullName());
		tvTypeBillet.setText(billet.getTypeBillet());
		tvGareDepart.setText(billet.getGareDepart());
		tvDateAller.setText(billet.getDateDepartBillet());
		tvReference.setText(billet.getRefForBillet());
		tvGareArrivee.setText(billet.getGareArriveeBillet());
		RotateAnimation rotate= (RotateAnimation)AnimationUtils.loadAnimation(this,R.anim.rotation_45);
		tvNonValide.setAnimation(rotate);
		
		tvTypeBillet.setSelected(true);
//		tvNom.setMovementMethod(new ScrollingMovementMethod());
//		tvTypeBillet.setMovementMethod(new ScrollingMovementMethod());
//		tvGareDepart.setMovementMethod(new ScrollingMovementMethod());
		
		if (billet.isPerime()) {
			showBilletPerime();
		} else if (!billet.isValide()) {
			rlBtnComposte.setOnClickListener(this);
			tvNonValide.setText("Non valide");
		} else {
			showValidationLayout();
		}
		
		Bitmap bitmap = generateQRCode();
            
        if(bitmap != null) {
		  iQrCode.setImageBitmap(bitmap);
		} else {
			  iQrCode.setBackground(getResources().getDrawable(R.drawable.ic_action_cadenas));
		}
	}
	private void showBilletPerime() {
		findViewById(R.id.rlToHide1).setVisibility(View.GONE);
		findViewById(R.id.tvToHide).setVisibility(View.GONE);
		findViewById(R.id.tvJmJv).setVisibility(View.GONE);

		colorBlack = 0xFF808285;
		rlBtnComposte.setBackgroundColor(getResources().getColor(R.color.greyBilletInvalide));
		findViewById(R.id.tvNonValide).setVisibility(View.VISIBLE);
		findViewById(R.id.rlBilletComposte).setVisibility(View.GONE);
		findViewById(R.id.rlBilletPerime).setVisibility(View.VISIBLE);
		TextView tvValideLe = (TextView) findViewById(R.id.tvValideLe2);
		tvValideLe.setText(billet.getDateValidation());

		TextView tvNonValide = (TextView) findViewById(R.id.tvNonValide);
		tvNonValide.setText("Non valide");

		int grey = getResources().getColor(R.color.greyBilletInvalide);
		tvNonValide.setTextColor(grey);
		tvNom.setTextColor(grey);
		tvGareDepart.setTextColor(grey);
		tvReference.setTextColor(grey);
		tvGareArrivee.setTextColor(grey);	
	}
	protected void setAdditionalLayout() {
		int grey = getResources().getColor(R.color.greyBilletInvalide);

		tvNom.setTextColor(grey);
		tvGareDepart.setTextColor(grey);
		tvReference.setTextColor(grey);
		tvGareArrivee.setTextColor(grey);
//		findViewById(R.id.rlBilletComposte).setVisibility(View.VISIBLE);
	}
	protected Bitmap generateQRCode() {
		QRCodeWriter writer = new QRCodeWriter();
        Bitmap bitmap;
		try
        {
            EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = writer.encode("http://www.lys-altibus.com", BarcodeFormat.QR_CODE, 150, 150, hint);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++)
            {
                int offset = y * width;
                for (int x = 0; x < width; x++)
                {
                     pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : 0xFFFFFFFF;
                }
            }

           bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        } catch (WriterException e) {
			e.printStackTrace();
			bitmap = null;
        }
		return bitmap;
	}
	protected void showValidationLayout() {
		findViewById(R.id.rlToHide1).setVisibility(View.GONE);
		findViewById(R.id.tvToHide).setVisibility(View.GONE);
		findViewById(R.id.tvJmJv).setVisibility(View.GONE);
		rlBtnComposte.setBackgroundColor(getResources().getColor(R.color.greenBillet));
		findViewById(R.id.rlBilletPerime).setVisibility(View.GONE);
		TextView tvNonValide = (TextView) findViewById(R.id.tvNonValide);
		tvNonValide.setText("");
		tvNonValide.setBackgroundColor(0X00000000);
		tvNonValide.setVisibility(View.GONE);

        findViewById(R.id.rlBilletComposte).setVisibility(View.VISIBLE);

		ImageView superValidImage = (ImageView) findViewById(R.id.superValidImage);

        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        superValidImage.startAnimation(animation);

		TextView tvValideLe = (TextView) findViewById(R.id.tvValideLe);
		tvValideLe.setText(billet.getDateValidation());
	}

	public void initialize( Bundle extras ) {
		if (extras == null) {
			returnCode = Config.EXTRA_FAILURE;
			finish();
		}
		billet = extras.getParcelable("billetDB");
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlBtnComposte) { // Compostage du billet
			SimpleAlertDialog dlg = new SimpleAlertDialog(this, getString(R.string.valideDevantConducteur), getString(R.string.composter), getString(R.string.cancel), this);
			dlg.setTitle("Composter le billet ?");
			dlg.show();
		} else if (v.getId() == R.id.iClickableInfo) {
			SimpleAlertDialog dlg = new SimpleAlertDialog(this, getString(R.string.cgv));
			dlg.setTitle("Conditions générales");
			dlg.show();			
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		try {
			billetsDao = getHelper().getBilletDataDao();
		} catch (SQLException e) {
			e.printStackTrace();
			billetsDao = null;
		}
	}
	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
		returnCode = RESULT_CANCELED;	
		finish();
		return false;
    }
	@Override
	public void onClick(DialogInterface dlg, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			Calendar cal = Calendar.getInstance();
			cal.setTime( billet.getDateAller() );
			int jourAller = cal.get(Calendar.DAY_OF_YEAR);
			
			cal.setTime(new Date());
			int today = cal.get(Calendar.DAY_OF_YEAR);
			
			if (jourAller == today) {
				rlBtnComposte.setOnClickListener(null);
				UpdateBuilder<BilletDB, Integer> updateBuilder = billetsDao.updateBuilder();
				Date dateValidation = new Date(cal.getTimeInMillis());
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
				StringBuilder strDateValidation = new StringBuilder( "Le " );
				strDateValidation.append(dateFormatter.format(dateValidation));
				
				try {
					updateBuilder.updateColumnValue("dateValidation", dateValidation);
					updateBuilder.updateColumnValue("valide", true);
					updateBuilder.where().eq("id", billet.getId());
					updateBuilder.update();
				} catch (SQLException e) {
					e.printStackTrace();
					Helper.grilledRare(this, "Impossible de valider le billet : problème avec la base de données");
				}
				billet.setDateValidation(dateValidation);
				showValidationLayout();
			} else {
				Helper.grilledWellDone(	this, "Le billet peut être composté seulement le jour du voyage");
			}
		}
		dlg.dismiss();
	}
}
