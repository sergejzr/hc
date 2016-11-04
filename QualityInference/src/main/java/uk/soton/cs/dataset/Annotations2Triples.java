package uk.soton.cs.dataset;

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

		FileWriter fw=new FileWriter(out);
	
		
		
		
		//BufferedReader br = new BufferedReader(new FileReader(index1));
		//Pattern p = Pattern.compile("\" ObjectId");
		//Matcher matcher = p.matcher(line = br.readLine());

		int start = 0;

		while (sc.hasNext()) {
ArrayList<Triple> triples=new ArrayList<>();
			//int end = matcher.start();
			String objline = sc.next();// line.substring(start, end);
			// System.out.println(objline);
			Pattern pobject = Pattern.compile("'(.*?)['\"]:");
			Matcher matcherobj = pobject.matcher(objline);
			if(!matcherobj.find())
			{
				 pobject = Pattern.compile("(.*?)['\"]:");
				 matcherobj = pobject.matcher(objline);
				 matcherobj.find();
			}

			String uid = matcherobj.group(1);
			
			if(uid.trim().length()==0)
			{
				int y=0;
				y++;
			}
			String annotations = objline.substring(matcherobj.end());

			Pattern pannotations = Pattern.compile("\\((.*?)\\)");
			Matcher matcherannotations = pannotations.matcher(annotations);

			
			while (matcherannotations.find()) {

				Pattern objectid=Pattern.compile("u'(.*?)'");
				matcherobj = objectid.matcher(matcherannotations.group(1));
				if(!matcherobj.find())
				{
					int y=0;
					y++;
				}
				String objid = matcherobj.group(1);
				
				
				StringBuilder sb=new StringBuilder();
				boolean first=true;
				while (matcherobj.find()) {
					String annotationstr = (matcherobj.group(1));
					
					if(!first)
						sb.append(",");
					sb.append(annotationstr);
					first=false;
				
				}
			//	triples.add(new Triple(uid, objid, sb.toString()));
				
					String linex=uid+"\t"+objid+"\t"+sb.toString()+"\n";
				fw.write(linex);
			//	System.out.println(linex);
			}

		
			

		}
		sc.close();
		
	}

}
