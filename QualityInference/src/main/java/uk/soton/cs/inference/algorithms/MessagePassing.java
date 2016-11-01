package uk.soton.cs.inference.algorithms;

import java.util.Collection;
import java.util.Date;
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
		while (difference > delta&&k++<4) {
			
		
			System.out.println("Round "+k+" start ");
			//BigArray oldY=Y.copy();
			for (CSObject object : idx.getObjectindex().values()) {
				double tmp_sum_all = 0;

				for (Annotation annotation : object.getUsers().values()) {
					CSUser user = annotation.getUser();

					if (object.hasAnnotationExceptForUser(level,answer,user)) {
						A.set(object.getId(), user.getId(), 1);
						tmp_sum_all += Y.get(object.getId(), user.getId());
					} else {
						A.set(object.getId(), user.getId(), -1);
						tmp_sum_all -= Y.get(object.getId(), user.getId());
					}
				}

				double tmp_j = 0;
				for (Annotation annotation : object.getUsers().values()) {
					CSUser user = annotation.getUser();

					if (object.hasAnnotationExceptForUser(level,answer,user)) {
						// A.set(object.getId(), user.getId(), 1);
						// tmp_sum_all+=Y.get(object.getId(), user.getId());
						tmp_j = Y.get(object.getId(), user.getId());
					} else {
						// A.set(object.getId(), user.getId(), -1);
						//// tmp_sum_all-=Y.get(object.getId(), user.getId());
						tmp_j = -Y.get(object.getId(), user.getId());
					}
					X.set(object.getId(), user.getId(), tmp_sum_all - tmp_j);
				}

			}
			
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
			
			Date curdate = new Date();
			System.out.println("Round "+k+" took "+(curdate.getTime()-start.getTime()));
			start=curdate;
		}
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
