package org.com.ramboindustries.corp.sql.abstracts;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLJdbc {

	public void createConnection() throws SQLException;

	public void createPreparedStatement(final String SQL) throws SQLException;

	public void createResultSet(PreparedStatement preparedStatement) throws SQLException;
	
	public void createResultSet(final String SQL) throws SQLException;

	public void closeConnection() throws SQLException;

	public void closePreparedStatement() throws SQLException;

	public void closeResultSet() throws SQLException;

	public void closeAll() throws SQLException;

	public void commit() throws SQLException;
	
	public void rollback() throws SQLException;
}
