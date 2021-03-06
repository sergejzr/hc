package uk.soton.cs.inference.dataset;

import java.util.Hashtable;

import uk.soton.cs.inference.algorithms.BigArray;

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
	public Hashtable<String, Annotation> getAnnotationsByThisUser() {
		return objects;
	}
	
	public Annotation getHerAnnotationForObject(String objectid)
	{
		return objects.get(objectid);
	}

	public Annotation getHerAnnotationForObject(CSObject object)
	{
		return getHerAnnotationForObject(object.getId());
	}
	
	Hashtable<String, Double> cache=new Hashtable<>();
	
	public double computeAnnotationSum(boolean reindex, String answer, int level, BigArray X) {

		String key=answer+"|"+level;
		Double ret = cache.get(key);
		if(!reindex&&ret!=null) return ret;
		ret=0.0;
		for(Annotation annotation:getAnnotationsByThisUser().values())
		{

			if(answer.equals(annotation.getAtLevel(level)))
			{

				ret+= X.get(annotation.getObject().getId(), getId());
			}else
			{
				
				ret-= X.get(annotation.getObject().getId(), getId());
			}
			
		}
		cache.put(key, ret);
		return ret;
	}

	public boolean sameAs(CSUser user) {
		// TODO Auto-generated method stub
		return user.id.equals(id);
	}
}
