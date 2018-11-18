package org.com.ramboindustries.corp.sql.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.com.ramboindustries.corp.sql.abstracts.SQLBasicWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLComplexWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLUniqueWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.enums.SQLBasicConditionType;
import org.com.ramboindustries.corp.sql.enums.SQLComplexConditionType;
import org.com.ramboindustries.corp.sql.enums.SQLOperator;
import org.com.ramboindustries.corp.sql.enums.SQLUniqueConditionType;
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

		base.save(user);
		
		user.setLogin("kernelppp");
		user.setName("Carlos");
		base.save(user);
		user.setLogin("kernelPanic_R");
		user.setName("Bolsonaro");
		base.save(user);


		List<SQLWhereCondition> wheres = new ArrayList<>();
		wheres.add(new SQLComplexWhereCondition("USER_ID", 1L, 12l, SQLComplexConditionType.BETWEEN, SQLOperator.AND));
		wheres.add(new SQLBasicWhereCondition("LOGIN", "%kernel%", SQLBasicConditionType.LIKE));
		wheres.add(new SQLUniqueWhereCondition("PASSWORD", SQLUniqueConditionType.IS_NOT_NULL));
		wheres.add(new SQLUniqueWhereCondition("SALARY", SQLUniqueConditionType.IS_NULL));
		
		base.findList(User.class, wheres).forEach(x -> {
			System.out.println(x.getId());
			System.out.println(x.getLogin());
		});;

	}

}
