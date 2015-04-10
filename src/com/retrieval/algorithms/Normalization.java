package com.retrieval.algorithms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Normalization {

	/**
	 * Normalize for each column (each score under the same task)
	 * 
	 * @param scoreMap
	 * @return
	 */
	public static Map<Integer, Double> compute(Map<Integer, Double> scoreMap) {
		// TODO Auto-generated method stub
		Collection<Double> vals = scoreMap.values();
		double xBar=0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		for(double val:vals){
			xBar+=val;
			if(val>max)
				max = val;
			if(val<min)
				min=val;
		}
		xBar = xBar/(double)vals.size();
		Iterator it = scoreMap.entrySet().iterator();
		  while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        double newScore = ((double)pairs.getValue()-xBar)/(max-min);
		        scoreMap.replace((Integer) pairs.getKey(), newScore);
		}
		return scoreMap;
	}
}
