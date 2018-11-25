package org.com.ramboindustries.corp.sql.connections;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.com.ramboindustries.corp.sql.abstracts.SQLInfo;
import org.com.ramboindustries.corp.sql.abstracts.SQLWhereCondition;

public class JRGenericSQLConnection extends JDBCConnection {

	public JRGenericSQLConnection(SQLInfo sqlConnection, boolean SHOW_SQL, boolean autoCommit) {
		super(sqlConnection, SHOW_SQL, autoCommit);
	}

	/**
	 * Find by Primary key value
	 * 
	 * @param primaryKey
	 * @return
	 * @throws SQLException
	 */
	public <E> Optional<E> find(Class<E> clazz, Object primaryKey) throws SQLException {
		return super.findOne(clazz, primaryKey);
	}

	/**
	 * Find a value by a single where condition
	 * 
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public <E> Optional<E> findOne(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		return super.findOne(clazz, where);
	}

	/**
	 * Find a value by N where conditions
	 * 
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public <E> Optional<E> findOne(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
		return super.findOne(clazz, where);
	}

	/**
	 * Find all values
	 * 
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> findAll(Class<E> clazz) throws SQLException {
		return super.select(clazz);
	}

	/**
	 * Find all values from a single where condition
	 * 
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> find(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		return super.select(clazz, where);
	}

	/**
	 * Find all values by N where conditions
	 * 
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public <E> List<E> find(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
		return super.select(clazz, where);
	}

	/**
	 * Save a object
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public <E> Optional<E> save(E object) throws Exception {
		return super.insert(object);
	}

	/**
	 * Generates a table
	 * 
	 * @throws SQLException
	 */
	public <E> void createTable(Class<E> clazz) throws SQLException {
		super.createSQLTable(clazz);
	}

	/**
	 * Delete a object by his primary key
	 * 
	 * @param primaryKey
	 * @throws SQLException
	 */
	public <E> void delete(Class<E> clazz, Object primaryKey) throws SQLException {
		super.delete(clazz, primaryKey);
	}

	/**
	 * Delete objects by a single where condition
	 * 
	 * @param where
	 * @throws SQLException
	 */
	public <E> void delete(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		super.delete(clazz, where);
	}

	/**
	 * Delete object by N where condtitions
	 * 
	 * @param where
	 * @throws SQLException
	 */
	public <E> void delete(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
		super.delete(clazz, where);
	}

	/**
	 * Merge an object by primary key
	 * 
	 * @param object
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public <E> Optional<E> merge(E object, Object primaryKey) throws Exception {
		return super.update(object, primaryKey);
	}

	public <E> Optional<E> merge(E object, SQLWhereCondition where) throws Exception {
		return super.update(object, where);
	}

	public <E> Optional<E> merge(E object, List<SQLWhereCondition> where) throws Exception {
		return super.update(object, where);
	}

}
