package fr.wheelmilk.android.altibusproject.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

//public interface AltibusDataModel {
//
//	public ArrayList<String> itemNames();
//		new UnsupportedOperationException("Not implemented yet");
//		return null;
//	}
//}

@Root(name = "Altibus.data", strict = false)
public class AltibusDataModel implements Parcelable {
   
	@ElementListUnion({
	      @ElementList(entry="garesDepart", inline=true, type=GaresDepart.class),
	      @ElementList(entry="garesArrivee", inline=true, type=GaresArrivee.class),
	      @ElementList(entry="horaireAller", inline=true, type=HorrairesAller.class),
	      @ElementList(entry="horaireRetour", inline=true, type=HorrairesRetour.class)
	   })
	public List<GaresDataModel> items;

	public AltibusDataModel( @ElementListUnion({
	      @ElementList(entry="garesDepart", inline=true, type=GaresDepart.class),
	      @ElementList(entry="garesArrivee", inline=true, type=GaresArrivee.class),
	      @ElementList(entry="horaireAller", inline=true, type=HorrairesAller.class),
	      @ElementList(entry="horaireRetour", inline=true, type=HorrairesRetour.class)
	   }) List<GaresDataModel> _items ) {
		items = _items;
	}
	public AltibusDataModel() {
	}
	public HashMap<String, GaresDataModel> objects() {
		HashMap<String, GaresDataModel> objectMap = new HashMap<String, GaresDataModel>();

		for(GaresDataModel item : items) {
			objectMap.put( item.gareName(), item );
	    }
		return objectMap;
	}
	
	public TreeMap<Float, Gares4Geoloc> garesDeLaMuerte(Location loc, int distanceMaxInKilometers) {
		TreeMap<Float, Gares4Geoloc> objectMap = new TreeMap<Float, Gares4Geoloc>();

		for(GaresDataModel item : items) {
			Gares4Geoloc g = new Gares4Geoloc( item.gareName(), item.location() );
			if (g.getDistance(loc) != null) {
				if (g.distanceFloat < (float) distanceMaxInKilometers * 1000) objectMap.put( g.distanceFloat, g);
			}
	    }
		return objectMap;
	}
	
	public ArrayList<Gares4Geoloc> garesWithDistance() {
		ArrayList<Gares4Geoloc> objectMap = new ArrayList<Gares4Geoloc>();

		for(GaresDataModel item : items) {
			objectMap.add(new Gares4Geoloc( item.gareName(), item.location() ) );
	    }
		return objectMap;
	}

	public ArrayList<String> itemNames() {
		ArrayList<String> itemNames = new ArrayList<String>();

		for(GaresDataModel item : items) {
			itemNames.add( item.gareName() );
	    }
		return itemNames;
	}

	public AltibusDataModel(Parcel in) {
		in.readTypedList(items, null);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(items);
	}
    public static final Parcelable.Creator<AltibusDataModel> CREATOR = new Parcelable.Creator<AltibusDataModel>() {
        @Override
		public AltibusDataModel createFromParcel(Parcel in) {
            return new AltibusDataModel(in);
        }

		@Override
		public AltibusDataModel[] newArray(int size) {
			return null;
		}
    };
}