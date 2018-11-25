package org.com.ramboindustries.corp.sql.connections;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.com.ramboindustries.corp.sql.abstracts.SQLInfo;
import org.com.ramboindustries.corp.sql.abstracts.SQLWhereCondition;

public abstract class JRSQLConnection <E> extends JDBCConnection {

	public JRSQLConnection(SQLInfo sqlConnection, boolean showSql, boolean autoCommit) {
		super(sqlConnection, showSql, autoCommit);
	}

	/**
	 * Abstract method to use with generic
	 * @return
	 */
	protected abstract Class<E> getClazz();
	
	/**
	 * Find by Primary key value
	 * @param primaryKey
	 * @return
	 * @throws SQLException
	 */
	public Optional<E> find(Object primaryKey) throws SQLException{
		return super.findOne(getClazz(), primaryKey);
	}
	
	/**
	 * Find a value by a single where condition
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public Optional<E> findOne(SQLWhereCondition where) throws SQLException{
		return super.findOne(getClazz(), where);
	}
	
	/**
	 * Find a value by N where conditions
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public Optional<E> findOne(List<SQLWhereCondition> where) throws SQLException{
		return super.findOne(getClazz(), where);
	}
	
	/**
	 * Find all values
	 * @return
	 * @throws SQLException
	 */
	public List<E> findAll() throws SQLException{
		return super.select(getClazz());
	}
	
	/**
	 * Find all values from a single where condition
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public List<E> find(SQLWhereCondition where) throws SQLException {
		return super.select(getClazz(), where);
	}
	
	/**
	 * Find all values by N where conditions
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public List<E> find(List<SQLWhereCondition> where) throws SQLException {
		return super.select(getClazz(), where);
	}
	
	/**
	 * Save a object
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Optional<E> save(E object) throws Exception {
		return super.insert(object);
	}
	
	
	/**
	 * Generates a table 
	 * @throws SQLException
	 */
	public void createTable() throws SQLException {
		super.createSQLTable(getClazz());
	}
	
	/**
	 * Delete a object by his primary key
	 * @param primaryKey
	 * @throws SQLException
	 */
	public void delete(Object primaryKey) throws SQLException {
		super.delete(getClazz(), primaryKey);
	}
	
	/**
	 * Delete objects by a single where condition
	 * @param where
	 * @throws SQLException
	 */
	public void delete(SQLWhereCondition where) throws SQLException {
		super.delete(getClazz(), where);
	}
	
	/**
	 * Delete object by N where condtitions
	 * @param where
	 * @throws SQLException
	 */
	public void delete(List<SQLWhereCondition> where) throws SQLException {
		super.delete(getClazz(), where);
	}
	
	/**
	 * Merge an object by primary key
	 * @param object
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public Optional<E> merge(E object, Object primaryKey) throws Exception{
		return super.update(object, primaryKey);
	}
	
	public Optional<E> merge(E object, SQLWhereCondition where) throws Exception{
		return super.update(object, where);
	}

	public Optional<E> merge(E object, List<SQLWhereCondition> where) throws Exception{
		return super.update(object, where);
	}

		

}
