package fr.wheelmilk.android.altibusproject.models;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "Altibus.data")
public class AltibusDataPays {
	
	@ElementList(entry = "pays", inline = true)
	public List<Pays> listePays;

	public AltibusDataPays(@ElementList(inline = true) List<Pays> _listePays) {
		listePays = _listePays;
	}
//	public AltibusDataPays(ArrayList<Pays> _listePays) {
//		listePays = _listePays;
//	}
	public ArrayList<String> getListPays() {
		ArrayList<String> liste = new ArrayList<String>();
		for(Pays item : listePays) {
			liste.add( item.getPays() );
	    }
		return liste;
	}
	public String paysToString() {
		StringBuilder s = new StringBuilder();
		for(Pays item : listePays) {
			s.append(item.getPays());
			s.append("\n");
	    }
		return s.toString();
	}

//	@Element
//	public Pays pays;
//	
//	public AltibusDataPays(Pays _listePays) {
//		pays = _listePays;
//	}
//	public String getListPays() {
//		return pays.getPays();
//	}
}