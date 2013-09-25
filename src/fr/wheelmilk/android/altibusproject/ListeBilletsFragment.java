package fr.wheelmilk.android.altibusproject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import fr.wheelmilk.android.altibusproject.models.BilletDB;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.DatabaseHelper;
import fr.wheelmilk.android.altibusproject.support.Helper;

public abstract class ListeBilletsFragment extends SherlockFragment implements OnClickListener { //implements OnItemClickListener {
	int fragVal;
//	FragmentPagerAdapter pager;

	boolean firstRun = true;
	protected View layoutView;
	protected final String LOG_TAG = getClass().getSimpleName();
	private DatabaseHelper databaseHelper = null;
	ArrayList<BilletDB> billets;
	ListView lvBillets;
	protected BilletsArrayAdapter aaBillets;
	Dao<BilletDB, Integer> billetsDao;

	static ListeBilletsFragment init(int val) {
		new UnsupportedOperationException("Please implement this method in child classes");
		return null;
	}
	public void startLoadFromDatabase() {
		try { // get our dao
			billetsDao = getHelper().getBilletDataDao();
			Calendar cal = Calendar.getInstance();  
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, 1);
			// query for all accounts that have that password
			billets = (ArrayList<BilletDB>) billetsDao.query(
							billetsDao.queryBuilder().orderBy("da", true).where()
			         .ge("da", cal.getTime())
			         .prepare());
		} catch (SQLException e) {
			e.printStackTrace();
			billets = new ArrayList<BilletDB>();
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
		startLoadFromDatabase();
	}

	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.mes_billets_fragment, container, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflateDialog(inflater, container);
		
//		TextView tvBilletsHeader = (TextView) layoutView.findViewById(R.id.tvBilletsHeader);
		lvBillets = (ListView) layoutView.findViewById(R.id.lvBillets);
		lvBillets.setEmptyView(layoutView.findViewById(R.id.tvEmptyList));
//		lvBillets.setItemsCanFocus(false);
		lvBillets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				onItemClicked(v, position);
				Log.v(LOG_TAG, "item Clicked :" + position);
			}
		});
		
		aaBillets = new BilletsArrayAdapter(getActivity(), R.layout.passagers_list_item, billets, this);
		lvBillets.setAdapter(aaBillets);
		addItemToLayoutOnCreate();
		return layoutView;
	}
//	@Override
//	public void onItemClick(AdapterView<?> av, View v, int position, long j) {
//		Log.v(LOG_TAG, "item Clicked :" + position);
//		onItemClicked(v, position);
//	}
	
	protected void addItemToLayoutOnCreate() {
		new UnsupportedOperationException("Please Override this method in child classes");
	}
	protected void onItemClicked(View v, int position) {
		Intent i = new Intent(this.getActivity(), BilletCompostagePopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		Bundle b = new Bundle();
		
		b.putParcelable("billetDB", billets.get(position));
		i.putExtras(b);
		startActivityForResult(i, Config.COMPOSTAGE_BILLET_RETOUR_CODE);
		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		final int canceled = Activity.RESULT_CANCELED;
		switch(resultCode) {
		case Config.EXTRA_FAILURE:
			Helper.grilledRare(getActivity(), getResources().getString(R.string.errorExtras));
			break;
		case canceled:
			break;
		default:
			switch(requestCode) {
			case Config.COMPOSTAGE_BILLET_RETOUR_CODE:
				updateView();
			}
		}
	}
	protected DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
		}
		return databaseHelper;
	}

	public void updateView() {
		startLoadFromDatabase();
		if (billets.size() > 0) {
			aaBillets.clear();
			for (BilletDB billet : billets) {
				aaBillets.add(billet);
			}
//			aaBillets.addAll(billets);
			aaBillets.notifyDataSetChanged();
			lvBillets.invalidate();
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if (firstRun) {
			firstRun = false;
		} else {
			updateView();
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		Log.v(getClass().toString(), "onPause");
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
