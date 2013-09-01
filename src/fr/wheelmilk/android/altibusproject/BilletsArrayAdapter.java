package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;

import fr.wheelmilk.android.altibusproject.models.BilletDB;
import fr.wheelmilk.android.altibusproject.support.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BilletsArrayAdapter extends ArrayAdapter<BilletDB> {

    private ArrayList<BilletDB> items;
    private Context context;
//    private int itemColor;

    public BilletsArrayAdapter(Context _context, int textViewResourceId, ArrayList<BilletDB> _items) {
            super(_context, textViewResourceId, _items);
            items = _items;
            context = _context;
    }
//    @Override
//    public boolean areAllItemsEnabled() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled(int position) {
//        return false;
//    }
	private class Holder {
	    TextView tvGareDepart;
	    TextView tvGareArrivee;	    
	    TextView tvHeureDepart;
	    TextView tvHeureArrivee;
	    TextView tvDateDepart;
	    TextView tvMontant;
	    TextView tvNbPassagers;
	    TextView tvAction;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Holder holder = new Holder();
		if (v == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            v = inflater.inflate(R.layout.mes_billets_list_items, parent, false);
            holder.tvGareDepart = (TextView) v.findViewById(R.id.tvGareDepart);
			holder.tvGareArrivee = (TextView) v.findViewById(R.id.tvGareArrivee);
			holder.tvHeureDepart = (TextView) v.findViewById(R.id.tvHeureDepart);
			holder.tvHeureArrivee = (TextView) v.findViewById(R.id.tvHeureArrivee);
			holder.tvDateDepart = (TextView) v.findViewById(R.id.tvDateDepart);
			holder.tvMontant = (TextView) v.findViewById(R.id.tvMontant);
			holder.tvNbPassagers = (TextView) v.findViewById(R.id.tvNbPassagers);
			holder.tvAction = (TextView) v.findViewById(R.id.tvSummary);
			
			v.setTag(holder);
		} else {
			holder = (Holder) v.getTag();
		}
        BilletDB b = getItem(position);
        holder.tvGareDepart.setText(b.getGareDepart());
        holder.tvGareArrivee.setText(b.getGareRetour());
        holder.tvHeureArrivee.setText(b.getHaa());
        holder.tvHeureDepart.setText(b.getHa());
        holder.tvMontant.setText(b.getPrettyPt());
        holder.tvDateDepart.setText(Helper.prettifyDate(b.getDateAller(), null));
        holder.tvNbPassagers.setText(b.getTypeBillet());
        
        if (b.isPerime()) holder.tvAction.setText(R.string.billetPerime);
        else {
        	if (b.isValide()) holder.tvAction.setText(R.string.billetComposte);
        	else holder.tvAction.setText(R.string.cliquerPourEditer);
        }
        	
		return v;
	}
}