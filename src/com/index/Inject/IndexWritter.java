package com.index.Inject;


import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * The parent class for all index writers 
 * @author birui
 *
 */
public class IndexWritter {
	
	protected Directory directory;
	protected IndexWriter indexWriter;
	
	//filed type for text content
	protected FieldType textType = new FieldType(); 
	// field type for numeric ID value
	protected FieldType numType = new FieldType(); 

	/**
	 *  Input file as directory for index
	 *  it uses the FSDirectory which is saving in hardware
	 * @param file
	 * @throws IOException
	 */
	public IndexWritter(File file) throws IOException {
		// TODO Auto-generated method stub
		this.directory = FSDirectory.open(file);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
				Version.LUCENE_46, new com.utils.nlp.PorterStemAnalyzer());
		this.indexWriter = new IndexWriter(directory, indexWriterConfig);
		
		this.textType.setIndexed(true);
		this.textType.setStored(true);
		this.textType.setTokenized(true);
		this.textType.setStoreTermVectors(true);
		
		this.numType.setIndexed(true);
		this.numType.setStored(true);
		this.numType.setNumericType(NumericType.INT);
		this.numType.setTokenized(false);
		this.numType.setStoreTermVectors(false);
	}
	

	
	public void close() throws IOException {
		// TODO Auto-generated method stub
		indexWriter.close();
	}


}
