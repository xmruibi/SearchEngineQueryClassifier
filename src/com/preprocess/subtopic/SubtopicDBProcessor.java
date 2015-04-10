package com.preprocess.subtopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.DAOs.SubtopicDAO;
import com.DAOs.TaskDAO;
import com.POJO.SubtopicInfo;
import com.utils.google.GoogleSearch;
import com.utils.nlp.KeywordExtraction;

public class SubtopicDBProcessor {
	
	
	
	/**
	 * The initial subtopic data saving step
	 * Insert new subtopicinfo(id, content, keywords) into database
	 * @see keywordsGen
	 * @param sInfo new subtipicinfo from user input;
	 * @return subtopicID 
	 */
	public static int initInsert(SubtopicInfo sInfo) {
		// TODO Auto-generated method stub
		SubtopicDAO sDao = new SubtopicDAO();
		 return sDao.insertNewSubtopicInfo(keywordsGen(sInfo));	
	}
	
	/**
	 * Get keywords from new subtopic statement
	 * @param sInfo original SubtopicInfo
	 * @return subtopicinfo with keywords information 
	 */
	private static SubtopicInfo keywordsGen(SubtopicInfo sInfo) {
		// TODO Auto-generated method stub
		String subtopicStmt = sInfo.getSubtopicStmt();
		List<String> keywords = new ArrayList<String>();
		keywords = KeywordExtraction.getKeywords(subtopicStmt);
		sInfo.setKeywords(String.join(",", keywords));
		return sInfo;
	}
	
	
	/**
	 * The second stage of subtopic data saving 
	 * Get subtopic description by subtopic queries which is generated by keywords extraction
	 * Update database information with inserting subtopic description
	 * @see getSubtopicDescription, getSubtopicQuery
	 * @param sInfo
	 */
	public static void updateSubtopicDescri(SubtopicInfo sInfo) {
		// TODO Auto-generated method stub
		String subtopicDescri = getSubtopicDescription(getSubtopicQuery(sInfo));
		sInfo.setDescription(subtopicDescri);
		SubtopicDAO sDAO = new SubtopicDAO();
		sDAO.updateSubtopicDescri(sInfo);
		sDAO.close();
		
	}
	
	/**
	 *  Get subtopic description (subtopic language model)
	 * @param subtopicQueries
	 * @return
	 */
	private static String getSubtopicDescription(List<String> subtopicQueries) {
		// TODO Auto-generated method stub
		String description="";
		for(String query:subtopicQueries){
			description+= GoogleSearch.results(query, 20);
		}
		return description;
	}

	/**
	 * Get keywords combination from task statement and subtopic statement
	 * @param sInfo
	 * @return
	 */
	private static List<String> getSubtopicQuery(SubtopicInfo sInfo) {
		// TODO Auto-generated method stub
		TaskDAO tDao = new TaskDAO();
		List<String> queries = new ArrayList<String>();
		List<String> subtopicKeywords = new ArrayList<String>();
		List<String> taskKeywords = new ArrayList<String>();
		subtopicKeywords = Arrays.asList(sInfo.getKeywords().split(","));
		taskKeywords= Arrays.asList(tDao.getTaskInfoByID(sInfo.getTaskId()).getKeywords().split(","));
		for(String sWord:subtopicKeywords){
			for(String tWord:taskKeywords){
				queries.add(tWord+" "+sWord);
			}
		}
		tDao.close();
		return queries;
	}

}
