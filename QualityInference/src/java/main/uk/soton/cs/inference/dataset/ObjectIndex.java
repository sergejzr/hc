package uk.soton.cs.inference.dataset;

import java.util.Hashtable;
import java.util.Random;

import uk.soton.cs.inference.algorithms.BigArray;

public class ObjectIndex {

	Hashtable<String, CSObject> objectidx;

	public ObjectIndex(Hashtable<String, CSObject> objectidx, Hashtable<String, CSUser> useridx) {
		super();
		this.objectidx = objectidx;
		this.useridx = useridx;
	}

	Hashtable<String, CSUser> useridx;
	private CSUser goldUser;

	public CSObject getObject(String objectid) {
		// TODO Auto-generated method stub
		return objectidx.get(objectid);
	}

	public Hashtable<String, CSObject> getObjectindex() {
		return objectidx;
	}

	public Hashtable<String, CSUser> getUserindex() {
		return useridx;
	}

	public void setGoldUser(CSUser goldUser) {
		this.goldUser=goldUser;
	}

	public CSUser getGolduser() {
		// TODO Auto-generated method stub
		return goldUser;
	}
	public BigArray randomizeArray()
	{
		BigArray ret=new BigArray();
		Random r=new Random();
		for(CSObject object:objectidx.values())
		{
			for(String userid: object.getUsers().keySet())
			{
				ret.set(object.getId(), userid, r.nextGaussian());
			}
		}
		return ret;
	}
}
