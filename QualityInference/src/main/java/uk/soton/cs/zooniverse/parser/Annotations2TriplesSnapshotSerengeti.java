package uk.soton.cs.zooniverse.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
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

import uk.soton.cs.dataset.Triple;
import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class Annotations2TriplesSnapshotSerengeti {


	public Annotations2TriplesSnapshotSerengeti() {
		
	}


	public static void main(String[] args) {
		Annotations2TriplesSnapshotSerengeti p = new Annotations2TriplesSnapshotSerengeti();
		try {
			p.convert(new File(args[0]),new File(args[1]));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void convert(File in, File out) throws IOException, ParseException {
		
		String line=null;
		FileWriter fw=new FileWriter(out);
		fw.write("user_id\tobject_id\tannottion_path\ttimestamp\n");
		BufferedReader br = new BufferedReader(new FileReader(in));
		Pattern p = Pattern.compile("\" ObjectId");
		Matcher matcher = p.matcher(line = br.readLine());

		int start = 0;

		while (matcher.find()) {

			int end = matcher.start();
			String objline = line.substring(start, end);
			// System.out.println(objline);
			Pattern pobject = Pattern.compile("'(.*?)['\"]");
			Matcher matcherobj = pobject.matcher(objline);
			matcherobj.find();

			String uid = matcherobj.group(1);

		
			String annotations = objline.substring(matcherobj.end());

			Pattern pannotations = Pattern.compile("\\((.*?)\\)");
			Matcher matcherannotations = pannotations.matcher(annotations);

			while (matcherannotations.find()) {
				
				


				matcherobj = pobject.matcher(matcherannotations.group(1));
				matcherobj.find();
				String objid = matcherobj.group(1);



				int level = 0;
				StringBuilder sb=new StringBuilder();
				boolean first=true;
				while (matcherobj.find()) {
					String annotationstr = (matcherobj.group(1));
					
					if(!first)
						sb.append(",");
					sb.append(annotationstr);
					first=false;
				
				}
			
				
				fw.write(uid);
				fw.write("\t");
				fw.write(objid);
				fw.write("\t");
				fw.write(sb.toString());
				fw.write("\t");
				fw.write("\n");
			}

		
			
			start = end;
		}
		
		fw.close();
		br.close();

		
	}

}
