package uk.soton.cs.dataset;

import java.io.IOException;
import java.util.HashSet;

import org.json.simple.parser.ParseException;

import uk.soton.cs.inference.algorithms.MessagePassing;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class AlgorithmTester {
	
	public static void main(String[] args) {
		AlgorithmTester t=new AlgorithmTester();
		t.test();
	}

	private void test() {
		MessagePassing mp=new MessagePassing();
		
		Parser p=new Parser();
		try {
			
			ObjectIndex idx = p.loadIndex();
			
			CSObject object = idx.getObjectindex().values().iterator().next();
			
			mp.calculate(idx,idx.getObjectindex().values());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
