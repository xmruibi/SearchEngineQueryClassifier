package com.preprocess.subtopic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.DAOs.SubtopicDAO;
import com.DAOs.TaskDAO;
import com.POJO.SubtopicInfo;
import com.index.Inject.IndexWritter;
import com.index.Inject.SubtopicIndexWritter;
import com.retrieval.algorithms.DirichletPriorSmth;
import com.utils.constant.LuceneConstant;

/**
 * Subtopic Index set up
 * @author birui
 *
 */
public class SubtopicIndexFactory {


	private  List<SubtopicInfo> sInfoList = new ArrayList<SubtopicInfo>();
	/**
	 * Read from database and get subtopic list by taskID 
	 * Set up the subtopic part in current index
	 * @param taskID Task Selector to choose current task ID
	 */
	public  void setCurrentIndex(int currentTaskID) {
		// TODO Auto-generated method stub	
		TaskDAO tDao = new TaskDAO();
		sInfoList = tDao.hasSubtopicsID(currentTaskID);
		try {
		File file=new File(LuceneConstant.INDEX_PATH);
		if(file.exists()){
			FileUtils.deleteDirectory(file);
		}
		SubtopicIndexWritter indexWritter = new SubtopicIndexWritter(file, sInfoList);
		indexWritter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot create temp index for subtopic: "+e.getMessage());
		}
	}
	
	public List<SubtopicInfo> getsInfoList() {
		return sInfoList;
	}
}
