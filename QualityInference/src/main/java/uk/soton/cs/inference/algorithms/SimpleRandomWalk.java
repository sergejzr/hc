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

public class SimpleRandomWalk extends Algorithm {

	public SimpleRandomWalk() {
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
			
			Double precision = 1. * tp / (tp + fp);
			Double accuracy=(1. * (tp + tn) / (tp + tn + fp + fn));
			Double tgood= tp/(tp+fn);
			if(tgood.isNaN()) tgood=0.;
			
			Double fgood= tn/(fp+tn);
			if(tgood==0.0) tgood=fgood;
			if(fgood==0.0) fgood=tgood;
			
			if(tgood.isNaN()) fgood=0.;
			Double correctness=(tgood + fgood)/6;
			if(correctness.isNaN())
			{
				int y=0;
						y++;
			}
			realuserprecision.put(user.getId(), correctness);
			
			
		}
		
	
		for (CSUser user : idx.getUserindex().values()) {
			usertp.put(user.getId(),r.nextInt(20)/100.+.8);
		}
		while (k-- > 0) {
		//	usertp=realuserprecision;
			System.out.println("round:"+k);
			for (CSObject object : idx.getObjectindex().values()) {
				double pobject_sum = 0.0;
				double nobject_sum = 0.0;

				int pcnt = 0;
				int ncnt = 0;
				for (Annotation annotation : object.getUsers().values()) {



					if (annotation.getAtLevel(level).contains(answer)) {
						pobject_sum += usertp.get(annotation.getUser().getId());
						pcnt++;
					} else {
						nobject_sum+= usertp.get(annotation.getUser().getId());
						ncnt++;
					}
					
				}
				if(pcnt>0)
				opprobs.put(object.getId(), pobject_sum / pcnt);
				if(ncnt>0)
				onprobs.put(object.getId(), nobject_sum / ncnt);

			}
//print("opprobs", opprobs,20);
//print("onprobs",onprobs,20);
			
			for (CSUser user : idx.getUserindex().values()) {

				double tp = 0.0, tn = 0.0, fp = 0.0, fn = 0.0;
				int pcnt = 0,ncnt=0;
				for (Annotation annotation : user.getAnnotationsByThisUser().values()) {
					
					CSObject object = annotation.getObject();

					Double p = opprobs.get(object.getId());if(p==null) p=0.;
					
					Double n = onprobs.get(object.getId());if(n==null) n=0.;

					double comtroversity =  1.*Math.abs(p - n) / (p + n);
if(user.getId().equals("50c6079b9177d0288b002201"))
{
int u=0;
u++;
}
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

				double precision = 1. * tp / (tp + fp);
				Double accuracy=(1. * (tp + tn) / (tp + tn + fp + fn));
				Double correctness=(tp/(tp+fn) + tn/(fp+tn))/2;
				//if(pcnt>0)
				if(correctness.isNaN())
				{
				continue;
				}
				usertp.put(user.getId(), correctness);
				
			}
			print("realuser",realuserprecision,usertp,10);
	

		}

		return evaluateAll(level, answer, idx, values, realuserprecision);

	}

	private void print(String string, Hashtable<String, Double> usertp, Hashtable<String, Double> usertp2, int max) {
		//if(true)return;
System.out.print("Var {");
for(String uid:usertp.keySet())
{
	System.out.print("("+uid.substring(0,5)+" r:"+Math.round(usertp.get(uid)*100.)/100.+" e:"+Math.round(usertp2.get(uid)*100.)/100.+") ");
	if(max--<0) break;
}
System.out.print("}");
System.out.println();

		
	}

	private Hashtable<String, Double> evaluateAll(int level, String answer, ObjectIndex idx,
			Collection<CSObject> values, Hashtable<String, Double> usertp) {

		double tp = 0.0, fp = 0.0, tn = 0.0, fn = 0.0;

		CSUser gold = idx.getGolduser();

		for (CSObject object : values) {
			double pos = 0, neg = 0.;

ArrayList<Double> pweights=new ArrayList<>();
ArrayList<Double> nweights=new ArrayList<>();
			HashSet<String> usersuggestions=new HashSet<>();
			for (Annotation annotation : object.getUsers().values()) {
				
				usersuggestions.addAll(annotation.getAtLevel(level));
				if (annotation.getAtLevel(level).contains(answer)) {
					
					Double weight = usertp.get(annotation.getUser().getId());
					pweights.add(weight);
					pos+=weight;
				
				} else {
					Double weight = usertp.get(annotation.getUser().getId());
					nweights.add(weight);
					neg+=weight;
				
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
