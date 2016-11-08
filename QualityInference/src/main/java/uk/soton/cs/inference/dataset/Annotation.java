package uk.soton.cs.inference.dataset;

import java.util.HashSet;
import java.util.Vector;

public class Annotation {

CSObject object;
CSUser user;
public Annotation(CSObject object, CSUser user) {
	super();
	this.object = object;
	this.user = user;
}
Vector<HashSet<String>> levels=new Vector<>();
private String time;
public String getTime() {
	return time;
}
public void addLevel(int level, String annotationstr) {
	while(levels.size()<level+1)
	{
		levels.add(null);
	}
	HashSet<String> conti = levels.elementAt(level);
	if(conti==null){
		levels.set(level, conti=new HashSet<>());
	}
	conti.add(annotationstr);
	
}

public CSUser getUser() {
	return user;
}
public CSObject getObject() {
	return object;
}

public HashSet<String> getAtLevel(int i) {
	return levels.get(i);
	
}

public int levelsSize() {
	// TODO Auto-generated method stub
	return levels.size();
}

public void addTimestamp(String time) {
	this.time=time;
	
}
}
