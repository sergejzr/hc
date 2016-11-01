package uk.soton.cs.inference.dataset;

import java.util.Vector;

public class Annotation {

CSObject object;
CSUser user;
public Annotation(CSObject object, CSUser user) {
	super();
	this.object = object;
	this.user = user;
}
Vector<String> levels=new Vector<>();

public void addLevel(int level, String annotationstr) {
	while(levels.size()<level+1)
	{
		levels.add(null);
	}
	levels.set(level, annotationstr);
	
}

public CSUser getUser() {
	return user;
}
public CSObject getObject() {
	return object;
}

public String getAtLevel(int i) {
	return levels.get(i);
	
}

public int levelsSize() {
	// TODO Auto-generated method stub
	return levels.size();
}
}
