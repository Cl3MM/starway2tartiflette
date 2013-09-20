package fr.wheelmilk.android.altibusproject;

import com.actionbarsherlock.app.SherlockFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AltibusMenuFragment extends SherlockFragment implements OnClickListener {
		RelativeLayout rlReservation;
		RelativeLayout rlHoraires;
		RelativeLayout rlMesBillets;
		RelativeLayout rlHistorique;
		RelativeLayout rlMonCompte;
		
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.slidingmenu,
	        container, false);
	    rlReservation = (RelativeLayout) view.findViewById(R.id.rlMenuReservation);
	    rlHoraires = (RelativeLayout) view.findViewById(R.id.rlMenuHoraires);
	    rlMesBillets = (RelativeLayout) view.findViewById(R.id.rlMenuMesBillets);
	    rlHistorique = (RelativeLayout) view.findViewById(R.id.rlMenuHistorique);
	    rlMonCompte = (RelativeLayout) view.findViewById(R.id.rlMenuMonCompte);

	    rlReservation.setOnClickListener(this);
	    rlHoraires.setOnClickListener(this);
	    rlMesBillets.setOnClickListener(this);
	    rlHistorique.setOnClickListener(this);
	    rlMonCompte.setOnClickListener(this);

	    return view;
	  }

	// the meat of switching the above fragment
//	private void switchFragment(Fragment fragment) {
//		if (getActivity() == null)
//			return;
//
//		if (getActivity() instanceof AltibusMainActivity) {
//			AltibusMainActivity fca = (AltibusMainActivity) getActivity();
//			fca.switchContent(fragment);
//		}
//	}
		private void switchFragment(int position) {
			if (getActivity() == null)
				return;

			if (getActivity() instanceof AltibusMainActivity) {
//				mP
				AltibusMainActivity fca = (AltibusMainActivity) getActivity();
				fca.switchContent(position);
			}
		}
	@Override
	public void onClick(View v) {
		int vid = v.getId();
		Log.v(getClass().toString(), "Item clicked: "+ vid);
//		Fragment newContent = null;
		int position = -1;
		if (vid == R.id.rlMenuReservation) {
//			newContent = PageAchat.init(0);
			position = 0;
		} else if (vid == R.id.rlMenuHoraires) {
			position = 1;
//			newContent = PageHorraires.init(1);
		} else if (vid == R.id.rlMenuMesBillets) {
			position = 2;
//			newContent = MesBilletsFragment.init(2);
		} else if (vid == R.id.rlMenuHistorique) {
//			newContent = HistoriqueFragment.init(3);
			position = 3;
		} else if (vid == R.id.rlMenuMonCompte) {
			startActivity(new Intent(getActivity(), UserPreferences.class));
		}
//		if (newContent != null)
//			switchFragment(newContent);
		if (position >= 0)
			switchFragment(position);
	}

}