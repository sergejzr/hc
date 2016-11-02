package uk.soton.cs.dataset;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;

import org.json.simple.parser.ParseException;

import uk.soton.cs.inference.algorithms.MessagePassing;
import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class AlgorithmTester {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("arg missing: file");
			return;
		}
		AlgorithmTester t = new AlgorithmTester();
		t.test(args);
	}

	HashSet<String> getAllLables(ObjectIndex idx, int level) {
		HashSet<String> ret = new HashSet<>();
		for (CSObject object : idx.getObjectindex().values()) {

			for (Annotation annotation : object.getUsers().values()) {
				ret.addAll(annotation.getAtLevel(level));
			}

		}

		return ret;
	}

	private void test(String[] args) {
		MessagePassing mp = new MessagePassing();
		MajorityVoitng mv = new MajorityVoitng();
		// String answer = "zebra";
		int level = 0;
		Parser p = new Parser(new File(args[0]));
		Hashtable<String, Double> mpaverages = new Hashtable<>();
		Hashtable<String, Double> mvaverages = new Hashtable<>();

		try {

			ObjectIndex idx = p.loadIndex();
		//	idx.toARFF(new File("serengeti.arff"), 0);
			// if(true) return;
			HashSet<String> lables = getAllLables(idx, 0);
			// CSObject object =
			// idx.getObjectindex().values().iterator().next();
//Hashtable<String, Integer> cnts=new Hashtable<>();
			for (String lable : lables) {
				//if(!lable.startsWith("lion")) continue;
				System.out.println("calculate for " + lable);
				int cntall = 0;
				HashSet<String> objects=new HashSet<>();
				for (Annotation annotation : idx.getGolduser().getObjects().values()) {
					if (annotation.getAtLevel(0).contains(lable)) {
						objects.add(annotation.getObject().getId());
						cntall++;
					}
				}
				System.out.println("------------------------------------------------\nMP");
				// idx.getGolduser().getObjects().get(key)
				System.out.println("overall should be " + cntall + " specie of " + lable+" in:"+objects);
				

				Hashtable<String, Double> mpres;
				print(mpres = mp.calculate(idx, idx.getObjectindex().values(), 0, lable));
				addAverage(mpres, mpaverages);
				System.out.println("\nMV");
				Hashtable<String, Double> mvres;
				print(mvres = mv.calculate(idx, idx.getObjectindex().values(), 0, lable));
				addAverage(mvres, mvaverages);
			}

			for (String s : mpaverages.keySet()) {
				System.out.println(s + ": " + "MP:" + mpaverages.get(s) / idx.getObjectindex().size() + " MV:"
						+ mvaverages.get(s) / idx.getObjectindex().size());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addAverage(Hashtable<String, Double> mpres, Hashtable<String, Double> mpaverages) {
		for (String s : mpres.keySet()) {
			Double d = mpaverages.get(s);
			if (d == null) {
				d = 0.0;
			}
			if( d.isInfinite() || d.isNaN())
			{
				continue;
			}
			mpaverages.put(s, d + mpres.get(s));
		}

	}

	private void print(Hashtable<String, Double> calculate) {
		for (String s : calculate.keySet()) {
			System.out.println(s + ": " + calculate.get(s));
		}

	}
}
