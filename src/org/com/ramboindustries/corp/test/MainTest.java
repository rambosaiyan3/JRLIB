package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.utils.ConnectionSQL;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		ConnectionSQL connectionSQL = new ConnectionSQL("jdbc:mysql://localhost:3306/teste", "root", "");
		connectionSQL.createConnection();
		SQLUtils sql = new SQLUtils();
		String s1 = sql.createTableScript(Company.class);
		String ss = (sql.createTableScript(Person.class));
		connectionSQL.createPreparedStatement(ss);
		connectionSQL.getPreparedStatement().executeUpdate();
		System.out.println(ss);
		
	}
		

}
