package fr.wheelmilk.android.altibusproject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import fr.wheelmilk.android.altibusproject.models.Billet;
import fr.wheelmilk.android.altibusproject.support.Config;
import fr.wheelmilk.android.altibusproject.support.DatabaseHelper;

public abstract class ListeBilletsFragment extends SherlockFragment {
	int fragVal;
//	FragmentPagerAdapter pager;

	private View layoutView;
	protected final String LOG_TAG = getClass().getSimpleName();
	private DatabaseHelper databaseHelper = null;
	ArrayList<Billet> billets;
	ListView lvBillets;
	protected BilletsArrayAdapter aaBillets;
	Dao<Billet, Integer> billetsDao;

	static ListeBilletsFragment init(int val) {
		new UnsupportedOperationException("Please implement this method in child classes");
		return null;
	}
	public void startLoadFromDatabase() {
		try { // get our dao
			billetsDao = getHelper().getBilletDataDao();
			Calendar cal = Calendar.getInstance();  
			cal.setTime(new Date());
//			cal.add(Calendar.DAY_OF_YEAR, 1);
			// query for all accounts that have that password
			billets = (ArrayList<Billet>) billetsDao.query(
							billetsDao.queryBuilder().orderBy("da", true).where()
			         .ge("da", cal.getTime())
			         .prepare());
		} catch (SQLException e) {
			e.printStackTrace();
			billets = new ArrayList<Billet>();
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
//		pager 	= (FragmentPagerAdapter) (getArguments() != null ? getArguments().getParcelable("pager") : null);
//	    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
//	            mMessageReceiver, new IntentFilter("billetCreated"));
		startLoadFromDatabase();
	}

//	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			Log.v(getClass().toString(), "Received intent: " + action);
//			// ... react to local broadcast message
//			updateBillets();
//		}
//	};

	protected View inflateDialog(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.mes_billets_fragment, container, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflateDialog(inflater, container);
		
		lvBillets = (ListView) layoutView.findViewById(R.id.lvBillets);
		lvBillets.setEmptyView(layoutView.findViewById(R.id.tvEmptyList));
		lvBillets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
//				PassagerActionMode am = new PassagerActionMode( v.getResources(), passagers.get(position) );
//				mMode = startActionMode(am);
				onItemClicked(v, position);
			}
		});
		
		aaBillets = new BilletsArrayAdapter(getActivity(), R.layout.passagers_list_item, billets);
		lvBillets.setAdapter(aaBillets);
		addItemToLayoutOnCreate();
		return layoutView;
	}

	protected void addItemToLayoutOnCreate() {
		new UnsupportedOperationException("Please Override this method in child classes");
	}
	protected void onItemClicked(View v, int position) {
		Log.v(LOG_TAG, "item Clicked :" + position);
		Log.v(LOG_TAG, "Nom :" + billets.get(position).getNom());
		Log.v(LOG_TAG, "Prenom :" + billets.get(position).getPrenom());
		Log.v(LOG_TAG, "Nb :" + billets.get(position).getNb());
		Intent i = new Intent(this.getActivity(), DatePickerPopUp.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//		i.putExtra("title", getResources().getString(R.string.dateDepart));
//		startActivityForResult(i, code);
//		this.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	protected DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
		}
		return databaseHelper;
	}

	@Override
	public void onResume() {
		super.onResume();
		startLoadFromDatabase();
		aaBillets.notifyDataSetChanged();
		lvBillets.invalidate();
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
}
