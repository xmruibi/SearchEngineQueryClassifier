package com.utils.nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;

/**
 * Using Stanford NLP package to extract keyword from text content
 * @author birui
 *
 */
public class KeywordExtraction {
	static LexicalizedParser lp;
	static List<String> keywords = new ArrayList<String>(); // keywords list	
	
	// keywords map with frequency
	static Map<String, Integer> keywordFrq = new HashMap<String, Integer>();
	


	/**
	 * Get keywords from a certain content
	 * @param content String content 
	 * @return the list of keywords
	 */
	public static List<String> getKeywords(String content) {
		// TODO Auto-generated method stub
		lp = LexicalizedParser
				.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		String sentence = content;
		List<Tree> phraseList = new ArrayList<Tree>();
		Tree parse = lp.parse(sentence);
		List taggedWords = parse.taggedYield();
		List<Tree> leaves = new ArrayList<Tree>();
		leaves = parse.getLeaves();

		String regEx = "[' ']+";
		Pattern p = Pattern.compile(regEx);

		/** Iterate each leaf in parser, get prop noun, noun... **/
		Tree preAncestor = null; // for checking the leaves under the same NP
		for (Tree leaf : leaves) {
			String keyword = null; // define the possible keyword

			if (leaf.ancestor(1, parse).label().value().contains("NN")
					|| leaf.ancestor(1, parse).label().value().contains("NNP")) {
				// check the word is normal noun
				if (preAncestor != null
						&& preAncestor == leaf.ancestor(2, parse))
					continue;

				if (leaf.ancestor(2, parse).label().value().equals("NP")) {
					// check the parent is phrase
					String phrase = "";
					int flag = 0;
					preAncestor = leaf.ancestor(2, parse);
					for (Tree checkLeaf : leaf.ancestor(2, parse).getLeaves()) {
						// check each leaf under a phrase
						if (checkLeaf.ancestor(1, parse).label().value()
								.contains("CD")
								|| checkLeaf.ancestor(1, parse).label().value()
										.contains("CC")) {
							// detect the CC(Coordinating conjunction),
							// CD(Cardinal number) leaf under phrase
							flag = 1;
						} else {
							phrase += " "
									+ KeywordFilter(checkLeaf.value()
											.toString());
						}
					}

					if (flag == 0) { // no CC or CD word contained
						setKeyword(phrase);
					} else if (flag == 1) {
						for (String word : phrase.split(" ")) {
							setKeyword(word);
						}
					}
				}
			} else if (leaf.ancestor(1, parse).label().value().contains("NNP")
					&& !leaf.ancestor(2, parse).label().value().equals("NP")) {
				// check the word is entity name
				keyword = leaf.value().toString() + "+"; // get a mark while
															// output

				setKeyword(keyword);

			} else {
				// the rest of single normal noun
				keyword = leaf.value().toString();
				setKeyword(keyword);
			}

		}
		return keywords;
	}

	/**
	 * Keyword setter for keyword list and keyword frequency map
	 * @param keyword
	 */
	private static void setKeyword(String keyword) {
		// TODO Auto-generated method stub
		String regEx = "[' ']+";
		Pattern p = Pattern.compile(regEx);
		keyword = KeywordFilter(keyword);
		keyword = p.matcher(keyword.replaceAll("\\p{Punct}", " ").trim())
				.replaceAll(" ");
		if (keyword.equals(" ") || keyword.equals("") || keyword.equals("RRB")
				|| keyword.equals("LRB"))
			return;
		if (!keywords.contains(keyword))
			keywords.add(keyword); // add in keyword list

		if (keywordFrq.containsKey(keyword)) // add in frequency map with
												// frequency
			keywordFrq.replace(keyword, keywordFrq.get(keyword) + 1);
		else
			keywordFrq.put(keyword, 1);
	}

	/**
	 * Simple filter 
	 * @param word
	 * @return clean word
	 */
	private static String KeywordFilter(String word) {
		// TODO Auto-generated method stub
		String[] words;
		if (word.split(" ").length > 1) {
			words = WordFilter.stopwordRemover(word.split(" "));
			word = "";
			for (String value : words)
				word += value + " ";
		} else
			word = WordFilter.stopwordRemover(word);
		return word;
	}
}
