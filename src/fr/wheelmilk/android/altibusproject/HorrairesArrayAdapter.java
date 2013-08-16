package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HorrairesArrayAdapter extends ArrayAdapter<HorrairesPopUpActivity.HorrairesMapping> {

    private ArrayList<HorrairesPopUpActivity.HorrairesMapping> items;
    private Context context;
    private int itemColor;

    public HorrairesArrayAdapter(Context _context, int textViewResourceId, ArrayList<HorrairesPopUpActivity.HorrairesMapping> _items, int _color) {
            super(_context, textViewResourceId, _items);
            items = _items;
            context = _context;
            itemColor = _color;
    }
    
	private class Holder {
	    TextView tvDepart;
	    TextView tvRetour;	    
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder holder = new Holder();
		if (v == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.popup_horraires_list_item, parent, false);
            holder.tvDepart = (TextView) v.findViewById(R.id.horraireAllerTv);
			holder.tvRetour = (TextView) v.findViewById(R.id.horraireArriveeTv);
			holder.tvDepart.setTextColor(itemColor);
			holder.tvRetour.setTextColor(itemColor);

			v.setTag(holder);
		} else {
			holder = (Holder) v.getTag();
		}
        HorrairesPopUpActivity.HorrairesMapping o = getItem(position);
        holder.tvDepart.setText(o.depart());
        holder.tvRetour.setText(o.arrivee());
		return v;
	}
}