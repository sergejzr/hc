package uk.soton.cs.zooniverse.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.parser.ParseException;

import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

/**
 * Parses a csv file into object-user-annotation index, ready for applying
 * QualityInference algorithms. Additionally a csv file with gold data can be
 * supplied
 * 
 * @author amber, zerr
 *
 */
public class AnnotationLogParser {

	public AnnotationLogParser() {

	}

	public static void main(String[] args) {
		AnnotationLogParser p = new AnnotationLogParser();
		try {
			File goldfile = args.length > 1 ? new File(args[1]) : null;
			ObjectIndex idx = p.loadIndex(new File(args[0]), goldfile);
			System.out.println(idx.getObjectindex().values().iterator().next());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param annotationlog
	 *            - A tab separated csv file with the columns: user_id object_id
	 *            annottion_path timestamp<br/>
	 *            annotations is a comma separated annotation path;.
	 * @param goldstandard
	 *            - a comma separated gold standard csv fila a sprovided e.g. by
	 *            Zooniverse with the columns:
	 *            CaptureEventID,NumSpecies,Species,Count<br/>
	 *            where CaptureEventID is the object id
	 * @return index, ready for applying QualityInference algorithms
	 * @throws IOException
	 * @throws ParseException
	 */
	public ObjectIndex loadIndex(File annotationlog, File goldstandard) throws IOException, ParseException {

		CSUser goldUser = new CSUser("GoldUser");
		Hashtable<String, CSObject> objectidx = new Hashtable<>();
		Hashtable<String, CSUser> useridx = new Hashtable<>();

		CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter('\t');
		FileReader fileReader = new FileReader(annotationlog);
		CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
		Iterator<CSVRecord> it = csvFileParser.iterator();
		while (it.hasNext()) {
			CSVRecord cur = it.next();
			String usertid = cur.get(0);
			String objectid = cur.get(1);
			String annotations = cur.get(2);
			String time = null;
			if (cur.size() > 3) {
				time = cur.get(3);
			}

			CSObject object = objectidx.get(objectid);
			if (object == null) {
				objectidx.put(objectid, object = new CSObject(objectid));
			}
			CSUser user = useridx.get(usertid);
			if (user == null) {
				useridx.put(usertid, user = new CSUser(usertid));
			}

			Annotation annotation = new Annotation(object, user);
			annotation.addTimestamp(time);
			object.addAnnotation(annotation);
			user.addAnnotation(annotation);

			String annotationarr[] = annotations.split(",");
			for (int i = 0; i < annotationarr.length; i++) {
				annotation.addLevel(i, annotationarr[i]);
			}

		}
		ObjectIndex idx = new ObjectIndex(objectidx, useridx);

		fileReader.close();
		csvFileParser.close();

		if (goldstandard != null) {
			csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter(',').withQuote('"');
			fileReader = new FileReader(goldstandard);
			csvFileParser = new CSVParser(fileReader, csvFileFormat);

			it = csvFileParser.iterator();
			while (it.hasNext()) {
				CSVRecord cur = it.next();
				String objectid = cur.get(0);

				CSObject object = idx.getObject(objectid);

				Annotation goldannotation = new Annotation(object, goldUser);
				// object.addAnnotation(goldannotation);
				goldUser.addAnnotation(goldannotation);

				for (int i = 2; i < cur.size(); i++) {
					goldannotation.addLevel(i - 2, cur.get(i));
				}

			}
		}
		idx.setGoldUser(goldUser);

		csvFileParser.close();
		idx.calculateFullPath();
		return idx;
	}

}
