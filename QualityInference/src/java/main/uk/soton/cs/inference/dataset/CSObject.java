package uk.soton.cs.inference.dataset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

public class CSObject {
	String id;

	public CSObject(String id) {
		super();
		this.id = id;
	}


Hashtable<String, Annotation> users=new Hashtable<>();
//Hashtable<Integer, HashSet<String>> levelannotations=new Hashtable<>();

	public void addAnnotation(Annotation annotation) {
		users.put(annotation.getUser().getId(), annotation);
	}
	public String getId() {
		return id;
	}
	public Hashtable<String, Annotation> getUsers() {
		return users;
	}
	
Vector<Hashtable<String, HashSet<String>>> answersidx=null;

	public boolean hasAnnotationExceptForUser(int level, String answer, CSUser user) {
		if(answersidx==null)
		{
			createIndex();
		}
		
		HashSet<String> conti = answersidx.elementAt(level).get(answer);
		if(conti==null) return false;
		return conti.size()>1 || !conti.contains(user.getId());
		
		
	}
	private void createIndex() {
		
		if(answersidx!=null) return;
		answersidx=new Vector<>();
		for(Annotation a:users.values())
		{
			for(int i=0;i<a.levelsSize();i++)
			{
				Hashtable<String, HashSet<String>> levelidx;
				if(answersidx.size()<i+1)
				{
					answersidx.add(levelidx=new Hashtable<String, HashSet<String>>());
				}
				
				levelidx=answersidx.elementAt(i);
						
						
				HashSet<String> curusers = levelidx.get(a.getAtLevel(i));
				if(curusers==null)
				{
					levelidx.put(a.getAtLevel(i), curusers=new HashSet<>());
				}
				curusers.add(a.user.getId());
				
			}
		}
		
	}
	public boolean hasAnnotation(int level, String answer) {
		return answersidx.elementAt(level).get(answer)!=null;
		
	}
	public String printAnnotations(int level) {
		// TODO Auto-generated method stub
		Hashtable<String, HashSet<String>> answers = answersidx.get(level);
		StringBuilder sb=new StringBuilder();
		
		for(String label:answers.keySet())
		{
			sb.append(label+answers.get(label)+"\n");
		}
		return sb.toString();
	}
}
