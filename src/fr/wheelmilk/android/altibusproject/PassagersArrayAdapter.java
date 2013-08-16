package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;

import fr.wheelmilk.android.altibusproject.models.Passager;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PassagersArrayAdapter extends ArrayAdapter<Passager> {

    private ArrayList<Passager> items;
    private Context context;


    public PassagersArrayAdapter(Context _context, int textViewResourceId, ArrayList<Passager> _passagers) {
            super(_context, textViewResourceId, _passagers);
            items = _passagers;
            context = _context;
    }
    
	private class PassagerHolder {
		RelativeLayout rlRoot;
	    TextView tvNom;
	    TextView tvTitle;
	    TextView tvAge;
	    TextView tvSummary;
	    View vRemove;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		PassagerHolder holder = new PassagerHolder();
		if (v == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.passagers_list_item, parent, false);
            holder.tvNom = (TextView) v.findViewById(R.id.tvNom);
            holder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            holder.tvAge = (TextView) v.findViewById(R.id.tvAge);
            holder.vRemove = v.findViewById(R.id.imgRemove);
            holder.rlRoot = (RelativeLayout) v.findViewById(R.id.rlRoot);

			v.setTag(holder);
		} else {
			holder = (PassagerHolder) v.getTag();
		}
		Passager o = getItem(position);
		if (position == 0) {
			holder.vRemove.setVisibility(View.GONE);
			holder.tvNom.setPadding(20, 0, 0, 0);
			holder.tvTitle.setPadding(20, 0, 0, 0);
		} else {
			holder.vRemove.setVisibility(View.VISIBLE);
		}
		if (o.isValid(v.getResources())) {
			holder.tvNom.setTextColor(v.getResources().getColor(R.color.orange_dark));
		} else {
			holder.tvNom.setTextColor(v.getResources().getColor(R.color.table_content));
		}
        holder.tvNom.setText( o.getFullName());
        holder.tvTitle.setText(getTitle(position));
        holder.tvAge.setText( o.getAgeAsStringWithYear());

//        int pL = holder.rlRoot.getPaddingLeft();
//        int pT = holder.rlRoot.getPaddingTop();
//        int pR = holder.rlRoot.getPaddingRight();
//        int pB = holder.rlRoot.getPaddingBottom();
//
//        holder.rlRoot.setBackgroundResource(R.drawable.passager_list_item_normal);
//        holder.rlRoot.setPadding(pL, pT, pR, pB);

        return v;
	}

	public String getTitle(int position) {
		String str = "Principal";
		if (position > 0) str = String.valueOf(position + 1);
		return "Passager " + str;
	}
}