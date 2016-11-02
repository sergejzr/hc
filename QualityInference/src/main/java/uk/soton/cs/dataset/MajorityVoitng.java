package uk.soton.cs.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;

import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class MajorityVoitng {

	public Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> refobjects, int level,
			String answer) {
		int tp = 0, fp = 0, tn = 0, fn = 0;

		Hashtable<String, Double> ret = new Hashtable<>();
		for (CSObject object : refobjects) {

			double tmp_sum_all = 0;

			Hashtable<String, Integer> cnts = new Hashtable<>();
			Hashtable<Integer, Integer> countings = new Hashtable<>();

			for (Annotation annotation : object.getUsers().values()) {

				Integer counting = countings.get(annotation.getAtLevel(level).size());
				if (counting == null) {
					counting = 0;
				}
				countings.put(annotation.getAtLevel(level).size(), counting + 1);
				for (String s : annotation.getAtLevel(level)) {
					Integer cnt = cnts.get(s);
					if (cnt == null) {
						cnt = 0;
					}
					cnts.put(s, cnt + 1);
				}

			}
			ArrayList<String> labels = new ArrayList<>();
			labels.addAll(cnts.keySet());

			Collections.sort(labels, new HTComparator<String>(cnts));
			Collections.reverse(labels);
			int max = 0;
			for (Integer c : countings.keySet()) {
				Integer cu = countings.get(c);
				if (cu > max)
					max = c;
			}

			HashSet<String> res = new HashSet<>();
			for (int i = 0; i < max; i++) {
				if(labels.get(i)==null) 
					continue;
				res.add(labels.get(i));
			}

			if (res.contains(answer)) {
				if (idx.getGolduser().getObjects().get(object.getId()).getAtLevel(level).contains(answer)) {
					tp++;
				} else {
					fp++;
				}
			} else {
				if (idx.getGolduser().getObjects().get(object.getId()).getAtLevel(level).contains(answer)) {
					fn++;
				} else {
					tn++;
				}
			}

			// System.out.println("Class "+answer+" is predicted
			// "+(tmp_sum_all>0?"true":"false")+" for object "+object.getId()+"
			// with true class:
			// "+idx.getGolduser().getObjects().get(object.getId()).getAtLevel(level)+"and
			// useranswers:");

			// System.out.println(object.printAnnotations(level));
		}

		double precision = 1. * tp / (tp + fp);
		double recall = 1. * tp / (tp + fn);

		ret.put("tp", (double)tp );
		ret.put("tn",(double)tn);
		ret.put("fp", (double)fp );
		ret.put("fn",(double)fn);
		ret.put("Accuracy", (1. * (tp + tn) / (tp + tn + fp + fn)));
		ret.put("Precision", precision);
		ret.put("Recall", recall );
		ret.put("F1", 2 * ((precision * recall) / (precision + recall)));
		return ret;

	}

}
