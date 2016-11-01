package uk.soton.cs.dataset;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.json.simple.parser.ParseException;

import uk.soton.cs.dataset.Parser;
import uk.soton.cs.inference.algorithms.MessagePassing;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class AlgorithmTester {
	
	
	public static void main(String[] args) {
		if(args.length<1)
		{
			System.out.println("arg missing: file");
			return;
		}
		AlgorithmTester t=new AlgorithmTester();
		t.test(args);
	}

	private void test(String[] args) {
		MessagePassing mp=new MessagePassing();
		
		Parser p=new Parser(new File(args[0]));
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
