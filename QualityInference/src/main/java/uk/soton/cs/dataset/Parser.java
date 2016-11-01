package uk.soton.cs.dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
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

public class Parser {
	File maindir = new File("/home/zerr/git/QualityAssessment/experiments/SnapshotSerengeti");

	public Parser(File file) {
		maindir=file;
	}

	public Parser() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Parser p = new Parser();
		try {
			p.loadIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ObjectIndex loadIndex() throws IOException, ParseException {
		File index1 = new File(maindir, "overlap_serengeti_answers_userbased.txt");

		String line;

		CSUser goldUser = new CSUser("GoldUser");
		Hashtable<String, CSObject> objectidx = new Hashtable<>();
		Hashtable<String, CSUser> useridx = new Hashtable<>();
		//useridx.put(goldUser.getId(), goldUser);
		BufferedReader br = new BufferedReader(new FileReader(index1));
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

			CSUser user = useridx.get(uid);
			if (user == null) {
				useridx.put(uid, user = new CSUser(uid));
			}

			String annotations = objline.substring(matcherobj.end());

			Pattern pannotations = Pattern.compile("\\((.*?)\\)");
			Matcher matcherannotations = pannotations.matcher(annotations);

			while (matcherannotations.find()) {

				matcherobj = pobject.matcher(matcherannotations.group(1));
				matcherobj.find();
				String objid = matcherobj.group(1);
				CSObject object = objectidx.get(objid);
				if (object == null) {
					objectidx.put(objid, object = new CSObject(objid));
				}
				Annotation annotation = new Annotation(object, user);

				object.addAnnotation(annotation);
				user.addAnnotation(annotation);

				int level = 0;
				while (matcherobj.find()) {
					String annotationstr = (matcherobj.group(1));
					// LevelAnnotation levelannotation=new
					// LevelAnnotation(level, annotationstr);
					annotation.addLevel(level, annotationstr);

					// user.addAnotation(object,annotation);

					level++;
				}
			}

			// System.out.println(annotations);

			start = end;

		}
		br.close();
		ObjectIndex idx = new ObjectIndex(objectidx, useridx);

		CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter(',').withQuote('"');
		FileReader fileReader = new FileReader(new File(maindir, "gold_standard_data_snapshotSerengati.csv"));
		CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);

		Iterator<CSVRecord> it = csvFileParser.iterator();
		while (it.hasNext()) {
			CSVRecord cur = it.next();
			String objectid = cur.get(0);

			CSObject object = idx.getObject(objectid);

			Annotation goldannotation = new Annotation(object, goldUser);
			//object.addAnnotation(goldannotation);
			goldUser.addAnnotation(goldannotation);

			for (int i = 2; i < cur.size(); i++) {
				goldannotation.addLevel(i-2, cur.get(i));
			}
			
				
		}
		idx.setGoldUser(goldUser);
		csvFileParser.close();
		return idx;
	}

}
