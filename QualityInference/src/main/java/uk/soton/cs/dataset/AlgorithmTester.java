package uk.soton.cs.dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.json.simple.parser.ParseException;

import uk.soton.cs.inference.algorithms.MessagePassing;
import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;
import uk.soton.cs.zooniverse.parser.Parser;
import uk.soton.sys.WorkerThreadFactory;

public class AlgorithmTester implements AlgoTester{

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("arg missing: file");
			return;
		}
		AlgorithmTester t = new AlgorithmTester();
		
		ArrayList<AlgoThread> mythreads=new ArrayList<>();
		
		for(Integer k:new Integer[]{0,1,2,3,5,10,20,30,40,50,100}){
		AlgoThread at=new AlgoThread(t,args, k);
		mythreads.add(at);
		at.start();
		}
		
		for(AlgoThread at:mythreads)
		{
			
			 synchronized(at){
		            try{
		                System.out.println("Waiting for jobs to complete...");
		                at.wait();
		            }catch(InterruptedException e){
		                e.printStackTrace();
		            }
			 }
			AlgorithmResult res = at.getResult();
			
			System.out.println(res.toString());
			
			
		}
		
		System.out.println("All done");
		
		
		
		
		
		
	}

	public Hashtable<String, AlgoSummary> test(String[] args,Integer k) {
		
		
		
		Hashtable<String, AlgoSummary> result=new Hashtable<>();
		
		
		
		MessagePassing mp = new MessagePassing();
		MajorityVoitng mv = new MajorityVoitng();
		// String answer = "zebra";
		int level = 0;
		Parser p = new Parser();
		Hashtable<String, Double> mpaverages = new Hashtable<>();
		Hashtable<String, Double> mvaverages = new Hashtable<>();

		try {

			ObjectIndex idx = p.loadIndex(new File(args[0]),new File(args[1]));

			// idx.getGoldLabels(level);

			// idx.toARFF(new File("serengeti.arff"), 0);
			// if(true) return;

			// CSObject object =
			// idx.getObjectindex().values().iterator().next();
			// Hashtable<String, Integer> cnts=new Hashtable<>();
			HashSet<String> alllables=new HashSet<>();
			alllables.addAll(idx.getAllPredictedLables(level));
			alllables.addAll(idx.getGoldLabels(level));
			
			for (String lable : alllables) {
		//	 if(!lable.contains("bees")) continue;
			//	if(!lable.contains("lion")) continue;
			//	System.out.println("calculate for " + lable);
				int cntall = 0;
				HashSet<String> objects = new HashSet<>();
				for (Annotation annotation : idx.getGolduser().getAnnotationsByThisUser().values()) {
					if (annotation.getAtLevel(level).contains(lable)) {
						objects.add(annotation.getObject().getId());
						cntall++;
					}
				}
				//System.out.println("------------------------------------------------\nMP");
				// idx.getGolduser().getObjects().get(key)
			//	System.out.println("overall should be " + cntall + " instances of " + lable + " in:" + objects);

				Hashtable<String, Double> mpres;
				print(mpres = mp.calculate(idx, idx.getObjectindex().values(), level, lable,k));
				addAverage(mpres, mpaverages);
				//System.out.println("\nMV");
				Hashtable<String, Double> mvres;
				print(mvres = mv.calculate(idx, idx.getObjectindex().values(), level, lable,k));
				addAverage(mvres, mvaverages);
			}

			double tp = mvaverages.get("tp"), tn = mvaverages.get("tn"), fp = mvaverages.get("fp"),
					fn = mvaverages.get("fn");

			double precision = 1. * tp / (tp + fp);
			double recall = 1. * tp / (tp + fn);
			Hashtable<String, Double> mvsummary = new Hashtable<>();
			mvsummary.put("tp", (double) tp);
			mvsummary.put("tn", (double) tn);
			mvsummary.put("fp", (double) fp);
			mvsummary.put("fn", (double) fn);
			mvsummary.put("Accuracy", (1. * (tp + tn) / (tp + tn + fp + fn)));
			mvsummary.put("Precision", precision);
			mvsummary.put("Recall", recall);
			mvsummary.put("F1", 2 * ((precision * recall) / (precision + recall)));

			 tp = mpaverages.get("tp");
			 tn = mpaverages.get("tn");
			 fp = mpaverages.get("fp");
			 fn = mpaverages.get("fn");

			 precision = 1. * tp / (tp + fp);
			 recall = 1. * tp / (tp + fn);
			Hashtable<String, Double> mpsummary = new Hashtable<>();
			mpsummary.put("tp", (double) tp);
			mpsummary.put("tn", (double) tn);
			mpsummary.put("fp", (double) fp);
			mpsummary.put("fn", (double) fn);
			mpsummary.put("Accuracy", (1. * (tp + tn) / (tp + tn + fp + fn)));
			mpsummary.put("Precision", precision);
			mpsummary.put("Recall", recall);
			mpsummary.put("F1", 2 * ((precision * recall) / (precision + recall)));
			
			
			AlgoSummary mvs=new AlgoSummary();
			
			for(String measure:mvsummary.keySet())
			{ 
				mvs.addMeasure(measure,mvsummary.get(measure));
			}
			
			result.put("MV", mvs);
			
			
			AlgoSummary mps=new AlgoSummary();
			
			for(String measure:mpsummary.keySet())
			{ 
				mps.addMeasure(measure,mpsummary.get(measure));
			}
			
			result.put("MP", mps);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
	}

	private void addAverage(Hashtable<String, Double> mpres, Hashtable<String, Double> mpaverages) {
		for (String s : mpres.keySet()) {
			Double d = mpaverages.get(s);
			if (d == null) {
				d = 0.0;
			}
			if (d.isInfinite() || d.isNaN()) {
				continue;
			}
			mpaverages.put(s, d + mpres.get(s));
		}

	}

	private void print(Hashtable<String, Double> calculate) {
		if(true)
		return;
		for (String s : calculate.keySet()) {
			System.out.println(s + ": " + calculate.get(s));
		}

	}
}
