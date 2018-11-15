package org.com.ramboindustries.corp.sql.test;

import java.math.BigDecimal;

import org.com.ramboindustries.corp.sql.test.dao.BaseDAO;
import org.com.ramboindustries.corp.sql.test.dao.UserDAO;
import org.com.ramboindustries.corp.sql.test.entity.User;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		BaseDAO base = new UserDAO();
		User user = new User();
		
		base.executeSQL("DROP DATABASE teste;");
		base.executeSQL("CREATE DATABASE teste;");
		base.executeSQL("use teste;");

		base.createTable(User.class);
		
		user.setLogin("matheus");
		user.setName("Matheus Rambo");
		user.setPassword("1598");
		user.setSalary(new BigDecimal("1.21"));
		
		user = base.save(user);
		System.out.println(user.toString());
	}
	

}
