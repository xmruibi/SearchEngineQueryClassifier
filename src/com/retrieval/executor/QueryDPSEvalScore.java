package com.retrieval.executor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.POJO.SubtopicInfo;
import com.retrieval.algorithms.DirichletPriorSmth;
import com.utils.constant.LuceneConstant;
import com.utils.nlp.LuceneWordFilter;

public class QueryDPSEvalScore {

	private DirichletPriorSmth dpsAlg;
	
	public QueryDPSEvalScore() {
		// TODO Auto-generated constructor stub
		dpsAlg = new DirichletPriorSmth(new File(LuceneConstant.INDEX_PATH));
	}
	
	/**
	 * Get Vector Space Model score by map <SubtopicID, ScoreValue> 
	 * @param sInfoList subtopic list in given task
	 * @param query current queryID
	 * @return SCOREMAP
	 * @throws IOException
	 */
	public Map<Integer, Double> getScoreList(List<SubtopicInfo> sInfoList,String query) throws IOException {
		// TODO Auto-generated method stub
		Map<Integer, Double> scoreMap = new HashMap<Integer,Double>();
		String terms = LuceneWordFilter.wordFilter(query.split(" "));
		dpsAlg.initCalc(terms.split(" "));
		for(SubtopicInfo sinfo:sInfoList){
			scoreMap.put(sinfo.getSubtopicId(), dpsAlg.query_subtopicScore(sinfo.getSubtopicId()));
		}
		dpsAlg.closeReader();
		return scoreMap;
	}
}
