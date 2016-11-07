package uk.soton.cs.dataset;

import java.util.Collection;
import java.util.Hashtable;

import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;

public abstract class Algorithm {

	private String name;
	public abstract Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> values, int level, String lable,
			Integer k);
public Algorithm(String name) {
	this.name=name;
}
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
