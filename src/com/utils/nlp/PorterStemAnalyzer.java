package com.utils.nlp;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.util.Version;

/**
 * 
 * @author birui
 *
 */
public class PorterStemAnalyzer extends Analyzer {

	/**
	 * @param fieldName
	 * @param reader
	 * @return
	 */
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

        Tokenizer source = new LowerCaseTokenizer(Version.LUCENE_46, reader);
        TokenStream stem = new org.apache.lucene.analysis.en.PorterStemFilter(source);
        TokenStream stopRemoval = new StopFilter(Version.LUCENE_46, stem, StopAnalyzer.ENGLISH_STOP_WORDS_SET);       
        return new TokenStreamComponents(source, stopRemoval);
      }
}

