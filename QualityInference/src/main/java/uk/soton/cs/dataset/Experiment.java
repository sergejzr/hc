package uk.soton.cs.dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.json.simple.parser.ParseException;

import uk.soton.cs.inference.algorithms.ExtendedSimpleRandomWalk;
import uk.soton.cs.inference.algorithms.GroundTruthBased;
import uk.soton.cs.inference.algorithms.MessagePassing;
import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;
import uk.soton.sys.WorkerThreadFactory;

public class Experiment implements AlgoTester{

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("arg missing: file");
			return;
		}
		
		Experiment t = new Experiment();
	
		//t.addAlgorithm(new MessagePassing());
		t.addAlgorithm(new MajorityVoitng());
	//	t.addAlgorithm(new SimpleRandomWalk());
	//	t.addAlgorithm(new GroundTruthBased());
		t.addAlgorithm(new ExtendedSimpleRandomWalk());

		
		
		
		
		
		
		
		ArrayList<AlgoThread> mythreads=new ArrayList<>();
		
		for(Integer k:new Integer[]{10}){
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


	ArrayList<Algorithm> algorithms=new ArrayList<>();
	
	private void addAlgorithm(Algorithm alg) {
		
		algorithms.add(alg);
		
	}

	public Hashtable<String, AlgoSummary> test(String[] args,Integer k) {
		
		
		int level = 0;
		Hashtable<String, AlgoSummary> result=new Hashtable<>();
		
		Parser p = new Parser();
		try{
		ObjectIndex idx = p.loadIndex(new File(args[0]),new File(args[1]));
		
		HashSet<String> alllables=new HashSet<>();
		alllables.addAll(idx.getAllPredictedLables(level));
		alllables.addAll(idx.getGoldLabels(level));
		
		for(Algorithm algo:algorithms)
		{
			Hashtable<String, Double> averages = new Hashtable<>();
			ArrayList<String> lablesincluded=new ArrayList<>();
			for (String lable : alllables) {
				if(!lable.contains("beest")){continue;}
				lablesincluded.add(lable);
			Hashtable<String, Double> mpres;
			mpres = algo.calculate(idx, idx.getObjectindex().values(), level, lable,k);
			addAverage(mpres, averages);
			}
			
			
			double tp = averages.get("tp"), tn = averages.get("tn"), fp = averages.get("fp"),
					fn = averages.get("fn");
			
			

			double precision = 1. * tp / (tp + fp);
			double recall = 1. * tp / (tp + fn);
			
			Hashtable<String, Double> summary = new Hashtable<>();
			summary.put("tp", (double) tp);
			summary.put("tn", (double) tn);
			summary.put("fp", (double) fp);
			summary.put("fn", (double) fn);
			summary.put("Accuracy", (1. * (tp + tn) / (tp + tn + fp + fn)));
			summary.put("Precision", precision);
			summary.put("Recall", recall);
			summary.put("F1", 2 * ((precision * recall) / (precision + recall)));
			
			AlgoSummary mvs=new AlgoSummary();
			
			for(String measure:summary.keySet())
			{ 
				mvs.addMeasure(measure,summary.get(measure));
			}
			result.put(algo.getName()+" "//+lablesincluded
					, mvs);
		}
		
		
		
		
		

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

	
}
