package uk.soton.cs.inference.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

import moa.recommender.rc.utils.Hash;
import uk.soton.cs.dataset.Algorithm;
import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class ExtendedSimpleRandomWalk extends Algorithm {

	public ExtendedSimpleRandomWalk() {
		super("SRW");

	}

	@Override
	public Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> values, int level, String answer,
			Integer k) {

		Hashtable<String, Double> opprobs = new Hashtable<>();
		Hashtable<String, Double> onprobs = new Hashtable<>();

		Hashtable<String, Double> usertp = new Hashtable<>();

		Random r = new Random();

		Hashtable<String, Double> realuserprecision = new Hashtable<>();

		Hashtable<String, Double> utp = new Hashtable<>();
		Hashtable<String, Double> utn = new Hashtable<>();
		Hashtable<String, Double> ufp = new Hashtable<>();
		Hashtable<String, Double> ufn = new Hashtable<>();

		for (CSUser user : idx.getUserindex().values()) {
			utp.put(user.getId(), 1.);
			utn.put(user.getId(), 0.);
			ufp.put(user.getId(), 0.);
			ufn.put(user.getId(), 0.);
		}

		CSUser gold = idx.getGolduser();

		for (CSUser user : idx.getUserindex().values()) {

			double tp = 0.0, tn = 0.0, fp = 0.0, fn = 0.0;
			double cnt = 0;
			for (Annotation annotation : user.getAnnotationsByThisUser().values()) {
				cnt++;
				CSObject object = annotation.getObject();

				HashSet<String> goldannotation = gold.getHerAnnotationForObject(object).getAtLevel(level);
				HashSet<String> userannotation = annotation.getAtLevel(level);

				HashSet<String> checked = new HashSet<>();
				for (String a : userannotation) {
					checked.add(a);
					if (goldannotation.contains(a)) {
						tp++;
					} else {
						fp++;
					}
				}

				for (String a : goldannotation) {
					if (checked.contains(a)) {
						continue;
					}
					if (!userannotation.contains(a)) {
						fn++;
					}
				}

			}

			double goodpart = (tp + tn) / (tp + fp + tn + fn);// [0-1]
			double badpart = (fp + fn) / (tp + fp + tn + fn);// [0-1]

			double weight = goodpart - badpart * 1.5;
			realuserprecision.put(user.getId(), weight);
		}

		for (CSUser user : idx.getUserindex().values()) {
			usertp.put(user.getId(), r.nextInt(20) / 100. + .8);
		}
		Hashtable<String, Double> pseudogroundtruth = new Hashtable<>();
		while (k-- > 0) {
			// usertp=realuserprecision;
			System.out.println("round:" + k);
			for (CSObject object : idx.getObjectindex().values()) {
				double pobject_sum = 0.0;
				double nobject_sum = 0.0;

				int pcnt = 0;
				int ncnt = 0;

				for (Annotation annotation : object.getUsers().values()) {

					double cutp = 0.0, cufp = 0.0, cutn = 0.0, cufn = 0.0;
					cutp = utp.get(annotation.getUser().getId());
					cutn = utn.get(annotation.getUser().getId());
					cufp = ufp.get(annotation.getUser().getId());
					cufn = ufn.get(annotation.getUser().getId());

					double goodpart = (cutp + cutn) / (cutp + cufp + cutn + cufn);// [0-1]
					double badpart = (cufp + cufn) / (cutp + cufp + cutn + cufn);// [0-1]

					double weight = goodpart - badpart * 1.5;

					if (annotation.getAtLevel(level).contains(answer)) {
						pobject_sum += weight;
						pcnt++;
					} else {
						nobject_sum += weight;
						ncnt++;
					}

				}

				if (pobject_sum > nobject_sum) {

					pseudogroundtruth.put(object.getId(), 1.);

				} else {
					pseudogroundtruth.put(object.getId(), 0.);
				}

				if (pcnt > 0)
					opprobs.put(object.getId(), pobject_sum / pcnt);
				if (ncnt > 0)
					onprobs.put(object.getId(), nobject_sum / ncnt);

			}
			// print("opprobs", opprobs,20);
			// print("onprobs",onprobs,20);

			for (CSUser user : idx.getUserindex().values()) {

				double tp = 0.0, tn = 0.0, fp = 0.0, fn = 0.0;

				for (Annotation annotation : user.getAnnotationsByThisUser().values()) {

					CSObject object = annotation.getObject();

					Double p = opprobs.get(object.getId());
					if (p == null)
						p = 0.;

					Double n = onprobs.get(object.getId());
					if (n == null)
						n = 0.;

					double comtroversity = 1. * Math.abs(p - n) / (p + n);

					Double gt = pseudogroundtruth.get(annotation.getObject().getId());

					if (annotation.getAtLevel(level).contains(answer)) {
						if (gt > 0) {
							tp++;
						}else
						{
							fp++;
						}
					} else {
						if (gt > 0) {
							fn++;
						}else
						{
							tn++;
						}
					}

				}

				utp.put(user.getId(), tp);
				utn.put(user.getId(), tn);
				utp.put(user.getId(), tp);
				utp.put(user.getId(), tp);

			}
			print("realuser", realuserprecision, usertp, 10);

		}

		return evaluateAll(level, answer, idx, values, utp, utn, ufp, ufn);

	}

	private void print(String string, Hashtable<String, Double> usertp, Hashtable<String, Double> usertp2, int max) {
		// if(true)return;
		System.out.print("Var {");
		for (String uid : usertp.keySet()) {
			System.out.print("(" + uid.substring(0, 5) + " r:" + Math.round(usertp.get(uid) * 100.) / 100. + " e:"
					+ Math.round(usertp2.get(uid) * 100.) / 100. + ") ");
			if (max-- < 0)
				break;
		}
		System.out.print("}");
		System.out.println();

	}

	private Hashtable<String, Double> evaluateAll(int level, String answer, ObjectIndex idx,
			Collection<CSObject> values, Hashtable<String, Double> utp, Hashtable<String, Double> utn,
			Hashtable<String, Double> ufp, Hashtable<String, Double> ufn) {

		double tp = 0.0, fp = 0.0, tn = 0.0, fn = 0.0;
		ArrayList<Double> pweights = new ArrayList<>();

		CSUser gold = idx.getGolduser();

		for (CSObject object : values) {
			double pos = 0, neg = 0.;

			ArrayList<Double> nweights = new ArrayList<>();
			HashSet<String> usersuggestions = new HashSet<>();
			for (Annotation annotation : object.getUsers().values()) {

				double cutp = 0.0, cufp = 0.0, cutn = 0.0, cufn = 0.0;
				cutp = utp.get(annotation.getUser().getId());
				cutn = utn.get(annotation.getUser().getId());
				cufp = ufp.get(annotation.getUser().getId());
				cufn = ufn.get(annotation.getUser().getId());

				double goodpart = (cutp + cutn) / (cutp + cufp + cutn + cufn);// [0-1]
				double badpart = (cufp + cufn) / (cutp + cufp + cutn + cufn);// [0-1]

				double weight = goodpart - badpart * 1.5;

				weight = goodpart * Math.abs(goodpart - badpart) / (goodpart + badpart);

				usersuggestions.addAll(annotation.getAtLevel(level));
				if (annotation.getAtLevel(level).contains(answer)) {

					// Double weight =0.;//=
					// usertp.get(annotation.getUser().getId());

					pweights.add(weight);
					pos += weight;

				} else {
					// Double weight =0.;//=
					// usertp.get(annotation.getUser().getId());
					nweights.add(weight);
					neg += weight;

				}

			}

			Annotation gannotation = gold.getAnnotationsByThisUser().get(object.getId());

			if (gannotation.getAtLevel(level).contains(answer)) {
				if (pos > neg) {
					tp++;
				} else {
					fn++;

				}
			} else {
				if (pos > neg) {
					fp++;
				} else {
					tn++;
				}

			}

		}

		Hashtable<String, Double> ret = new Hashtable<>();

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
