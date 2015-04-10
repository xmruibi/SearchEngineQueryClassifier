package com.preprocess.query;

import com.utils.google.GoogleSearch;

public class QueryProcessor {

	public static String getExpan(String  query) {
		// TODO Auto-generated method stub
		return GoogleSearch.results(query,5);
	}
}
