package org.com.ramboindustries.corp.sql.abstracts;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.com.ramboindustries.corp.exceptions.JRUnexpectedException;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;

/**
 * 
 * @author kernelpanic_r
 *
 */
public interface SQLJdbc {

	public void openConnection() throws SQLException;

	public void closeAll() throws SQLException;

	public void commit() throws SQLException;

	public void rollback() throws SQLException;

	public ResultSet executeSQLSelect(final String SQL) throws SQLException;

	public void executeSQL(final String SQL) throws SQLException;

	public <E> E findOne(final Class<E> CLAZZ, final SQLWhereCondition SQL_WHERE_CONDITION, final boolean SHOW_SQL) throws SQLException, JRUnexpectedException;
	
	public  <E> List<E> selectFrom(final Class<E> CLAZZ, final boolean SHOW_SQL)  throws SQLException;
	
	public <E> List<E> selectFrom(final Class<E> CLAZZ, final SQLWhereCondition SQL_WHERE_CONDITION, final boolean SHOW_SQL) throws SQLException;

	public List<Object[]> selectFrom(final Class<?> CLAZZ, final Field[] COLUMNS, boolean SHOW_SQL) throws SQLException; 
	
}
