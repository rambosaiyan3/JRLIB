package org.com.ramboindustries.corp.sql.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
		user = base.save(user).get();
		
		user.setLogin("kernelpanic_r");
		user = base.update(user, 1L).get();
	
		
		user = base.update(user, new ArrayList<>(
				Arrays.asList(new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL),
						new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL)))).get();
		
		user = new User();
		user.setLogin("kernel");
		user.setName("Mate");
		user.setPassword("fa");
		
		user = base.save(user).get();

//		base.delete(User.class, new ArrayList<>(Arrays.asList(new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL), new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL))));
		base.delete(User.class, 1L);
		
		
		
		
		User use1 = base.find(User.class, new ArrayList<>(Arrays.asList(new SQLWhereCondition("USER_ID", null, SQLConditionType.IS_NOT_NULL)))).get();
	
		
		
		
		
	}
	

}
