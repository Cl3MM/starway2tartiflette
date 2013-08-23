package fr.wheelmilk.android.altibusproject.models;

import java.util.ArrayList;
import java.util.TreeMap;
import com.loopj.android.http.RequestParams;

import fr.wheelmilk.android.altibusproject.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Passagers extends ArrayList<Passager> implements Parcelable {

	private static final long serialVersionUID = 1L;
	StringBuilder erroMessages;
	RequestParams params;
	GaresArrivee ga;
	
	public Passagers() {
		super();
	}
	public Passagers(ArrayList<Passager> _passagers) {
		super();
		this.addAll( _passagers );
	}
	public Passagers(Passager _passager) {
		super();
		this.add(_passager);
	}
	public Passager first() {
		if (size() > 0) {
			return get(0);
		} else {
			return null;
		}
	}

	public boolean isValid(Resources mRes) {
		if (size() == 0) return false;
		boolean result = true;
		erroMessages = new StringBuilder();
		StringBuilder messagePassagerPrincipal = new StringBuilder();
		StringBuilder messages = new StringBuilder();
		
		for(Passager p : this) {
			boolean isValid; 
			if (p.isPrincipal) isValid = p.isPassagerPrincipalValid(mRes);
			else isValid = p.isValid(mRes);

			if (!isValid) {
				StringBuilder message = new StringBuilder();
				Log.v(this.getClass().toString(), p.getClass().toString());
				if ( p.isPrincipal ) {
					messagePassagerPrincipal.append(p.errorMessages());
				} else {
					// if (position == 0)
					messages.append("- ").append(mRes.getString(R.string.passager)).append(" ").append(String.valueOf(indexOf(p) + 1));
					messages.append("\n");
				}
			} 
		}
		if (messagePassagerPrincipal.length()> 0) {
			erroMessages.append( messagePassagerPrincipal.toString() );
			erroMessages.append("\n");
		}
		if (messages.length()>0) {
			erroMessages.append(mRes.getString(R.string.passagersInvalides));
			erroMessages.append("\n");
			erroMessages.append(messages);
		}

		if (erroMessages.length()> 0) { 
			result = false;
		}
		Log.v(this.getClass().toString(), "Passager valid? " + result);
		return result;
	}
	public String getErrorMessages() {
		return erroMessages.toString();
	}
	public int getNombreEnfants() {
		int nbEnfants = 0;
		for(Passager p : this) {
			if (p.isEnfant()) nbEnfants += 1 ; 
		}
		return nbEnfants;
	}
	public int getNombreAdultes() {
		return size() - getNombreEnfants();
	}

	public TreeMap<String, String> getParams(Context cxt) {
		TreeMap<String, String> paramsTree = new TreeMap<String, String>();

		for(Passager p : this) {
			int position = this.indexOf(p); 
			if (p.isPrincipal)
				paramsTree.putAll( p.getPassagerPrincipalParams(cxt) );
			else
				paramsTree.putAll( p.getParams(cxt) );
		}

		return paramsTree;
	}

	// Parcelable implementation
	public Passagers(Parcel in) {
		super();
		in.readTypedList(this, Passager.CREATOR);
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeList(this);
		dest.writeTypedList(this);
	}
    public static final Parcelable.Creator<Passagers> CREATOR = new Parcelable.Creator<Passagers>() {
        @Override
		public Passagers createFromParcel(Parcel in) {
            return new Passagers(in);
        }
 
        @Override
		public Passagers[] newArray(int size) {
            return new Passagers[size];
        }
    };
}
