package com.retrieval.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

import com.index.Read.IndexRead;
import com.utils.constant.LuceneConstant;

/**
 * 
 * @author birui
 * 
 *         This class is for calculating the score comparing the given query
 *         with subtopic language model by using Dirichlet Prior Smoothing
 *         Algorithm. Input the index file when instance this class, then giving
 *         your query by using initCalc method, finally get score by giving your
 *         subtopic ID.
 *
 */
public class DirichletPriorSmth {

	private IndexReader reader;
	private IndexSearcher searcher;
	private final int u = 100; // u constant for smoothing

	private Map<Integer, Integer> singleTermDocFreqMap;
	// a single term frequency map with document id and the frequency in a
	// certain document
	private long singleTermCollectionFreq = 0; //
	// a single term's frequency in collection

	private Map<String, Map<Integer, Integer>> queryDocFreqMap = new HashMap<String, Map<Integer, Integer>>();
	// a complex map with term in query (multiple words) and the document it
	// appears with its frequency in that document
	private Map<String, Long> queryCollectionFreqMap = new HashMap<String, Long>();
	// a map with term in query and frequency in the entire collection

	private long collectionLength = 0; // the length of collection
	private String[] currentQuery; // the given query

	public DirichletPriorSmth(File file) {
		// TODO Auto-generated constructor stub
		IndexRead indexreader;
		try {
			indexreader = new IndexRead(file);
			this.reader = indexreader.getReader();
			this.searcher = new IndexSearcher(reader);
			collectionLength(); // initial to get the collection length
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			this.reader = null;
		}
	}

	/**
	 * The public method to input query and set up those maps
	 * 
	 * @param query
	 * @return initial status feedback
	 */
	public String initCalc(String[] query) {
		// TODO Auto-generated method stub
		try {
			this.currentQuery = query;
			for (String term : query) {
				termFreq(term); //
				queryDocFreqMap.put(term, singleTermDocFreqMap);
				queryCollectionFreqMap.put(term, singleTermCollectionFreq);
			}
			return "Init successful!";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Init failed! -- " + e.getMessage();
		}
	}

	/**
	 * The public method to get query score with input subtopic ID
	 * 
	 * @param subtopicID
	 *            -- subtopic ID
	 * @return the score of query with a subtopic
	 */
	public double query_subtopicScore(int subtopicID) {
		// TODO Auto-generated method stub
		double score = 1;
		int docID = searchDocBySubtopic(subtopicID);
		int currentDocLength = getDocLength(docID);
		for (String term : this.currentQuery) {
			int termDocFreq = 0;
			if (queryDocFreqMap.get(term) != null
					&& queryDocFreqMap.get(term).containsKey(docID))
				termDocFreq = queryDocFreqMap.get(term).get(docID);
			long termCollectionFreq = queryCollectionFreqMap.get(term);
			score *= probabilityCalc(termDocFreq, termCollectionFreq,
					currentDocLength);
		}
		return score;
	}

	/**
	 * Close reader when ending this instance
	 * @throws IOException
	 */
	public void closeReader() throws IOException {
		// TODO Auto-generated method stub
		reader.close();
	}
	/**
	 * Get the total term amount in entire collection(all documents in index)
	 * 
	 * @return collection length
	 * @throws IOException
	 */
	private void collectionLength() throws IOException {
		for (int i = 0; i < reader.numDocs(); i++) {
			this.collectionLength += reader.document(i)
					.getField(LuceneConstant.SUBTOPIC_CONTENT_FIELD).toString()
					.length();
		}
	}

	/**
	 * Get a single term frequency in each document and in the whole collection
	 * 
	 * @param term
	 *            Input term for calculating the frequency in the whole
	 *            collection
	 * @throws IOException
	 */
	private void termFreq(String term) throws IOException {
		singleTermCollectionFreq = 0; // clean
		singleTermDocFreqMap = new HashMap<Integer, Integer>(); // clean
		DocsEnum docEnum = MultiFields.getTermDocsEnum(this.reader,
				MultiFields.getLiveDocs(this.reader),
				LuceneConstant.SUBTOPIC_CONTENT_FIELD, new BytesRef(term));
		if (docEnum == null) {
			singleTermCollectionFreq = 1;
			singleTermDocFreqMap = new HashMap<Integer, Integer>();
		} else {
			int doc = DocsEnum.NO_MORE_DOCS;
			while ((doc = docEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
				singleTermDocFreqMap.put(doc, docEnum.freq());
				singleTermCollectionFreq += docEnum.freq(); // for check
			}
		}
		
		
		
	}

	/**
	 * Find document ID by subtopic ID and set the current document ID as
	 * subtopic ID
	 * 
	 * @param sid
	 *            subtopic ID
	 * @return document ID
	 */
	private int searchDocBySubtopic(int sid) {
		// TODO Auto-generated method stub
		Query IDquery;
		int doc=-1;
		try {
			IDquery = NumericRangeQuery.newIntRange(LuceneConstant.SUBTOPIC_ID_FIELD, sid, sid, true, true);
			if (IDquery != null) {
				 TopDocs td = searcher.search(IDquery, 1);
				 for(ScoreDoc scoreDoc:td.scoreDocs){
					 doc=scoreDoc.doc;
				 }
				return doc;
			} else {
				throw new NullPointerException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Get current document length by document id
	 * 
	 * @param docId
	 *            Input Document ID
	 * @return document length
	 */
	private int getDocLength(int docID) {
		try {
			return reader.document(docID)
					.getField(LuceneConstant.SUBTOPIC_CONTENT_FIELD).toString()
					.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(docID + ": " + e.getMessage());
		}
		return -1;
	}

	/**
	 * The Dirichlet Prior Smoothing algorithm
	 * 
	 * @param termDocFreq
	 * @param termCollectionFreq
	 * @param currentDocLength
	 * @return a single term's score according to this algorithm
	 */
	private double probabilityCalc(int termDocFreq, long termCollectionFreq,
			int currentDocLength) {
		// TODO Auto-generated method stub
		double score = 0;
		double smoothingDenominator = (currentDocLength + u);
		double smoothingMolecular = (termDocFreq + ((double) (u * termCollectionFreq) / (double) collectionLength));
		score = smoothingMolecular / smoothingDenominator;
		return score;
	}
	
	
	
}
