package uk.soton.cs.dataset;

public class Triple {

	String uid,objid,annotation;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getObjid() {
		return objid;
	}

	public Triple(String uid, String objid, String annotation) {
		super();
		this.uid = uid;
		this.objid = objid;
		this.annotation = annotation;
	}

	public void setObjid(String objid) {
		this.objid = objid;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
}
