package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;
import java.util.Locale;

import fr.wheelmilk.android.altibusproject.models.Gares4Geoloc;
import fr.wheelmilk.android.altibusproject.support.Config;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.antidots.android.altibus.R;

public class PopUpArrayAdapter extends ArrayAdapter<Gares4Geoloc>  implements Filterable {

    private ArrayList<Gares4Geoloc> mOriginalValues;
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
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Gares4Geoloc getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
//		Gares4Geoloc o = getItem(position);
		Gares4Geoloc o = items.get(position);
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
	@Override
    public Filter getFilter() {
        Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
				 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "results:" + results);
                items = (ArrayList<Gares4Geoloc>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Gares4Geoloc> FilteredArrList = new ArrayList<Gares4Geoloc>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Gares4Geoloc>(items); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return  
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {

                	constraint = constraint.toString().toLowerCase(Locale.FRANCE);
        			 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "constraint:" + constraint);

        			for(Gares4Geoloc item : items) {

                        if (item.name.toLowerCase(Locale.FRANCE).contains((constraint.toString()))) {
                            FilteredArrList.add(item);
                        }
            	    }
        			 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "Filter count:" + FilteredArrList.size());
        			 if (Config.DEBUG == 1) Log.v(this.getClass().toString(), "Full list count:" + items.size());

//                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i);
//                        if (data.toLowerCase().startsWith(constraint.toString())) {
//                            FilteredArrList.add(data);
//                        }
//                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
