package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLSystem;

public class SQLOracleConnection extends SQLConnection {

	public SQLOracleConnection(final String DATABASE, final String USER, final String PASSWORD) {
		super(null, "jdbc:oracle:thin:@" + DATABASE, USER, PASSWORD, SQLSystem.ORACLE);
	}
	
	private SQLOracleConnection(String driver, String url, String user, String password, SQLSystem system) {
		super(driver, url, user, password, system);
	}

}
