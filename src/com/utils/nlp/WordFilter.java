package com.utils.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tartarus.snowball.ext.EnglishStemmer;

import weka.core.Stopwords;

/**
 * 
 * @author birui
 *
 */
public class WordFilter {

	/**
	 * Stopword filter for single word
	 * @param word
	 * @return
	 */
	public static String stopwordRemover(String word){
		Stopwords stopword = new Stopwords();
		try {
			stopword.read(new File("EN_Stopwords.txt"));
				if(stopword.isStopword(word)){
					return "";
				}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return word;
	}
	/**
	 * Stopwords filter for words string array
	 * @param words
	 * @return The clean words without stop words
	 */
	public static String[] stopwordRemover(String[] words) {
		Stopwords stopword = new Stopwords();
		String[] outWords = new String[words.length];
		int count = 0;
		try {
			stopword.read(new File("EN_Stopwords.txt"));
			for(String word:words){
				if(!stopword.isStopword(word)){
					outWords[count]=word;
					count++;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> list = new ArrayList<String>(Arrays.asList(outWords));
	    list.removeAll(Collections.singleton(null));
	    return list.toArray(new String[list.size()]);
	}

	/**
	 * Stemmer filter for words String array
	 * @param inwords
	 * @return The clean stemmed word
	 */
	public static String[] stem(String[] inwords) {
		String[] outwords = new String[inwords.length];
		for (int i = 0; i < inwords.length; i++) {
			EnglishStemmer stemmer = new EnglishStemmer();
			stemmer.setCurrent(inwords[i]);
			boolean result = stemmer.stem();
			if (!result)
				outwords[i] = inwords[i];
			else
				outwords[i] = stemmer.getCurrent();
		}
		List<String> list = new ArrayList<String>(Arrays.asList(outwords));
	    list.removeAll(Collections.singleton(null));
	    return list.toArray(new String[list.size()]);
	}
}
