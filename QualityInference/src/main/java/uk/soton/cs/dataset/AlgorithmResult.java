package uk.soton.cs.dataset;

import java.util.Hashtable;

public class AlgorithmResult {

	private Hashtable<String, Hashtable<String, AlgoSummary>> summaries = new Hashtable<>();

	public void add(String method, String setup, AlgoSummary summary) {

		Hashtable<String, AlgoSummary> csummary = summaries.get(setup);
		if (csummary == null) {
			summaries.put(setup, csummary = new Hashtable<>());
		}

		csummary.put(method, summary);

	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		for(String setup:summaries.keySet())
		{
			sb.append("Setup: "+setup+"\n");
			
			AlgoSummary anyalgo = summaries.get(setup).elements().nextElement();
			
			for(String measure:anyalgo.measures.keySet())
			{
				sb.append(measure+": " );
			
				
				for(String algoname:summaries.get(setup).keySet())
				{
					sb.append(algoname+"="+summaries.get(setup).get(algoname).measures.get(measure));
					sb.append(",");
				}
				sb.append("\n");
			}
			
			
			
			
			
			
			
		}
		return sb.toString();
	}

}
