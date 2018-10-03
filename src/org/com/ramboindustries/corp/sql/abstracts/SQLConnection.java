package org.com.ramboindustries.corp.sql.abstracts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.com.ramboindustries.corp.sql.utils.SQLUtils;


public class SQLConnection implements SQLJdbc {

	private final String URL;
	private final String USER;
	private final String PASS;
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;

	public SQLConnection(final String URL, final String USER, final String PASS) {
		this.URL = URL;
		this.USER = USER;
		this.PASS = PASS;
	}

	public Connection getConnection() {
		return connection;
	}

	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}

	public void createConnection() throws SQLException {
		connection = DriverManager.getConnection(URL, USER, PASS);
	}

	public void createPreparedStatement(final String SQL) throws SQLException {
		preparedStatement = connection.prepareStatement(SQL);
	}

	public void closePreparedStatement() throws SQLException {
		preparedStatement.close();
	}

	public void executeSQL() throws SQLException {
		preparedStatement.executeUpdate();
	}

	public void executeSQL(PreparedStatement preparedStatement,
			final Map<Integer, Object> parameters) throws SQLException {
		SQLUtils.setParametersPreparedStatement(preparedStatement, parameters);
		preparedStatement.executeUpdate();
	}

	@Override
	public void createResultSet(PreparedStatement preparedStatement) throws SQLException {
		resultSet = preparedStatement.executeQuery();
	}

	@Override
	public void closeResultSet() throws SQLException {
		resultSet.close();
	}

	public void createResultSet(final String SQL) throws SQLException {
		if(preparedStatement == null) createPreparedStatement(SQL);
		resultSet = preparedStatement.executeQuery();
	}

	
	@Override
	public void closeAll() throws SQLException {
		if (resultSet != null)
			resultSet.close();
		if (preparedStatement != null)
			preparedStatement.close();
		if (connection != null)
			connection.close();
	}

	@Override
	public void commit() throws SQLException {
		connection.commit();
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}
	
	

}
