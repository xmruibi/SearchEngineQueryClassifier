package com.index.Inject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;

import com.POJO.SubtopicInfo;
import com.utils.constant.LuceneConstant;

public class SubtopicIndexWritter extends IndexWritter {

	/**
	 * Insert all subtopics under a certain task into current index
	 * @param file file for index
	 * @param topics all subtopics as a list undex current task
	 * @throws IOException
	 */
	public SubtopicIndexWritter(File file, List<SubtopicInfo> topics)
			throws IOException {
		super(file);
		for (SubtopicInfo info : topics) {
			createIndex(info);
		}
		System.out.println("Subtopic Model Index set up successfully!");
	}

	/**
	 *  Create index for subtopic 
	 * @param topic
	 * @throws IOException
	 */
	public void createIndex(SubtopicInfo topic) throws IOException {
		Document document = new Document();
		IntField Id = new IntField(LuceneConstant.SUBTOPIC_ID_FIELD, topic.getSubtopicId().intValue(),this.numType);
		//Field UserTag = new Field(LuceneConstant.SUBTOPIC_USERTAG_FILED);
		Field Expens = new Field(LuceneConstant.SUBTOPIC_CONTENT_FIELD, topic.getDescription(), this.textType);
		
		IntField QueryNullId = new IntField(LuceneConstant.QUERY_ID_FIELD, -1,this.numType);
		Field QueryNull = new Field(LuceneConstant.QUERY_CONTENT_FIELD, "", this.textType);

		document.add(Id);
		document.add(Expens);
		document.add(QueryNullId);
		document.add(QueryNull);
		this.indexWriter.addDocument(document);
		this.indexWriter.commit();
	}
}
