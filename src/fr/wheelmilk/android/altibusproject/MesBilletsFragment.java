package fr.wheelmilk.android.altibusproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class MesBilletsFragment extends ListeBilletsFragment {

	static MesBilletsFragment init(int val) {
		MesBilletsFragment page = new MesBilletsFragment();
		Bundle args = new Bundle();
		args.putInt("val", val);
//		args.putParcelable("pager", (Parcelable) pager);
		page.setArguments(args);
		return page;
	}

//	@Override
//	public void onItemClick(AdapterView<?> av, View v, int position, long j) {
//		Log.v(LOG_TAG, "item Clicked :" + position);
//		onItemClicked(v, position);
//	}
}
