package uk.soton.cs.inference.dataset;

import java.util.Hashtable;

public class CSUser {
	String id;

	public CSUser(String id) {
		super();
		this.id = id;
	}

	Hashtable<String, Annotation> objects = new Hashtable<>();

	public void addAnnotation(Annotation annotation) {
		objects.put(annotation.getObject().getId(), annotation);

	}

	public String getId() {
		return id;
	}
	public Hashtable<String, Annotation> getObjects() {
		return objects;
	}
}
