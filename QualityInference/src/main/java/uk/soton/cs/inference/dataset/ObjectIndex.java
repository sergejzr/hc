package uk.soton.cs.inference.dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.lang3.ArrayUtils;

import uk.soton.cs.inference.algorithms.BigArray;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.NonSparseToSparse;

public class ObjectIndex {

	Hashtable<String, CSObject> objectidx;

	Vector<HashSet<String>> lables = new Vector<>();

	public ObjectIndex(Hashtable<String, CSObject> objectidx, Hashtable<String, CSUser> useridx) {
		super();
		this.objectidx = objectidx;
		this.useridx = useridx;

		for (CSObject object : objectidx.values()) {
			for (Annotation annotation : object.getUsers().values()) {
				for (int i = 0; i < annotation.levelsSize(); i++) {
					if (lables.size() < i + 1) {
						lables.add(null);
					}
					HashSet<String> set = lables.elementAt(i);
					if (set == null) {
						lables.set(i, set = new HashSet<>());
					}

					set.addAll(annotation.getAtLevel(i));
				}

			}
		}
	}

	Hashtable<String, CSUser> useridx;
	private CSUser goldUser;

	public CSObject getObject(String objectid) {
		// TODO Auto-generated method stub
		return objectidx.get(objectid);
	}

	public Hashtable<String, CSObject> getObjectindex() {
		return objectidx;
	}

	public Hashtable<String, CSUser> getUserindex() {
		return useridx;
	}

	public void setGoldUser(CSUser goldUser) {
		this.goldUser = goldUser;
	}

	public CSUser getGolduser() {
		// TODO Auto-generated method stub
		return goldUser;
	}

	public BigArray randomizeArray() {
		BigArray ret = new BigArray();
		Random r = new Random();
		for (CSObject object : objectidx.values()) {
			for (String userid : object.getUsers().keySet()) {
				ret.set(object.getId(), userid, r.nextGaussian());
			}
		}
		return ret;
	}

	public void toARFF(File file, int level) throws IOException {

		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		Attribute aclass;
		attributes.add(aclass = new Attribute("species", new ArrayList<String>(lables.elementAt(level))));

		for (String lable : lables.elementAt(level)) {
			attributes.add(new Attribute("true_"+lable,Arrays.asList("0,1".split(","))));
		}
		for (String lable : lables.elementAt(level)) {
			attributes.add(new Attribute("votes_"+lable));
		}
		ArffSaver saver = new ArffSaver();

		Instances dataSet;

		int maxnumatt = lables.elementAt(level).size();

		File outarff = file;
		saver.setFile(outarff);
		saver.setDestination(outarff);
		dataSet = new Instances("SnapshotSerengeti", attributes, 0);
		dataSet.setClass(aclass);

		for (CSObject object : objectidx.values()) {
			ArrayList<Integer> indexeslist = new ArrayList<>();
			ArrayList<Double> valueslist = new ArrayList<>();
			Hashtable<String, Double> counter = new Hashtable<>();

	
			
			Annotation trueannotation = goldUser.getObjects().get(object.getId());
			HashSet<String> trueset = trueannotation.getAtLevel(level);
			
			for (String lable : lables.elementAt(level))
			{
				indexeslist.add(dataSet.attribute("true_"+lable).index());
				valueslist.add(trueset.contains(lable)?1.:0.);
			}
			/*
			for(Annotation annotation:goldUser.getObjects().values())
			{
				for(String truelabel: annotation.getAtLevel(level))
				{
					gtindexlist.add(dataSet.attribute("true_"+truelabel).index());
					gtvalueslist.add()
				}
			}
			*/
			for (Annotation annotation : object.getUsers().values()) {
				for (String label : annotation.getAtLevel(level)) {
					Double cnt = counter.get(label);
					if (cnt == null) {
						cnt = 0.;
					}
					counter.put(label, cnt + 1);
				}
			}

			for (String label : counter.keySet()) {
				Double val = counter.get(label);
				Attribute att = dataSet.attribute("votes_"+label);
				indexeslist.add(att.index());
				valueslist.add(val);
			}

			double[] values = new double[valueslist.size()];
			int[] indexes = new int[valueslist.size()];

			for (int i = 0; i < indexeslist.size(); i++) {
				indexes[i] = indexeslist.get(i);
			}
			for (int i = 0; i < valueslist.size(); i++) {
				values[i] = valueslist.get(i);
			}

			SparseInstance instance = new SparseInstance(1.0, values, indexes, maxnumatt);
			dataSet.add(instance);
			instance.setDataset(dataSet);
		}

		Instances instNew = dataSet;
		NonSparseToSparse nonSparseToSparseInstance = new NonSparseToSparse();

		try {

			nonSparseToSparseInstance.setInputFormat(instNew);
			Instances sparseDataset = Filter.useFilter(instNew, nonSparseToSparseInstance);
			sparseDataset.setClassIndex(aclass.index());

			saver.setDestination(outarff);
			saver.setInstances(sparseDataset);
			saver.writeBatch();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
