package com.DAOs;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.POJO.SubtopicInfo;
import com.POJO.TaskInfo;

public class TaskDAO extends GeneralDAO{

	private Session session;

	public TaskDAO() {
		// TODO Auto-generated constructor stub
		session = this.openSession();
	}
	
	public TaskInfo getTaskInfoByID(int taskID) {
		// TODO Auto-generated method stub
		try {		
			Query q = session.createQuery("from TaskInfo where taskID=:taskID");
			q.setParameter("taskID", taskID);
			return (TaskInfo) q.uniqueResult();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public List<SubtopicInfo> hasSubtopicsID(int taskID) {
		// TODO Auto-generated method stub
		try {				
			Query q = session.createQuery("from SubtopicInfo where taskID=:taskID");
			q.setParameter("taskID", taskID);
			return (List<SubtopicInfo>) q.list();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
}
