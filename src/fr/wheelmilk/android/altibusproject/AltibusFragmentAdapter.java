package fr.wheelmilk.android.altibusproject;

import com.viewpagerindicator.IconPagerAdapter;

import fr.wheelmilk.android.altibusproject.support.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

class AltibusFragmentAdapter extends FragmentStatePagerAdapter implements
		IconPagerAdapter {

	private int mCount = Config.CONTENT.length;
	static SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	public AltibusFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

//	@Override
//	public int getItemPosition(Object object) {
//		Log.v(getClass().toString(), "Refreshing fragment: " + registeredFragments.indexOfValue((Fragment) object) );
//		if (object instanceof ListeBilletsFragment) {
//			Log.v(getClass().toString(), "POSITION NONE" );
//			return POSITION_NONE;
////	        ((MesBilletsFragment) object).update();
//	    }
//	    //don't return POSITION_NONE, saves new fragment creation 
////	    return super.getItemPosition(object);
//		return POSITION_UNCHANGED;
////		return registeredFragments.indexOfValue((Fragment) object);
//	}
	@Override
	public Fragment getItem(int position) {
		Log.v(getClass().toString(), "getItem Position: " + position );
		switch (position) {
		case 0: // Fragment # 0 - This will show image
			return PageAchat.init(position);
		case 1: // Fragment # 0 - This will show image
			return PageHorraires.init(position);
		case 2: // Fragment # 0 - This will show image
			return MesBilletsFragment.init(position);
		case 3: // Fragment # 0 - This will show image
			return HistoriqueFragment.init(position);
			// case 1: // Fragment # 1 - This will show image
			// return BuyTicketsFragment.init(position);
		default:// Fragment # 2-9 - Will show list
			throw new IllegalArgumentException("not this many fragments: " + position);
		}
		// return AltibusFragment.newInstance(String.valueOf(position));
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Config.CONTENT[position];
	}

	@Override
	public int getIconResId(int index) {
		return Config.ICONS[index];
	}

	// Feinte pour chopper le Fragment actif dans le Viewpager
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.v(getClass().toString(), "instantiateItem: " + position );
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	public static Fragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}
}