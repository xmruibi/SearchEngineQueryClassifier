package com.retrieval.algorithms;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * This class is for combine the scores from DPS score and VSM score
 * and get sorted score map
 * @author birui
 *
 */
public class ScoreRank implements Comparator<Integer>  {

    Map<Integer, Double> base;
    public ScoreRank(Map<Integer, Double> base) {
        this.base = base;
    }
  
	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		if (base.get(o1) >= base.get(o2)) {
            return -1;
        } else {
            return 1;
        }
	}

	public static Map<Integer, Double> getRank(Map<Integer, Double> scoreMap1,Map<Integer, Double> scoreMap2) {
		// TODO Auto-generated method stub  
		for (Entry<Integer, Double> entry : scoreMap1.entrySet()) {  
		  scoreMap1.replace(entry.getKey(), entry.getValue()+scoreMap2.get(entry.getKey()));	  
		}  
		ScoreRank sort = new ScoreRank(scoreMap1);
		Map<Integer,Double> sorted = new TreeMap<Integer,Double>(sort);	
		sorted.putAll(scoreMap1);
		return sorted;
	}
}
