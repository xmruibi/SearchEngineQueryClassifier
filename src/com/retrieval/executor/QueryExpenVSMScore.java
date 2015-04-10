package com.retrieval.executor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.POJO.SubtopicInfo;
import com.retrieval.algorithms.TfidfVector;
import com.utils.constant.LuceneConstant;

public class QueryExpenVSMScore {

	private TfidfVector tVector;
	public QueryExpenVSMScore() {
		// TODO Auto-generated constructor stub
		tVector = new TfidfVector(new File(LuceneConstant.INDEX_PATH));
	}
	
	/**
	 * Get Vector Space Model score by map <SubtopicID, ScoreValue> 
	 * @param sInfoList subtopic list in given task
	 * @param queryID current queryID
	 * @return SCOREMAP
	 * @throws IOException 
	 */
	public	Map<Integer, Double> getScoreList(List<SubtopicInfo> sInfoList,int queryID) throws IOException {
		// TODO Auto-generated method stub
		Map<Integer, Double> scoreMap = new HashMap<Integer,Double>();
		for(SubtopicInfo sinfo:sInfoList){
			try {
				scoreMap.put(sinfo.getSubtopicId(), tVector.VSMSimlarityScore(sinfo.getSubtopicId(), queryID));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tVector.closeReader();
		return scoreMap;
	}
}
