package com.retrieval.executor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import com.POJO.SubtopicInfo;
import com.preprocess.query.QueryIndexFactory;
import com.preprocess.subtopic.SubtopicIndexFactory;
import com.retrieval.algorithms.Normalization;
import com.retrieval.algorithms.ScoreRank;

/**
 * This class is for demo the query-topic rank
 * 
 * @author birui
 *
 */
public class SubtopicRankerDemo {

	public static void main(String[] args) throws IOException {
		Map<Integer, Double> scoreMap1 = new HashMap<Integer, Double>();
		Map<Integer, Double> scoreMap2 = new HashMap<Integer, Double>();
		SubtopicIndexFactory sIndexFactory = new SubtopicIndexFactory();
		sIndexFactory.setCurrentIndex(1);

		while (1 == 1) {
			Scanner scan = new Scanner(System.in);
			String query = scan.nextLine();

			long starttime = System.currentTimeMillis();
			// Insert current query into index
			QueryIndexFactory qIndexFactory = new QueryIndexFactory();
			int queryID = qIndexFactory.insertCurrentQuery(query);

			// Get score map by Dirichlet Prior Smoothing which computes the
			// query score term by term
			QueryDPSEvalScore dpsScore = new QueryDPSEvalScore();
			scoreMap1 = Normalization.compute(dpsScore.getScoreList(
					sIndexFactory.getsInfoList(), query));

			// Get Vector Space Model by comparing the query expansion content
			// and subtopic language
			QueryExpenVSMScore vsmScore = new QueryExpenVSMScore();
			scoreMap2 = Normalization.compute(vsmScore.getScoreList(
					sIndexFactory.getsInfoList(), queryID));

			// Get subtopic content for showing
			Map<Integer, String> sInfoMap = new HashMap<Integer, String>();
			for (SubtopicInfo sinfo : sIndexFactory.getsInfoList()) {
				sInfoMap.put(sinfo.getSubtopicId(), sinfo.getSubtopicStmt());
			}
			long endtime = System.currentTimeMillis();
			for (Entry<Integer, Double> entry : ScoreRank.getRank(scoreMap1,
					scoreMap2).entrySet()) {
				System.out.println(sInfoMap.get(entry.getKey()) + "    "
						+ entry.getValue());
			}
			System.out.println("Searching time: "+String.valueOf(endtime-starttime)+"ms");
		}
	}
}
