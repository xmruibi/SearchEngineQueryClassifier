package com.index.Inject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;

import com.POJO.QueryInfo;
import com.utils.constant.LuceneConstant;

public class QueryExpIndexWritter extends IndexWritter{
	
	public QueryExpIndexWritter(File file) throws IOException {
		super(file);
	}

	/**
	 * Create index for query 
	 * Note: the field for query is different from subtopic
	 * @param query
	 * @throws IOException
	 */
	public void createIndex(QueryInfo query) throws IOException {
		Document document = new Document();
		IntField Id = new IntField(LuceneConstant.QUERY_ID_FIELD, query.getQueryId().intValue(),this.numType);
		Field Expens = new Field(LuceneConstant.QUERY_CONTENT_FIELD, query.getQueryExpension(), this.textType);
		IntField SubtopicNullId = new IntField(LuceneConstant.SUBTOPIC_ID_FIELD, -1,this.numType);
		Field SubtopicNull = new Field(LuceneConstant.SUBTOPIC_CONTENT_FIELD, "", this.textType);
		
		document.add(Id);
		document.add(Expens);
		document.add(SubtopicNullId);
		document.add(SubtopicNull);
		this.indexWriter.addDocument(document);
		this.indexWriter.commit();
		System.out.println("....Query Expension indexing......");
	}

}
