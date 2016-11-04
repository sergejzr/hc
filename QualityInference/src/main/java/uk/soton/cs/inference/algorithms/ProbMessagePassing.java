package uk.soton.cs.inference.algorithms;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class ProbMessagePassing {

	public Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> refobjects, int level,
			String answer) {
		double delta = 2;
		double difference = Double.MAX_VALUE;

		BigArray A = new BigArray();
		// BigArray Y = idx.randomizeArray();
		BigArray Y = idx.randomizeArrayPos();
		BigArray X = new BigArray();

		// /System.out.println();
		int k = 40;
		System.out.println("Y: " + Y.toString(2, 2));
		HashSet<String> cache = new HashSet<>();
		while (difference > delta && k-- > 0) {

			// System.out.println("Round " + k + " start
			// //-------------------------------------");
			// BigArray oldY=Y.copy();

			for (CSUser user : idx.getUserindex().values()) {

				for (Annotation annotation : user.getAnnotationsByThisUser().values()) {
					CSObject object = annotation.getObject();
					double tpm_sum = 0.0;
					int cnt = 0;
					for (Annotation uannotator : object.getUsers().values()) {
						CSUser annotator = uannotator.getUser();
						if (annotator.sameAs(user)) {
							continue;
						}

						if (uannotator.getAtLevel(level).contains(answer)) {
							tpm_sum += Y.get(object.getId(), annotator.getId());
						} else {
							tpm_sum -= Y.get(object.getId(), annotator.getId());
						}
						cnt++;
					}
					// if(cnt>0) tpm_sum/=cnt;
					X.set(object.getId(), user.getId(), tpm_sum);
				}

			}

			// System.out.println("X: "+X.toString(2,2));

			for (CSObject object : idx.getObjectindex().values()) {

				for (Annotation uannotation : object.getUsers().values()) {

					CSUser user = uannotation.getUser();
					double tmp_sum = 0.0;
					int cnt = 0;
					for (Annotation annotation : user.getAnnotationsByThisUser().values()) {

						if (annotation.getObject().sameObject(object)) {
							continue;
						}
						if (annotation.getAtLevel(level).contains(answer)) {
							tmp_sum += X.get(annotation.getObject().getId(), user.getId());
						} else {
							tmp_sum -= X.get(annotation.getObject().getId(), user.getId());
						}
						cnt++;
					}
					// if(cnt>0) tmp_sum/=cnt;
					Y.set(object.getId(), user.getId(), tmp_sum);
				}

			}

			System.out.println("Y: " + Y.toString(2, 2));

		}
		return evaluateAll(level, answer, idx, A, X, Y, refobjects);

	}

	private Hashtable<String, Double> evaluateAll(int level, String answer, ObjectIndex idx, BigArray A, BigArray X,
			BigArray Y, Collection<CSObject> refobjects) {
		double tp = 0, fp = 0, tn = 0, fn = 0;

		Hashtable<String, Double> ret = new Hashtable<>();
		for (CSObject object : refobjects) {

			double tmp_sum_all = 0;

			for (Annotation annotation : object.getUsers().values()) {

				if (annotation.getAtLevel(level).contains(answer)) {
					// System.out.println("Object: "+object.getId()+"
					// user:"+annotation.getUser().getId()+"
					// labels:"+annotation.getAtLevel(level));

					tmp_sum_all += Y.get(object.getId(), annotation.getUser().getId());
				} else {

					tmp_sum_all -= Y.get(object.getId(), annotation.getUser().getId());
				}
				/*
				 * 
				 * if (object.hasAnnotation(level, answer)) {
				 * 
				 * System.out.println("Annotations: "
				 * +annotation.getAtLevel(level));
				 * 
				 * A.set(object.getId(), annotation.getUser().getId(), 1);
				 * tmp_sum_all += Y.get(object.getId(),
				 * annotation.getUser().getId()); } else { A.set(object.getId(),
				 * annotation.getUser().getId(), -1); tmp_sum_all -=
				 * Y.get(object.getId(), annotation.getUser().getId()); }
				 */

			}

			if (tmp_sum_all > 0) {
				if (idx.getGolduser().getAnnotationsByThisUser().get(object.getId()).getAtLevel(level)
						.contains(answer)) {
					tp++;
				} else {
					fp++;
				}
			} else {
				if (idx.getGolduser().getAnnotationsByThisUser().get(object.getId()).getAtLevel(level)
						.contains(answer)) {
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

		// if(fp==0){fp=0.001;}
		// if(fn==0){fn=0.001;}

		double precision = 1. * tp / (tp + fp);
		double recall = 1. * tp / (tp + fn);

		ret.put("tp", (double) tp);
		ret.put("tn", (double) tn);
		ret.put("fp", (double) fp);
		ret.put("fn", (double) fn);
		ret.put("Accuracy", (1. * (tp + tn) / (tp + tn + fp + fn)));
		ret.put("Precision", precision);
		ret.put("Recall", recall);
		ret.put("F1", 2 * ((precision * recall) / (precision + recall)));

		return ret;

	}

}
