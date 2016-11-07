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

public class GroundTruthBased extends Algorithm {

	public GroundTruthBased() {
		super("GTB");

	}

	@Override
	public Hashtable<String, Double> calculate(ObjectIndex idx, Collection<CSObject> values, int level, String answer,
			Integer k) {

		Hashtable<String, Double> opprobs = new Hashtable<>();
		Hashtable<String, Double> onprobs = new Hashtable<>();

		
		Hashtable<String, Double> usertp = new Hashtable<>();
		
		Random r = new Random();
		
		Hashtable<String, Double> realuserprecision = new Hashtable<>();

		
		CSUser golduser = idx.getGolduser();
		
		Hashtable<String, Double> utp = new Hashtable<>();
		Hashtable<String, Double> utn = new Hashtable<>();
		Hashtable<String, Double> ufp = new Hashtable<>();
		Hashtable<String, Double> ufn = new Hashtable<>();
		
		for (CSUser user : idx.getUserindex().values()) {

			double tp = 0.0, tn = 0.0, fp = 0.0, fn = 0.0;
			double cnt=0;
			for (Annotation annotation : user.getAnnotationsByThisUser().values()) {
				cnt++;
				CSObject object = annotation.getObject();

				
				 HashSet<String> goldannotation = golduser.getHerAnnotationForObject(object).getAtLevel(level);
				 HashSet<String> userannotation = annotation.getAtLevel(level);
				
				 HashSet<String> checked=new HashSet<>();
				 for(String a:userannotation)
				 {
					 checked.add(a);
					 if(goldannotation.contains(a))
					 {
						 tp++;
					 }else
					 {
						 fp++;
					 }
				 }
				 
				 for(String a:goldannotation)
				 {
					 if(checked.contains(a)){continue;}
					 if(!userannotation.contains(a))
					 {
						 fn++;
					 }
				 }
				
					
				
				
				
				
			}
			if(user.getId().equals("50ed7a955aadce5ad50006f6"))
			{
				int howmanydone=user.getAnnotationsByThisUser().size();
				int y=0;
				y++;
				
				
			}
			utp.put(user.getId(),tp);
			utn.put(user.getId(),tn);
			ufp.put(user.getId(),fp);
			ufn.put(user.getId(),fn);
			

			
			
		}
		
	

		

		return evaluateAll(level, answer, idx, values, utp,utn,ufp,ufn);

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
			Collection<CSObject> values, Hashtable<String, Double> utp, Hashtable<String, Double> utn, Hashtable<String, Double> ufp, Hashtable<String, Double> ufn) {

		double tp = 0.0, fp = 0.0, tn = 0.0, fn = 0.0;
		ArrayList<Double> pweights=new ArrayList<>();

		CSUser gold = idx.getGolduser();

		for (CSObject object : values) {
			double pos = 0, neg = 0.;
			
ArrayList<Double> nweights=new ArrayList<>();
			HashSet<String> usersuggestions=new HashSet<>();
			for (Annotation annotation : object.getUsers().values()) {
				
				double cutp = 0.0, cufp = 0.0, cutn = 0.0, cufn = 0.0;
				cutp=utp.get(annotation.getUser().getId());
				cutn=utn.get(annotation.getUser().getId());
				cufp=ufp.get(annotation.getUser().getId());
				cufn=ufn.get(annotation.getUser().getId());
				
				double goodpart = (cutp+cutn)/(cutp+cufp+cutn+cufn);//[0-1]
				double badpart = (cufp+cufn)/(cutp+cufp+cutn+cufn);//[0-1]
				
				double weight=goodpart-badpart*1.5;
				
				weight=goodpart*Math.abs(goodpart - badpart) / (goodpart + badpart);
				
				usersuggestions.addAll(annotation.getAtLevel(level));
				if (annotation.getAtLevel(level).contains(answer)) {
					
				//	Double weight =0.;//= usertp.get(annotation.getUser().getId());
					
					pweights.add(weight);
					pos+=weight;
				
				} else {
					//Double weight =0.;//= usertp.get(annotation.getUser().getId());
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
