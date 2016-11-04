package uk.soton.cs.dataset;

import java.util.Hashtable;

public class AlgoThread extends Thread {
	private AlgoTester t;
	private int k;
	private String[] args;
	private AlgorithmResult result;

	public AlgoThread(AlgoTester t, String[] args, int k) {
		this.t = t;
		this.k = k;
		this.args = args;
	}

	public AlgorithmResult getResult() {
		return result;
	}

	@Override
	public void run() {

		Hashtable<String, AlgoSummary> res = t.test(args, k);

		result = new AlgorithmResult();

		for (String alg : res.keySet()) {
			result.add(alg, "k=" + k, res.get(alg));
		}

	}

	public Integer getK() {
		// TODO Auto-generated method stub
		return k;
	}
}
