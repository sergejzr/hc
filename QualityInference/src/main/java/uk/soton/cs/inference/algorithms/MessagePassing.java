package uk.soton.cs.inference.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class MessagePassing {

	public Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> refobjects, int level,
			String answer) {
		double delta = 2;
		double difference = Double.MAX_VALUE;

		Hashtable<String, String> evals;
		BigArray A = new BigArray();
		BigArray Y = idx.randomizeArray();
		BigArray X = new BigArray();

//		/System.out.println();
		int k = 0;

		HashSet<String> cache = new HashSet<>();
		while (difference > delta && k++ < 4) {

		//	System.out.println("Round " + k + " start //-------------------------------------");
			// BigArray oldY=Y.copy();

			for (CSObject object : idx.getObjectindex().values()) {
				double tmp_sum_all = 0;

				for (Annotation annotation : object.getUsers().values()) {
					CSUser user = annotation.getUser();

					String key = object.getId() + "|" + level + "|" + user.getId();

					if (cache.contains(key) || object.hasAnnotationExceptForUser(level, answer, user)) {
						A.set(object.getId(), user.getId(), 1);
						tmp_sum_all += Y.get(object.getId(), user.getId());
						cache.add(key);
					} else {
						A.set(object.getId(), user.getId(), -1);
						tmp_sum_all -= Y.get(object.getId(), user.getId());
					}
				}

				double tmp_j = 0;
				for (Annotation annotation : object.getUsers().values()) {
					CSUser user = annotation.getUser();

					String key = object.getId() + "|" + level + "|" + user.getId();

					if (cache.contains(key) || object.hasAnnotationExceptForUser(level, answer, user)) {
						// A.set(object.getId(), user.getId(), 1);
						// tmp_sum_all+=Y.get(object.getId(), user.getId());
						tmp_j = Y.get(object.getId(), user.getId());
						cache.add(key);
					} else {
						// A.set(object.getId(), user.getId(), -1);
						//// tmp_sum_all-=Y.get(object.getId(), user.getId());
						tmp_j = -Y.get(object.getId(), user.getId());
					}
					X.set(object.getId(), user.getId(), tmp_sum_all - tmp_j);
				}

			}

			boolean firstround = true;
			for (CSObject object : idx.getObjectindex().values()) {
				for (CSUser user : idx.getUserindex().values()) {

					double tmp_sum_j_not_i = user.computeAnnotationSum(firstround, answer, level, X);

					Annotation annotation = user.getObjects().get(object.getId());
					if (annotation == null)
						continue;
					if (annotation.getAtLevel(level).contains(answer)) {
						tmp_sum_j_not_i -= X.get(annotation.getObject().getId(), user.getId());
					} else {
						tmp_sum_j_not_i += X.get(annotation.getObject().getId(), user.getId());
					}

					Y.set(object.getId(), user.getId(), tmp_sum_j_not_i);
					// difference = Y.difference(oldY);

				}
				firstround = false;
			}

		//	System.out.println("predict objects at round " + k);

		}
		return evaluateAll(level, answer, idx, A, X, Y, refobjects);

	}

	private Hashtable<String, Double> evaluateAll(int level, String answer, ObjectIndex idx, BigArray A, BigArray X,
			BigArray Y, Collection<CSObject> refobjects) {
		int tp = 0, fp = 0, tn = 0, fn = 0;

		Hashtable<String, Double> ret = new Hashtable<>();
		for (CSObject object : refobjects) {

			double tmp_sum_all = 0;

			for (Annotation annotation : object.getUsers().values()) {
				if (object.hasAnnotation(level, answer)) {
					A.set(object.getId(), annotation.getUser().getId(), 1);
					tmp_sum_all += Y.get(object.getId(), annotation.getUser().getId());
				} else {
					A.set(object.getId(), annotation.getUser().getId(), -1);
					tmp_sum_all -= Y.get(object.getId(), annotation.getUser().getId());
				}
			}

			if (tmp_sum_all > 0) {
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
