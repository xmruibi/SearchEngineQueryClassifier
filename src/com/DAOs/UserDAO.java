package com.DAOs;

import org.hibernate.Session;

public class UserDAO extends GeneralDAO{

	private Session session;
	public UserDAO() {
		// TODO Auto-generated constructor stub
		session = this.openSession();
	}
	
	private void getUserInfo() {
		// TODO Auto-generated method stub

	}
	
}
