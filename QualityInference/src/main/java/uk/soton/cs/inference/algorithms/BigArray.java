package uk.soton.cs.inference.algorithms;

import java.util.Hashtable;

public class BigArray {
	
Hashtable<String, Hashtable<String, Double>> arr=new Hashtable<>();

@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		for(String xid:arr.keySet())
		{
			Hashtable<String, Double> y = arr.get(xid);
			for(String yid:y.keySet())
			{
				sb.append("("+xid+","+yid+","+y.get(yid)+")");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
public void set(String i, String j, double d)
{
	Hashtable<String, Double> cur = arr.get(i);
	if(cur==null)
	{
		arr.put(i, cur=new Hashtable<>());
	}
	cur.put(j,d);
}
public double get(String i, String j)
{
	Hashtable<String, Double> cur = arr.get(i);
	if(cur==null) return 0;
	Double d = cur.get(j);
	if(d==null)
	{
		//random reliability
		return 0;
	}
	return d;
}
public BigArray copy() {
	BigArray ret=new BigArray();
	for(String s:arr.keySet())
	{
		Hashtable<String, Double> cur = arr.get(s);
		Hashtable<String, Double> copy=new Hashtable<>();
		for(String s1:cur.keySet())
		{
			copy.put(s1, cur.get(s1));
		}
		ret.arr.put(s, copy);
	}
	return ret;
}
public double difference(BigArray oldY) {
	
	double sum=0.0;
	for(String s:arr.keySet())
	{
		Hashtable<String, Double> conti1 = arr.get(s);
		Hashtable<String, Double> conti2 = oldY.arr.get(s);
		if(conti2==null)
		{
			sum+=conti1.size();
			continue;
		}
		
		for(String s1:conti1.keySet())
		{
			Double d1 = conti1.get(s1);
			Double d2 = conti2.get(s1);
			if(d2==null) d2=-d1;
			sum+=Math.pow((d2+d1),2);
		}
	}
	

	return Math.sqrt(sum);
}
public String toString(int imax, int jmax) {
	StringBuilder sb=new StringBuilder();
	
	
	for(String xid:arr.keySet())
	{
		int cury=jmax;
		if(imax--<0) continue;
		Hashtable<String, Double> y = arr.get(xid);
		for(String yid:y.keySet())
		{
			if(cury--<0) continue;
			sb.append("("+xid+","+yid+","+y.get(yid)+")");
		}
		sb.append("\n");
		
	}
	return sb.toString();
}
}
