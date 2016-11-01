package uk.soton.cs.inference.algorithms;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

import uk.soton.cs.inference.dataset.Annotation;
import uk.soton.cs.inference.dataset.CSObject;
import uk.soton.cs.inference.dataset.CSUser;
import uk.soton.cs.inference.dataset.ObjectIndex;

public class MessagePassing {

	public void calculate(ObjectIndex idx, Collection<CSObject> refobjects) {
		double delta = 2;
		double difference = Double.MAX_VALUE;
		String answer = "zebra";
		int level = 0;

		BigArray A = new BigArray();
		BigArray Y = idx.randomizeArray();
		BigArray X = new BigArray();
		Random r=new Random();/*
		for(CSObject object:idx.getObjectindex().values())
		{
			for(CSUser user:idx.getUserindex().values())
			{
				Y.set(object.getId(), user.getId(), r.nextGaussian());
			}
		}
		*/
		
		int k=0;
		Date start=new Date();
		HashSet<String> cache=new HashSet<>();
		while (difference > delta&&k++<4) {
			
		
			System.out.println("Round "+k+" start ");
			//BigArray oldY=Y.copy();
			Date startloop1=new Date();
			
			int loop1=0,loop11=0,loop12=0,loop2=0;
			for (CSObject object : idx.getObjectindex().values()) {
				double tmp_sum_all = 0;
				Date startloop11=new Date();
				for (Annotation annotation : object.getUsers().values()) {
					CSUser user = annotation.getUser();

					
					String key=object.getId()+"|"+level+"|"+user.getId();
					
					if (cache.contains(key)||object.hasAnnotationExceptForUser(level,answer,user)) {
						A.set(object.getId(), user.getId(), 1);
						tmp_sum_all += Y.get(object.getId(), user.getId());
						cache.add(key);
					} else {
						A.set(object.getId(), user.getId(), -1);
						tmp_sum_all -= Y.get(object.getId(), user.getId());
					}
				}
				Date d1 = new Date();
			//	System.out.println("Loop11 took " +Math.round((d1.getTime()-startloop11.getTime())/2));
				loop11+=d1.getTime()-startloop11.getTime();
				Date startloop12 = d1;
				double tmp_j = 0;
				for (Annotation annotation : object.getUsers().values()) {
					CSUser user = annotation.getUser();

					String key=object.getId()+"|"+level+"|"+user.getId();
					
					if (cache.contains(key)||object.hasAnnotationExceptForUser(level,answer,user)) {
						// A.set(object.getId(), user.getId(), 1);
						// tmp_sum_all+=Y.get(object.getId(), user.getId());
						tmp_j = Y.get(object.getId(), user.getId());
						cache.add(key);
					} else {
						// A.set(object.getId(), user.getId(), -1);
						//// tmp_sum_all-=Y.get(object.getId(), user.getId());
						tmp_j = -Y.get(object.getId(), user.getId());
					}
					X.set(object.getId(), user.getId(), tmp_sum_all - tmp_j);
				}
				
				Date d2 = new Date();
				loop12+=(d2.getTime()-startloop12.getTime());

			}
			Date d2 = new Date();
			System.out.println("Loop1 took " +Math.round((d2.getTime()-startloop1.getTime())/1000));
			System.out.println("Loop11 took " +Math.round(loop11/1000));
			System.out.println("Loop11 took " +Math.round(loop12/1000));
			Date startloop2 = d2;
			
			for (CSObject object : idx.getObjectindex().values()) 
			{
				for(CSUser user:idx.getUserindex().values())
				{
					
					double tmp_sum_j_not_i = 0;
					for(Annotation annotation:user.getObjects().values())
					{
						if(annotation.getObject().getId().equals(object.getId()))
						{
							//only other objects are interesting
							continue;
						}
						
						if(answer.equals(annotation.getAtLevel(level)))
						{
							A.set(annotation.getObject().getId(), user.getId(), 1);
							tmp_sum_j_not_i+= X.get(annotation.getObject().getId(), user.getId());
						}else
						{
							A.set(annotation.getObject().getId(), user.getId(), -1);
							tmp_sum_j_not_i-= X.get(annotation.getObject().getId(), user.getId());
						}
						Y.set(object.getId(), user.getId(), tmp_sum_j_not_i);
					}
				//	difference = Y.difference(oldY);
								
				}

			}
			Date d3= new Date();
			System.out.println("Loop2 took " +Math.round((d3.getTime()-startloop2.getTime())/2));
		
			Date curdate = new Date();
			System.out.println("Round "+k+" took "+(curdate.getTime()-start.getTime()));
			start=curdate;
		}
		System.out.println("predict objects");
		for(CSObject object:refobjects)
		{
			
			
			double tmp_sum_all = 0;
			
			for(Annotation annotation:object.getUsers().values())
			{
				if(object.hasAnnotation(level, answer))
				{
					A.set(object.getId(), annotation.getUser().getId(), 1);
					tmp_sum_all+=Y.get(object.getId(), annotation.getUser().getId());
				}else
				{
					A.set(object.getId(), annotation.getUser().getId(), -1);
					tmp_sum_all-=Y.get(object.getId(), annotation.getUser().getId());
				}
			}
			
			System.out.println("Class "+answer+" is predicted "+(tmp_sum_all>0?"true":"false")+" for object "+object.getId()+" with true class: "+idx.getGolduser().getObjects().get(object.getId()).getAtLevel(level)+"and useranswers:");
			
			System.out.println(object.printAnnotations(level));
		}

	}

}
