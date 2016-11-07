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

public class RandomWalk extends Algorithm {

	public RandomWalk() {
		super("RW");

	}

	@Override
	public Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> values, int level, String answer,
			Integer k) {

		Hashtable<String, Double> opprobs = new Hashtable<>();
		Hashtable<String, Double> onprobs = new Hashtable<>();

		
		Hashtable<String, Double> usertp = new Hashtable<>();
		Hashtable<String, Double> usertn = new Hashtable<>();
		Random r = new Random();
		
		Hashtable<String, Double> realusertp = new Hashtable<>();
		Hashtable<String, Double> realusertn = new Hashtable<>();
		Hashtable<String, Double> realuserfp = new Hashtable<>();
		Hashtable<String, Double> realuserfn = new Hashtable<>();
		
		CSUser gold = idx.getGolduser();
		
		
		for (CSUser user : idx.getUserindex().values()) {

			double tp = 0.0, tn = 0.0, fp = 0.0, fn = 0.0;
			double cnt=0;
			for (Annotation annotation : user.getAnnotationsByThisUser().values()) {
				cnt++;
				CSObject object = annotation.getObject();

				
				Annotation an = gold.getHerAnnotationForObject(object);
				if(an.getAtLevel(level).contains(answer))
				{
					if(annotation.getAtLevel(level).contains(answer))
					{
						tp++;
					}else
					{
						fn++;
					}
				}else
				{

					if(annotation.getAtLevel(level).contains(answer))
					{
						fp++;
					}else
					{
						tn++;
					}
				}
				
			}
			realuserfn.put(user.getId(), fn/cnt);
			realusertp.put(user.getId(), tp/cnt);
			realusertn.put(user.getId(), tn/cnt);
			realuserfp.put(user.getId(), fp/cnt);
		}
		
	
		for (CSUser user : idx.getUserindex().values()) {
			
			usertp.put(user.getId(), .5);
			usertn.put(user.getId(), .5);
		}
		while (k-- > 0) {
			System.out.println("round:"+k);
			for (CSObject object : idx.getObjectindex().values()) {
				double pobject_sum = 0.0;
				double nobject_sum = 0.0;

				int pcnt = 0;
				int ncnt = 0;
				for (Annotation annotation : object.getUsers().values()) {
					// double
					// uservalue=uprobs.get(annotation.getUser().getId());

					if (annotation.getAtLevel(level).contains(answer)) {
						pobject_sum += usertp.get(annotation.getUser().getId());
						pcnt++;
					} else {
						nobject_sum += usertn.get(annotation.getUser().getId());
						ncnt++;
					}
					
				}
				if(pcnt>0)
				opprobs.put(object.getId(), pobject_sum / pcnt);
				if(ncnt>0)
				onprobs.put(object.getId(), nobject_sum / ncnt);

			}
print("opprobs", opprobs,20);
print("onprobs",onprobs,20);
			
			for (CSUser user : idx.getUserindex().values()) {

				double tp = 0.0, tn = 0.0, fp = 0.0, fn = 0.0;
				int pcnt = 0,ncnt=0;
				for (Annotation annotation : user.getAnnotationsByThisUser().values()) {
					
					CSObject object = annotation.getObject();

					Double p = opprobs.get(object.getId());if(p==null) p=0.;
					
					Double n = onprobs.get(object.getId());if(n==null) n=0.;

					double comtroversity =  1.*Math.abs(p - n) / (p + n);

					if (annotation.getAtLevel(level).contains(answer)) {
						if (p > n) {

							tp += comtroversity;
							pcnt++;
						} else {

							fp += comtroversity;
						}
					} else {
						if (p > n) {
							fn += comtroversity;
						} else {
							tn += comtroversity;
							ncnt++;
						}
					}

				}

				if(pcnt>0)
				usertp.put(user.getId(), tp / pcnt);
				if(ncnt>0)
				usertn.put(user.getId(), tn / ncnt);
			}
			print("usertp",usertp,5);
			print("usertn",usertn,5);
		}

		return evaluateAll(level, answer, idx, values, usertp, usertn);

	}

	private void print(String string, Hashtable<String, Double> usertp, int max) {
		
System.out.print("Var "+string+"{");
for(String uid:usertp.keySet())
{
	System.out.print("("+uid+":"+usertp.get(uid)+") ");
	if(max--<0) break;
}
System.out.print("}");
System.out.println();

		
	}

	private Hashtable<String, Double> evaluateAll(int level, String answer, ObjectIndex idx,
			Collection<CSObject> values, Hashtable<String, Double> usertp, Hashtable<String, Double> usertn) {

		double tp = 0.0, fp = 0.0, tn = 0.0, fn = 0.0;

		CSUser gold = idx.getGolduser();

		for (CSObject object : values) {
			double pos = 0, neg = 0.;
int cntpos=0,cntneg=0;
ArrayList<Double> pweights=new ArrayList<>();
ArrayList<Double> nweights=new ArrayList<>();
			HashSet<String> usersuggestions=new HashSet<>();
			for (Annotation annotation : object.getUsers().values()) {
				
				usersuggestions.addAll(annotation.getAtLevel(level));
				if (annotation.getAtLevel(level).contains(answer)) {
					
					Double weight = usertp.get(annotation.getUser().getId());
					pweights.add(weight);
					pos+=weight;
					cntpos++;
				} else {
					Double weight = usertn.get(annotation.getUser().getId());
					nweights.add(weight);
					neg+=weight;
					cntneg++;
				}

			}

			Annotation gannotation = gold.getAnnotationsByThisUser().get(object.getId());

			if (gannotation.getAtLevel(level).contains(answer)) {
				if (pos > neg) {
					tp++;
				}else
				{
					fn++;
					
				}
			} else {
				if (pos > neg) {
					fp++;
				}else
				{
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
