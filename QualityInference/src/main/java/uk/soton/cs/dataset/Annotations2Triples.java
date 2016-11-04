package uk.soton.cs.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.parser.ParseException;

import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class Annotations2Triples {


	public Annotations2Triples() {
		
	}


	public static void main(String[] args) {
		Annotations2Triples p = new Annotations2Triples();
		try {
			p.loadIndex(new File(args[0]),new File(args[1]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadIndex(File in, File out) throws IOException, ParseException {
	
		String line;

		//useridx.put(goldUser.getId(), goldUser);
		
	//	Scanner sc=new Scanner(new FileInputStream(index1)).useDelimiter(":\\s\\[");
		Scanner sc=new Scanner(new FileInputStream(in)).useDelimiter(",\\s'");
		int j=0;

		
	
		
		
		
		//BufferedReader br = new BufferedReader(new FileReader(index1));
		//Pattern p = Pattern.compile("\" ObjectId");
		//Matcher matcher = p.matcher(line = br.readLine());

		int start = 0;

		while (sc.hasNext()) {
ArrayList<String> triple=new ArrayList<>();
			//int end = matcher.start();
			String objline = sc.next();// line.substring(start, end);
			// System.out.println(objline);
			Pattern pobject = Pattern.compile("'(.*?)['\"]");
			Matcher matcherobj = pobject.matcher(objline);
			matcherobj.find();

			String uid = matcherobj.group(1);
			triple.add(uid);
			String annotations = objline.substring(matcherobj.end());

			Pattern pannotations = Pattern.compile("\\((.*?)\\)");
			Matcher matcherannotations = pannotations.matcher(annotations);

			while (matcherannotations.find()) {

				matcherobj = pobject.matcher(matcherannotations.group(1));
				matcherobj.find();
				String objid = matcherobj.group(1);
				triple.add(objid);
				
				StringBuilder sb=new StringBuilder();
				while (matcherobj.find()) {
					String annotationstr = (matcherobj.group(1));
					sb.append(annotationstr);
					sb.append(",");
				
				}
			}

		 System.out.println(triple);

			

		}
		
		
	}

}
