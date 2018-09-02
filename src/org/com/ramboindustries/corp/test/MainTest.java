package org.com.ramboindustries.corp.test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.com.ramboindustries.corp.sql.SQLConditionType;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;

public class MainTest {

	public static void main(String[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {

		Employer employer = new Employer(null, "Matheus Rambo", new Date(), 2530.0, false);
		SQLUtils sqlUtils = new SQLUtils();
		String sqlInsert = sqlUtils.createInsertScriptSQL(employer);
		System.out.println(sqlInsert);
		List<SQLWhereCondition> where = new ArrayList<>();
		where.add(new SQLWhereCondition("id", 1L, SQLConditionType.EQUAL));
		where.add(new SQLWhereCondition("name", "Mat%", SQLConditionType.LIKE));
		where.add(new SQLWhereCondition("salary", 2500, SQLConditionType.EQUAL));
		String update = sqlUtils.createUpdateScriptSQL(employer, where);
		System.out.println(update);

		Company company = new Company(5L, "RCA", null);
		String cInsert = sqlUtils.createInsertScriptSQL(company);
		System.out.println(cInsert);
		String update3 = sqlUtils.createUpdateScriptSQL(company, where.get(0));
		System.out.println(update3);

	}

}
