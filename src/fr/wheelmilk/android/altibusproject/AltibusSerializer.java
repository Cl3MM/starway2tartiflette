package fr.wheelmilk.android.altibusproject;

import java.io.Reader;
import java.io.StringReader;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;
import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;


public class AltibusSerializer {
	Class<AltibusDataModel> klass; // Type of object to build
		
	
	
	
	final Class<T> typeParameterClass;

    public Foo(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }
    
    
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

	public <T> Object serializeXmlToObject( String xmlString ) {

		Serializer serializer = new Persister();
		Reader reader = new StringReader(xmlString);
		Log.v(this.getClass().toString(), "klass: " + klass.getClass().toString());
		Log.v(this.getClass().toString(), "klass: " + klass.toString());
		try {
			return serializer.read(klass, reader, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
