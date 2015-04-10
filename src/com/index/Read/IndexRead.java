package com.index.Read;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 
 * @author birui
 * 
 *         Basic Lucene Index Reader setup
 *
 */
public class IndexRead {

	private Directory directory;
	private IndexReader indexReader;

	/**
	 * Constructor
	 * 
	 * @param file
	 * @throws IOException
	 */
	public IndexRead(File file) throws IOException {
		// TODO Auto-generated method stub
		this.directory = FSDirectory.open(file);
		this.indexReader = DirectoryReader.open(directory);
	}

	/**
	 * Get this index reader
	 * 
	 * @return indexReader
	 */
	public IndexReader getReader() {
		return this.indexReader;
	}

	/**
	 * Close the reader
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		// TODO Auto-generated method stub
		this.indexReader.close();
	}

}
