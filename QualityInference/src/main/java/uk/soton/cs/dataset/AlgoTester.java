package uk.soton.cs.dataset;

import java.util.Hashtable;

public interface AlgoTester {

	Hashtable<String, AlgoSummary> test(String[] args, Integer k);

}
