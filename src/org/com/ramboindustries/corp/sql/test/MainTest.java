package org.com.ramboindustries.corp.sql.test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.com.ramboindustries.corp.sql.JDBCConnection;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.enums.SQLConditionType;
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
		
		user = base.save(user);
		System.out.println(user.getLogin());
		
		user.setLogin("kernelpanic_r");
		user.setName("MatheusZinho");
		List<SQLWhereCondition> where = Arrays.asList(new SQLWhereCondition("USER_ID", 1L, SQLConditionType.EQUAL), 
				new SQLWhereCondition("SALARY", 8, SQLConditionType.GREATER_THAN_OR_EQUAL));
		
		base.update(user, where);
		System.out.println(user.getLogin());
		
		System.out.println(base.find(User.class, where.get(0)));
		
		
	}
	

}
