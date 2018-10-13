package org.com.ramboindustries.corp.sql.abstracts;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.com.ramboindustries.corp.sql.SQLClassHelper;
import org.com.ramboindustries.corp.text.Type;


public abstract class SQLConnection implements SQLJdbc {

	private final String URL;
	private final String USER;
	private final String PASS;
	private Connection connection;

	public SQLConnection(final String URL, final String USER, final String PASS) {
		this.URL = URL;
		this.USER = USER;
		this.PASS = PASS;
	}
	
	@Override
	public void openConnection() throws SQLException {
		connection = DriverManager.getConnection(URL, USER, PASS);
	}


	@Override
	public void closeAll() throws SQLException {
		if(connection != null) connection.close();
	}

	@Override
	public void commit() throws SQLException {
		connection.commit();
	}

	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}

	@Override
	public ResultSet executeSQLSelect(final String SQL) throws SQLException {
		if(connection == null) 
			this.openConnection();
		return connection.prepareStatement(SQL).executeQuery();
	}

	@Override
	public ResultSet executeSQLSelect(PreparedStatement preparedStatement) throws SQLException{
		return preparedStatement.executeQuery();
	}

	@Override
	public void executeSQL(String SQL) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeSQL(PreparedStatement preparedStatement) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the value of the SQLColumn
	 * @param name column name
	 * @param resultSet
	 * @param clazz type of object that will be converted
	 * @return
	 * @throws SQLException
	 */
	protected Object getSQLValue( String name, ResultSet resultSet,  Class<?> clazz) throws SQLException {
		 Type type = Type.getTypeByName(clazz.getSimpleName());	
		if(type == null) {
			Field field = SQLClassHelper.getPrimaryKey(clazz);
			type = Type.getTypeByName(field.getType().getSimpleName());
			clazz = field.getType();
		}
		
		switch (type) {
		case BYTE:
			return clazz.cast(resultSet.getByte(name));
		case SHORT:
			return clazz.cast(resultSet.getShort(name));
		case INTEGER:
			return clazz.cast(resultSet.getInt(name));
		case LONG:
			return clazz.cast(resultSet.getLong(name));
		case FLOAT:
			return clazz.cast(resultSet.getFloat(name));
		case DOUBLE:
			return clazz.cast(resultSet.getDouble(name));
		case BIG_DECIMAL:
			return clazz.cast(resultSet.getBigDecimal(name));
		case BOOLEAN:
			return clazz.cast(resultSet.getBoolean(name));
		case STRING:
			return clazz.cast(resultSet.getString(name));
		default:
			return null;
		}
	}
	
	public abstract <E> List<E> selectFrom(final Class<E> CLAZZ, final boolean SHOW_SQL)  throws SQLException;
	
	
}
