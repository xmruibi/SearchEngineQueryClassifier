package com.DAOs;

import org.hibernate.Session;

public class QueryDAO extends GeneralDAO{

	private Session session;
	public QueryDAO() {
		// TODO Auto-generated constructor stub
		session = HibernateUtil.getSessionFactory().openSession();
	}
	private void getAllQuery() {
		// TODO Auto-generated method stub

	}
	
	private void insertQueryExpension() {
		// TODO Auto-generated method stub

	}
	
}
