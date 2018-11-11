package org.com.ramboindustries.corp.sql.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.com.ramboindustries.corp.sql.test.dao.BaseDAO;
import org.com.ramboindustries.corp.sql.test.dao.EmployerDAO;
import org.com.ramboindustries.corp.sql.test.dao.UserDAO;
import org.com.ramboindustries.corp.sql.test.entity.Employer;
import org.com.ramboindustries.corp.sql.test.entity.User;

public class MainTest {

	public static void main(String[] args) throws Exception {
	
		
		
		User user = new User();
		user.setDate(new Date());
		user.setLogin("rambo");
		user.setPassword("1598");
		user.setName("Matheues");
		user.setDate1(LocalDate.now());
		user.setDate33(LocalDateTime.now());
		BaseDAO dao = new UserDAO();
		
		dao.executeSQL("DROP DATABASE teste;");
		dao.executeSQL("CREATE DATABASE teste;");
		dao.executeSQL("USE teste;");
	
		dao.createTable(User.class);
		dao.createTable(Employer.class);

		
		user = dao.save(user);
		
		
		Employer employer = new Employer();
		employer.setEmail("matheus");
		employer.setUser(user);
		employer.setName("ddaa");
		
		BaseDAO da1 = new EmployerDAO();
		employer = da1.save(employer);
		System.out.println(employer.getId());
		System.out.println(employer.getUser());
		
		
		
	}
	
	
}
