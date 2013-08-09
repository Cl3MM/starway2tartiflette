package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PopUpArrayAdapter extends ArrayAdapter<String> {

	public ArrayList<String> items;
	int layoutResourceId;
	private Context context;

	public PopUpArrayAdapter(Context _context, int _layoutResourceId, ArrayList<String> _items) {
		super(_context, _layoutResourceId, _items);
		items = _items;
		layoutResourceId = _layoutResourceId;
		context = _context; 
	}

	private class GaresHolder {
	    TextView tv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		GaresHolder holder = new GaresHolder();
		if (v == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.popup_list_item, parent, false);
			holder.tv = (TextView) v.findViewById(R.id.itemText);
			v.setTag(holder);
		} else {
			holder = (GaresHolder) v.getTag();
		}
		String o = getItem(position);
		holder.tv.setText(o);
		return v;
	}
	@Override
	public String toString() {
		String listString = "";
		for (String s : items) {
		    listString += s + "\n";
		}
		return listString;
	}
}