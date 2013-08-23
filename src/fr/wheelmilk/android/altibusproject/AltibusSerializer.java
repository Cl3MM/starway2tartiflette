package fr.wheelmilk.android.altibusproject;

import java.io.Reader;
import java.io.StringReader;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;
import fr.wheelmilk.android.altibusproject.models.AltibusDataModel;


public class AltibusSerializer<T> {
	Class<AltibusDataModel> klass; // Type of object to build
	private Class<T> classObjectToSerialize;
		
	public AltibusSerializer(Class<T> _classObjectToSerialize) {
		classObjectToSerialize = _classObjectToSerialize;
	}
	
	public Class<T> getClassToSerialize() {
	      return classObjectToSerialize;
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

	public T serializeXmlToObject( String xmlString ) {

		Serializer serializer = new Persister();
		Reader reader = new StringReader(xmlString);
		Log.v(this.getClass().toString(), "klass: " + getClassToSerialize().toString());
//		Log.v(this.getClass().toString(), "klass: " + klass.toString());
		try {
			return serializer.read(getClassToSerialize(), reader, false);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
//public class AltibusSerializer {
//	Class<AltibusDataModel> klass; // Type of object to build
//		
//	public AltibusDataModel serializeXml( String xmlString ) {
//
//		Serializer serializer = new Persister();
//		Reader reader = new StringReader(xmlString);
//			
//		try {
//			return serializer.read(AltibusDataModel.class, reader, false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public <T> Object serializeXmlToObject( String xmlString ) {
//
//		Serializer serializer = new Persister();
//		Reader reader = new StringReader(xmlString);
//		Log.v(this.getClass().toString(), "klass: " + klass.getClass().toString());
//		Log.v(this.getClass().toString(), "klass: " + klass.toString());
//		try {
//			return serializer.read(klass, reader, false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
