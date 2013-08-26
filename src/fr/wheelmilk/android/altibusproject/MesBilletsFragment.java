package fr.wheelmilk.android.altibusproject;

import android.os.Bundle;

public class MesBilletsFragment extends ListeBilletsFragment {

	static MesBilletsFragment init(int val) {
		MesBilletsFragment page = new MesBilletsFragment();
		Bundle args = new Bundle();
		args.putInt("val", val);
//		args.putParcelable("pager", (Parcelable) pager);
		page.setArguments(args);
		return page;
	}
}
