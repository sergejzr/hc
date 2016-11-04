package uk.soton.cs.dataset;

import java.util.Hashtable;

public class AlgoSummary {

	Hashtable<String, Double> measures=new Hashtable<>();
	public void addMeasure(String measure, Double double1) {
		measures.put(measure, double1);
		
	}


}
