package org.com.ramboindustries.corp.sql.abstracts;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.com.ramboindustries.corp.sql.SQLWhereCondition;

public interface SQLJdbc {

	public void openConnection() throws SQLException;

	public void closeAll() throws SQLException;

	public void commit() throws SQLException;

	public void rollback() throws SQLException;

	public ResultSet executeSQLSelect(final String SQL) throws SQLException;

	public void executeSQL(final String SQL) throws SQLException;
	
	public <E> void createSQLTable(final Class<E> CLAZZ, final boolean SHOW_SQL) throws SQLException;

	public <E> E findOne(final Class<E> CLAZZ, final SQLWhereCondition SQL_WHERE_CONDITION, final boolean SHOW_SQL) throws SQLException;
	
	public <E> E findOne (final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE, final boolean SHOW_SQL) throws SQLException;
	
	public  <E> List<E> selectFrom(final Class<E> CLAZZ, final boolean SHOW_SQL)  throws SQLException;
		
	public <E> List<E> selectFrom(final Class<E> CLAZZ, final SQLWhereCondition SQL_WHERE_CONDITION, final boolean SHOW_SQL) throws SQLException;
	
	public <E> List<E> selectFrom(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE_CONDITIONS,final boolean SHOW_SQL) throws SQLException;

	public List<Object[]> selectFrom(final Class<?> CLAZZ, final Field[] COLUMNS, boolean SHOW_SQL) throws SQLException; 
	
	public List<Object[]> selectFrom(final Class<?> CLAZZ, final Field[] COLUMNS, SQLWhereCondition WHERE_CONDITION, final boolean SHOW_SQL) throws SQLException;
	
	public <E> E persistObject(final E OBJECT, final boolean SHOW_SQL) throws Exception;
	
	public <E> void deleteObject(final Class<E> CLAZZ, final SQLWhereCondition WHERE, final boolean SHOW_SQL) throws SQLException;
	
	public <E> void deleteObject(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE, final boolean SHOW_SQL) throws SQLException;
	
	public <E> E mergeObject(final E OBJECT, final SQLWhereCondition WHERE, final boolean SHOW_SQL) throws Exception;
	
	public <E> E mergeObject(final E OBJECT, final List<SQLWhereCondition> WHERE, final boolean SHOW_SQL) throws Exception;
	


	
}
