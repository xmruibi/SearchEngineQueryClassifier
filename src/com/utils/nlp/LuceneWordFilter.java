package com.utils.nlp;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 
 * @author birui
 *
 */
public class LuceneWordFilter {
	private static PorterStemAnalyzer analyzer;
	private static TokenStream ts;

	/**
	 * Word Filter using Lucene analyzer
	 * @see PorterStemAnalyzer
	 * @param words 
	 * @return
	 */
	public static String wordFilter(String[] words) {
		// TODO Auto-generated method stub
		String terms = "";
		for (String term : words) {
			analyzer = new PorterStemAnalyzer();
			Reader reader = new StringReader(term);
			try {
				ts = analyzer.tokenStream("myfield", reader);
			
			CharTermAttribute charTermAttribute = ts
					.getAttribute(CharTermAttribute.class);
			ts.reset();
			while (ts.incrementToken()) {
				terms += charTermAttribute.toString() + " ";
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				terms +=" ";
			}
		}
		return terms.trim();
	}
}
