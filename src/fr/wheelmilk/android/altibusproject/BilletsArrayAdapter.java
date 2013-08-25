package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;

import fr.wheelmilk.android.altibusproject.models.Billet;
import fr.wheelmilk.android.altibusproject.support.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BilletsArrayAdapter extends ArrayAdapter<Billet> {

    private ArrayList<Billet> items;
    private Context context;
//    private int itemColor;

    public BilletsArrayAdapter(Context _context, int textViewResourceId, ArrayList<Billet> _items) {
            super(_context, textViewResourceId, _items);
            items = _items;
            context = _context;
    }
    
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
        Billet b = getItem(position);
        holder.tvGareDepart.setText(b.getGareDepart());
        holder.tvGareArrivee.setText(b.getGareRetour());
        holder.tvHeureArrivee.setText(b.getHaa());
        holder.tvHeureDepart.setText(b.getHa());
        holder.tvMontant.setText(b.getPrettyPt());
        holder.tvDateDepart.setText(Helper.prettifyDate(b.getDateAller(), null));
        holder.tvNbPassagers.setText(setPassagerText(b));
        
        if (b.isPerime()) holder.tvAction.setText(R.string.billetPerime);
        else {
        	if (b.getValide()) holder.tvAction.setText(R.string.billetComposte);
        	else holder.tvAction.setText(R.string.cliquerPourEditer);
        }
        	
		return v;
	}
	private String setPassagerText(Billet b) {
        int enfants = b.getNbEnfants();
        int adultes = b.getNbAdultes();
        StringBuilder s = new StringBuilder();
        if (enfants > 0) {
        	s.append(String.valueOf(enfants));
        	s.append(" billet");
        	if (enfants > 1) s.append("s");
        	s.append(" -26 ans");
        }
        if (adultes > 0) {
        	if(enfants>0) s.append(" / ");
        	s.append(String.valueOf(adultes));
        	s.append(" billet");
        	if (adultes > 1) s.append("s");
        	s.append(" +26 ans");
        }
        return s.toString();
	}
}