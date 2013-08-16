package fr.wheelmilk.android.altibusproject;

import java.io.Reader;
import java.io.StringReader;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;


public class AltibusSerializer {
	Class<AltibusDataModel> klass; // Type of object to build
		
	public AltibusDataModel serializeXml( String xmlString ) {

		Serializer serializer = new Persister();
		Reader reader = new StringReader(xmlString);
			
		try {
			return serializer.read(AltibusDataModel.class, reader, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
