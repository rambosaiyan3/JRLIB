package org.com.ramboindustries.corp.sql.abstracts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLJdbc {

	public void openConnection() throws SQLException;

	public void closeAll() throws SQLException;

	public void commit() throws SQLException;

	public void rollback() throws SQLException;

	public ResultSet executeSQLSelect(final String SQL) throws SQLException;

	public ResultSet executeSQLSelect(PreparedStatement preparedStatement) throws SQLException;

	public void executeSQL(final String SQL) throws SQLException;

	public void executeSQL(PreparedStatement preparedStatement) throws SQLException;
}
