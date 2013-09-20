package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import fr.wheelmilk.android.altibusproject.models.Gares4Geoloc;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PopUpArrayAdapter extends ArrayAdapter<Gares4Geoloc> {

	public ArrayList<Gares4Geoloc> items;
	int layoutResourceId;
	private Context context;
	boolean geoloc = false;
    private int itemColor;
    
	public PopUpArrayAdapter(Context _context, boolean _geoloc, int _layoutResourceId, ArrayList<Gares4Geoloc> _items, int _itemColor) {
		super(_context, _layoutResourceId, _items);	
		items = _items;
		layoutResourceId = _layoutResourceId;
		context = _context;
		geoloc = _geoloc;
	    itemColor = _itemColor;
	}

	private class GaresHolder {
	    TextView tv;
	    TextView distance;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		GaresHolder holder = new GaresHolder();
		if (v == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.popup_list_item, parent, false);
			holder.tv = (TextView) v.findViewById(R.id.itemText);
			holder.distance = (TextView) v.findViewById(R.id.tvDistance);
			holder.distance.setTextColor(itemColor);
			v.setTag(holder);
		} else {
			holder = (GaresHolder) v.getTag();
		}
		Gares4Geoloc o = getItem(position);
		holder.tv.setText(o.name);

		if (geoloc) {
			holder.distance.setText(o.distance);
		} else {
			holder.distance.setVisibility(View.GONE);
		}
		return v;
	}
	@Override
	public String toString() {
		String listString = "";
		for (Gares4Geoloc s : items) {
		    listString += s + "\n";
		}
		return listString;
	}
}