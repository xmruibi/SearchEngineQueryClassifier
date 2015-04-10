package com.preprocess.query;

import java.io.File;
import java.io.IOException;

import com.POJO.QueryInfo;
import com.index.Inject.QueryExpIndexWritter;
import com.utils.constant.LuceneConstant;

/**
 * Query index insert class
 * @author birui
 *
 */
public class QueryIndexFactory {

	/**
	 * Insert current query into index with getting top 5 result from google
	 * @param query
	 */
	public int insertCurrentQuery(String query) {
		// TODO Auto-generated method stub
		QueryInfo qInfo = new QueryInfo();
		qInfo.setQueryId(qInfo.hashCode());
		qInfo.setQueryContent(query);
		qInfo.setQueryExpension(QueryProcessor.getExpan(query));

		try {
			QueryExpIndexWritter indexWritter = new QueryExpIndexWritter(new File(LuceneConstant.INDEX_PATH));
			indexWritter.createIndex(qInfo);
			indexWritter.close();
			return qInfo.hashCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
