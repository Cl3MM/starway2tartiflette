package fr.wheelmilk.android.altibusproject;

import java.util.ArrayList;

import fr.wheelmilk.android.altibusproject.models.Passager;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PassagersArrayAdapter extends ArrayAdapter<Passager> {

    private ArrayList<Passager> items;
    private Context context;
	private String nom;
//	private String prenom;
	private String saisirNom;

    public PassagersArrayAdapter(Context _context, int textViewResourceId, ArrayList<Passager> _passagers) {
            super(_context, textViewResourceId, _passagers);
            items = _passagers;
            context = _context;
    		nom = context.getResources().getString(R.string.saisirNom);
//    		prenom = context.getResources().getString(R.string.pref_user_last_name_summary);
    		saisirNom = context.getResources().getString(R.string.saisirNomPrenom);
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
            holder.tvNom = (TextView) v.findViewById(R.id.tvDateDepart);
            holder.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            holder.tvAge = (TextView) v.findViewById(R.id.tvHeureDepart);
            holder.tvSummary = (TextView) v.findViewById(R.id.tvSummary);
            holder.vRemove = v.findViewById(R.id.imgRemove);
            holder.rlRoot = (RelativeLayout) v.findViewById(R.id.rlRoot);

			v.setTag(holder);
		} else {
			holder = (PassagerHolder) v.getTag();
		}
		Passager p = getItem(position);
		if (position == 0) {
			holder.vRemove.setVisibility(View.GONE);
			holder.tvNom.setPadding(20, 0, 0, 0);
			holder.tvTitle.setPadding(20, 0, 0, 0);
			setSummaryForMain(p, v, holder);
		} else {
			holder.vRemove.setVisibility(View.VISIBLE);
			setSummary(p, v, holder);
		}
		if (p.getNom().equalsIgnoreCase(nom) || TextUtils.isEmpty(p.getNom()) ) {// Pas de nom
			holder.tvNom.setText(saisirNom);
		} else {
			holder.tvNom.setText( p.getFullName());
		}
        holder.tvTitle.setText(getTitle(position));

        if (p.getAge() < 0) {
			holder.tvAge.setText("");
        } else { holder.tvAge.setText( p.getAgeAsStringWithYear());
        }

//        int pL = holder.rlRoot.getPaddingLeft();
//        int pT = holder.rlRoot.getPaddingTop();
//        int pR = holder.rlRoot.getPaddingRight();
//        int pB = holder.rlRoot.getPaddingBottom();

//        holder.rlRoot.setBackgroundResource(R.drawable.passager_list_item_normal);
//        holder.rlRoot.setPadding(pL, pT, pR, pB);

        return v;
	}
	private void setSummaryForMain(Passager p, View v, PassagerHolder holder) {
		if (p.isPassagerPrincipalValid(v.getResources())) {
			holder.tvSummary.setTextColor(v.getResources().getColor(R.color.table_content));
			holder.tvSummary.setText(v.getResources().getString(R.string.modifierPassagerPrincipal));
		} else {
			holder.tvSummary.setTextColor(v.getResources().getColor(R.color.orange_dark));
			holder.tvSummary.setText(v.getResources().getString(R.string.ouvrirPrefPassagerPrincipal));
		}
	}
	private void setSummary(Passager p, View v, PassagerHolder holder) {
		if (p.isValid(v.getResources())) {
			holder.tvSummary.setTextColor(v.getResources().getColor(R.color.table_content));
			holder.tvSummary.setText(v.getResources().getString(R.string.editerInfoPassager));
		} else {
			holder.tvSummary.setTextColor(v.getResources().getColor(R.color.orange_dark));
			holder.tvSummary.setText(v.getResources().getString(R.string.completerInfoPassager));
		}
	}
	public String getTitle(int position) {
		String str = "Principal";
		if (position > 0) str = String.valueOf(position + 1);
		return "Passager " + str;
	}
}