package com.DAOs;

import org.hibernate.Session;

public class GeneralDAO {


	
	public Session openSession() {
		// TODO Auto-generated method stub
		return HibernateUtil.getSessionFactory().openSession();
	}
	
	public void close() {
		// TODO Auto-generated method stub
		HibernateUtil.shutdown();
	}
}
