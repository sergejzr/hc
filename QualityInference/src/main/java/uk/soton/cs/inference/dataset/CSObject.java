package uk.soton.cs.inference.dataset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class CSObject {
	String id;

	public CSObject(String id) {
		super();
		this.id = id;
	}

public String toString()
{
	
StringBuilder sb=new StringBuilder();
sb.append(id);
sb.append("\t");

for(String uid:users.keySet())
{
sb.append(uid+"("+users.get(uid).levels+") ");
}
return sb.toString();
};
Hashtable<String, Annotation> users=new Hashtable<>();
//Hashtable<Integer, HashSet<String>> levelannotations=new Hashtable<>();

public Set<String> getAnnotatorIds()
{
	return users.keySet();
}
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
		answersidx=new Vector<Hashtable<String, HashSet<String>>>();
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
						
				
				for(String lable: a.getAtLevel(i))
				{
					HashSet<String> curusers = levelidx.get(lable);
					
					if(curusers==null)
					{
						levelidx.put(lable, curusers=new HashSet<>());
					}
					curusers.add(a.user.getId());
					
				}
						
				
				
				
				
				
				
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
	@Override
	public boolean equals(Object obj) {
		
		return this.id.equals(((CSObject)obj).id);
	}
	public boolean sameObject(CSObject object) {
		// TODO Auto-generated method stub
		return this.id.equals(object.id);
	}
}
