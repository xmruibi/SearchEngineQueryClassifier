package com.retrieval.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

import com.index.Read.IndexRead;
import com.utils.constant.LuceneConstant;

/**
 * This class is for calculate the TFIDF vector space model between query expansion content and subtopic language model
 * Build the IDF value for query part and subtopic part in constructor
 * @see VSMSimlarityScore(int sid,int qid) which is the method can give the score by given subtopic id and query id
 * @author birui
 *
 */
public class TfidfVector {

	private IndexReader reader;
	private final Set<String> terms = new HashSet<>();	  // terms set for vector
	private Map<String, Float> idf4Subtopic = new HashMap<>();
	private Map<String, Float> idf4Query = new HashMap<>();
	TFIDFSimilarity tfidfSIM = new DefaultSimilarity();	

	/**
	 * Constructor with fiel path input
	 * acquire subtopics idf value 
	 * acquire query idf value
	 * @param file
	 */
	public TfidfVector(File file) {
		// TODO Auto-generated constructor stub
		IndexRead indexreader;
		try {
			indexreader = new IndexRead(file);
			this.reader = indexreader.getReader(); // initial to get the
													// collection length
			this.getIdfsForSubtopic();
			this.getIdfsForQuery();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			this.reader = null;
		}
	}

	/**
	 * The public method to get TFIDF Vector in given index
	 * @return the map with document ID and its term tfidf vector 
	 * @throws Exception
	 */
	public double VSMSimlarityScore(int sid,int qid) throws Exception {
		// TODO Auto-generated method stub
		int subtopcDoc = getDocByID(sid,LuceneConstant.SUBTOPIC_ID_FIELD);
		int queryDoc = getDocByID(qid,LuceneConstant.QUERY_ID_FIELD);
		RealVector subtopcVector = subtopic_get_vector(subtopcDoc);
		RealVector queryVector = query_get_vector(queryDoc);
		double dotP = subtopcVector.dotProduct(queryVector);
		double score = (double) ((dotP) / (subtopcVector.getNorm() * queryVector.getNorm()));
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
	 * Get all terms in Subtopic Field idf value in the index
	 * @throws IOException
	 */
	private void getIdfsForSubtopic() throws IOException {
		Map<String, Float> idfResult = new HashMap<>();

		for (int i = 0; i < reader.numDocs(); i++) {
			Terms docterms = reader.getTermVector(i,  LuceneConstant.SUBTOPIC_CONTENT_FIELD);
			if (docterms != null && docterms.size() > 0) {
				TermsEnum termEnum = docterms.iterator(null);
				BytesRef bytesRef;
				while ((bytesRef = termEnum.next()) != null) {
					if (termEnum.seekExact(bytesRef)) {
						String term = bytesRef.utf8ToString();
						float idf = tfidfSIM
								.idf(reader.docFreq(new Term( LuceneConstant.SUBTOPIC_CONTENT_FIELD,
										term)), reader.numDocs());
						idfResult.put(term, idf);
						terms.add(term);
					}
				}
			}
		}
		idf4Subtopic = idfResult;
	}

	/**
	 * Get all terms in query Field idf value in the index
	 * @throws IOException
	 */
	private void getIdfsForQuery() throws IOException {
		Map<String, Float> idfResult = new HashMap<>();

		for (int i = 0; i < reader.numDocs(); i++) {
			Terms docterms = reader.getTermVector(i,  LuceneConstant.QUERY_CONTENT_FIELD);
			if (docterms != null && docterms.size() > 0) {
				TermsEnum termEnum = docterms.iterator(null);
				BytesRef bytesRef;
				while ((bytesRef = termEnum.next()) != null) {
					if (termEnum.seekExact(bytesRef)) {
						String term = bytesRef.utf8ToString();
						float idf = tfidfSIM
								.idf(reader.docFreq(new Term( LuceneConstant.QUERY_CONTENT_FIELD,
										term)), reader.numDocs());
						idfResult.put(term, idf);
						terms.add(term);
					}
				}
			}
		}
		idf4Query = idfResult;
	}
	/**
	 * Get subtopic tfidf vector of each document in given index
	 * 
	 * @param docID
	 * @return
	 * @throws Exception
	 */
	private RealVector subtopic_get_vector(int docID) {
		Map<String, Float> tf_Idf_Weights = new HashMap<>();
		TermsEnum termsEnum;
		try {
			termsEnum = MultiFields.getTerms(reader,  LuceneConstant.SUBTOPIC_CONTENT_FIELD).iterator(
					null);
			Terms vector = reader.getTermVector(docID,  LuceneConstant.SUBTOPIC_CONTENT_FIELD);
			try {
				termsEnum = vector.iterator(termsEnum);
			} catch (NullPointerException e) {
				System.out.println(docID);
			}
			BytesRef bytesRef = null;
			while ((bytesRef = termsEnum.next()) != null) {
				if (termsEnum.seekExact(bytesRef)) {
					String term = bytesRef.utf8ToString();
					float tf = 0;
					DocsEnum docsEnum = termsEnum.docs(null, null);
					while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
						tf = tfidfSIM.tf(docsEnum.freq());
					}
					float idfValue = idf4Subtopic.get(term);
					float w = tf * idfValue;
					tf_Idf_Weights.put(term, w);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}		
		RealVector realVector = new ArrayRealVector(terms.size());
		int i = 0;
		for (String term : terms) {
			float value = tf_Idf_Weights.containsKey(term) ? tf_Idf_Weights
					.get(term) : 0;

			realVector.setEntry(i++, value);
		}
		return (RealVector) realVector.mapDivide(realVector.getL1Norm());
	}
	
	
	/**
	 * Get query tfidf vector by document id
	 * @param docID
	 * @return
	 */
	private RealVector query_get_vector(int docID) {
		Map<String, Float> tf_Idf_Weights = new HashMap<>();
		TermsEnum termsEnum;
		try {
			termsEnum = MultiFields.getTerms(reader,  LuceneConstant.QUERY_CONTENT_FIELD).iterator(
					null);
			Terms vector = reader.getTermVector(docID,  LuceneConstant.QUERY_CONTENT_FIELD);
			try {
				termsEnum = vector.iterator(termsEnum);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			BytesRef bytesRef = null;
			while ((bytesRef = termsEnum.next()) != null) {
				if (termsEnum.seekExact(bytesRef)) {
					String term = bytesRef.utf8ToString();
					float tf = 0;
					DocsEnum docsEnum = termsEnum.docs(null, null);
					while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
						tf = tfidfSIM.tf(docsEnum.freq());
					}
					float idfValue = idf4Query.get(term);
					float w = tf * idfValue;
					tf_Idf_Weights.put(term, w);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}		
		RealVector realVector = new ArrayRealVector(terms.size());
		int i = 0;
		for (String term : terms) {
			float value = tf_Idf_Weights.containsKey(term) ? tf_Idf_Weights
					.get(term) : 0;

			realVector.setEntry(i++, value);
		}
		return (RealVector) realVector.mapDivide(realVector.getL1Norm());
	}
	
	/**
	 * Get document ID by queryid and query field or subtopic id and subtopic field
	 * @param id query/subtopic ID
	 * @param field query/subtopic field
	 * @return
	 */
	private int getDocByID(int id,String field) {
		// TODO Auto-generated method stub
		IndexSearcher searcher = new IndexSearcher(reader);
		Query IDquery;
		int doc=-1;
		try {
			IDquery = NumericRangeQuery.newIntRange(field, id, id, true, true);
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
	
}
