package org.com.ramboindustries.corp.sql.test;

import org.com.ramboindustries.corp.sql.test.dao.BaseDAO;
import org.com.ramboindustries.corp.sql.test.entity.Employer;
import org.com.ramboindustries.corp.sql.test.entity.User;

public class MainTest {

	public static void main(String[] args) throws Exception {
	
		BaseDAO base = new BaseDAO();
		base.createTable(User.class);
		base.createTable(Employer.class);
	}
}
