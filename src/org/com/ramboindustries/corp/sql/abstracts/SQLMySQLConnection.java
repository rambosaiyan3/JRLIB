package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLSystem;

public class SQLMySQLConnection extends SQLConnection  {

	private final static int PORT = 3306;
	private final static String URL = "jdbc:mysql://";
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	
	public SQLMySQLConnection(final String SERVER, final String DATABASE, final String USER, final String PASSWORD) {
		this(DRIVER, createURL(SERVER, DATABASE, PORT), USER, PASSWORD, SQLSystem.MY_SQL);
	}
	
	public SQLMySQLConnection(final String SERVER, final String DATABASE, final String USER, final String PASSWORD, final int PORT) {
		this(DRIVER, createURL(SERVER, DATABASE, PORT), USER, PASSWORD, SQLSystem.MY_SQL);
	}
	
	
	private SQLMySQLConnection(String driver, String url, String user, String password, SQLSystem system) {
		super(driver, url, user, password, system);
	}

	private static String createURL(final String SERVER, final String DATABASE, final int PORT) {
		return URL + SERVER + ":" + PORT + "/" + DATABASE;
	}
	
}
