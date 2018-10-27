package org.com.ramboindustries.corp.sql.test;

import static java.lang.System.out;

import java.util.Arrays;
import java.util.List;

import org.com.ramboindustries.corp.sql.SQLConditionType;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.test.dao.BaseDAO;
import org.com.ramboindustries.corp.sql.test.dao.EmployerDAO;
import org.com.ramboindustries.corp.sql.test.dao.UserDAO;
import org.com.ramboindustries.corp.sql.test.entity.Employer;
import org.com.ramboindustries.corp.sql.test.entity.User;

public class MainTest {

	public static void main(String[] args) throws Exception {
		BaseDAO<User> baseUser = new UserDAO();
		BaseDAO<Employer> baseEmployer = new EmployerDAO();
		
	//	baseUser.createTable(User.class);
		baseEmployer.createTable(Employer.class);
		
	    List<User> persist = Arrays.asList(
	    		new User("Matheus Rambo", "rambosaiyan3", "1459"), 
	    		new User("Diego Santos", "dieginho", "584894"), 
	    		new User("Alvaro Dias", "alvinho", "5116")
	    		);
	    
	    persist.forEach(user -> {
	    	baseUser.save(user);
	    });
	    
	    List<Employer> persist1 = Arrays.asList(new Employer("Matheus Rambo", "matheusrambo@gmail.com", new User(1L)), new Employer("Felipe Rambo", "feliperambo.com", new User(2L)));
	    
	    persist1.forEach( user -> {
	    	baseEmployer.save(user);
	    });
	    
	    out.println("\nUSERS\n");
	    
		List<User> users = baseUser.findAll(User.class);
		users.forEach(user -> {
			out.println("\nID: " + user.getId());
			out.println("LOGIN: " + user.getLogin());
			out.println("NAME: " + user.getName());
		});
		
		
	    out.println("\nEMPLOYERS\n");

		List<Employer> employers = baseEmployer.findAll(Employer.class);
		employers.forEach(employer -> {
			out.println("\nID: " +  employer.getId());
			out.println("NAME: " + employer.getName());
			out.println("EMAIL: " + employer.getEmail());
			out.println("User name: " + employer.getUser().getName());
			out.println("User id: " + employer.getUser().getId());
			out.println("User login: " + employer.getUser().getLogin());
		});
		
		List<SQLWhereCondition> where = Arrays.asList(new SQLWhereCondition("NAME", "Alvaro Dias" , SQLConditionType.EQUAL), 
				new SQLWhereCondition("PASSWORD", "5116", SQLConditionType.EQUAL));
		
		User user = baseUser.find(User.class, where);
		out.println(user.getLogin());
		out.println(user.getName());
		
		
	}

	
}
