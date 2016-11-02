package uk.soton.cs.dataset;

import java.util.Comparator;
import java.util.Hashtable;

public class HTComparator<K> implements Comparator<K> {

	private Hashtable<K, Integer> cnts;

	public HTComparator(Hashtable<K, Integer> cnts) {
		this.cnts=cnts;
	}

	@Override
	public int compare(K o1, K o2) {
		// TODO Auto-generated method stub
		return cnts.get(o1).compareTo(cnts.get(o2));
	}



}
