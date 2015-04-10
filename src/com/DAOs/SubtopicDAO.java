package com.DAOs;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.POJO.SubtopicInfo;

public class SubtopicDAO extends GeneralDAO{
	private Session session;

	public SubtopicDAO() {
		// TODO Auto-generated constructor stub
		session = this.openSession();
	}


	/**
	 * Get All subtopic Info
	 * @return
	 */
	public List<SubtopicInfo> getAllSubtopicInfo() {
		// TODO Auto-generated method stub
		try {
			Query q = session.createQuery("from SubtopicInfo");
			return (List<SubtopicInfo>) q.list();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Get subtopic info by subtopic ID
	 * @param subtopicID
	 * @return
	 */
	public SubtopicInfo getSubtopicByID(int subtopicID) {
		// TODO Auto-generated method stub
		try {		
			Query q = session.createQuery("from SubtopicInfo where subtopicID:=subtopicID");
			q.setParameter("subtopicID", subtopicID);
			return (SubtopicInfo) q.uniqueResult();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}

	}
	
	/**
	 * Insert the original subtopic info
	 * @param subtInfo
	 * @return current subtopic ID
	 */
	public int insertNewSubtopicInfo(SubtopicInfo subtInfo) {
		// TODO Auto-generated method stub
		try{
			org.hibernate.Transaction tx = session.beginTransaction();
            session.save(subtInfo);
            tx.commit();
            session.close();
            return subtInfo.getSubtopicId();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
			return -1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}
	
	/**
	 * Update the subtopic description
	 * @param subtInfo
	 */
	public void updateSubtopicDescri(SubtopicInfo subtInfo) {
		// TODO Auto-generated method stub
		try {
			org.hibernate.Transaction tx = session.beginTransaction();
            session.update(subtInfo);
            tx.commit();
		} catch (HibernateException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	
	
	

}
