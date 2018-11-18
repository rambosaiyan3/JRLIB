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

		// user = base.update(user, new ArrayList<>(
		// Arrays.asList(new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL),
		// new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL)))).get();

		user = new User();
		user.setLogin("kernel");
		user.setName("Mate");
		user.setPassword("fa");

		user = base.save(user).get();

//		base.delete(User.class, new ArrayList<>(Arrays.asList(new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL), new SQLWhereCondition("USER_ID", 1, SQLConditionType.EQUAL))));
		// base.delete(User.class, 1L);

		SQLWhereCondition where = new SQLComplexWhereCondition("USER_ID", 1l, 10L, SQLComplexConditionType.BETWEEN,
				SQLOperator.AND);
		User uu = base.find(User.class, where).get();
		System.out.println(uu.getLogin());

		base.find(User.class, new SQLUniqueWhereCondition("USER_ID", SQLUniqueConditionType.IS_NULL));
		base.find(User.class, new SQLBasicWhereCondition("USER_ID", 1L, SQLBasicConditionType.EQUAL));

		List<SQLWhereCondition> wheres = new ArrayList<>();
		wheres.add(new SQLComplexWhereCondition("USER_ID", 1L, 2l, SQLComplexConditionType.BETWEEN, SQLOperator.AND));
		wheres.add(new SQLBasicWhereCondition("LOGIN", "%kernel", SQLBasicConditionType.LIKE));
		wheres.add(new SQLUniqueWhereCondition("PASSWORD", SQLUniqueConditionType.IS_NOT_NULL));

		base.findList(User.class, wheres);

	}

}
