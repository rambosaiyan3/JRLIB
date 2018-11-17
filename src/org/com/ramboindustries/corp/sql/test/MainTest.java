package org.com.ramboindustries.corp.sql.test;

import java.math.BigDecimal;
import java.util.List;

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
		user.setSalary(new BigDecimal("10.21"));
		
		user = base.save(user).get();
		
		user.setLogin("kernelpanic_r");
		user = base.update(user, 1L).get();
	
		user = new User();
		user.setLogin("kernel");
		user.setName("Mate");
		user.setPassword("fa");
		
		user = base.save(user).get();

	//	base.delete(User.class, 1L);
		
		List<User> users = base.findAll(User.class);
		users.forEach(usr -> {
			System.out.println(usr.getName());
			System.out.println(usr.getId());
		});
		
		
	}
	

}
