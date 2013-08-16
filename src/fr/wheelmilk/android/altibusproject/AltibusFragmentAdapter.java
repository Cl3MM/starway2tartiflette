package fr.wheelmilk.android.altibusproject;

import com.viewpagerindicator.IconPagerAdapter;

import fr.wheelmilk.android.altibusproject.support.Config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

class AltibusFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	private int mCount = Config.CONTENT.length;
	static SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

	public AltibusFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0: // Fragment # 0 - This will show image
			return PageAchat.init(position);
		case 1: // Fragment # 0 - This will show image
			return PageHorraires.init(position);
		case 2: // Fragment # 0 - This will show image
			return AchatFragment.init(position);
			// case 1: // Fragment # 1 - This will show image
			// return BuyTicketsFragment.init(position);
		default:// Fragment # 2-9 - Will show list
			return TestFragment.newInstance(String.valueOf(position));
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
		Fragment fragment = (Fragment) super.instantiateItem(container,
				position);
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
