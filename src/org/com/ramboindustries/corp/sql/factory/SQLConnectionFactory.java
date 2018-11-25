package org.com.ramboindustries.corp.sql.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.com.ramboindustries.corp.sql.abstracts.SQLInfo;

public class SQLConnectionFactory {

	public Connection getConnection(SQLInfo sqlConnection) throws SQLException {
		return DriverManager.getConnection(sqlConnection.getUrl(), sqlConnection.getUser(), sqlConnection.getPassword());
	}
	
}
