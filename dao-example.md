<<<<<<< HEAD:dao-example.md
public class BaseDAO  {

  # Database name
=======
package org.com.ramboindustries.corp.sql.test.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.com.ramboindustries.corp.sql.JDBCConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.test.entity.BaseEntity;

public class BaseDAO  {
	
	private final String HOST = "localhost";
>>>>>>> dev:src/org/com/ramboindustries/corp/sql/test/dao/BaseDAO.java
	private final String DATABASE = "teste";

  # Class that work with JDBC
	private JDBCConnection jdbc = null;
	private final boolean SHOW_SQL = true;

  # SQLSystem is the SGDB used

	public BaseDAO() {
		jdbc = new JDBCConnection(new SQLMySQLConnection(HOST, DATABASE, "root", ""));
	}

  # With a use of generics, we can have generic methods
  # clazz is the entity 
	public <E extends BaseEntity>  List<E> findAll(Class<E> clazz) throws SQLException {
		return jdbc.select(clazz, SHOW_SQL);
	}

<<<<<<< HEAD:dao-example.md
  # Create a table
=======
	public <E extends BaseEntity>  List<E> findList(Class<E> clazz, List<SQLWhereCondition> WHERE) throws SQLException {
		return jdbc.select(clazz, WHERE, SHOW_SQL);
	}
	
>>>>>>> dev:src/org/com/ramboindustries/corp/sql/test/dao/BaseDAO.java
	public <E extends BaseEntity>  void createTable(Class<E> clazz) throws Exception {
		jdbc.createSQLTable(clazz, SHOW_SQL);
		jdbc.commit();
	}
  
  # Let the user execute his own SQL statements
	public void executeSQL(String SQL) throws SQLException {
		jdbc.executeSQL(SQL);
		jdbc.commit();
	}

	public  <E extends BaseEntity>  Optional<E> save(E object)  {
		try {
			Optional<E> obj = jdbc.insert(object, SHOW_SQL);
			jdbc.commit();
			return obj;
		} catch (Exception e) {
			//jdbc.rollback();
			e.printStackTrace();
			return null;
		}
	}
	
	public  <E extends BaseEntity>  Optional<E> update(E object, SQLWhereCondition where) throws Exception {
		Optional<E> obj = jdbc.update(object, where, SHOW_SQL);
		jdbc.commit();
		return obj;
	}
	
	public <E extends BaseEntity>  Optional<E> update(E object, List<SQLWhereCondition> where) throws Exception {
		Optional<E> obj = jdbc.update(object, where, SHOW_SQL);
		jdbc.commit();
		return obj;
	}
	
<<<<<<< HEAD:dao-example.md
  # Return just a object
	public <E extends BaseEntity>  E find(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
=======
	public  <E extends BaseEntity>  Optional<E> update(E object, Object value) throws Exception {
		Optional<E> obj = jdbc.update(object, value, SHOW_SQL);
		jdbc.commit();
		return obj;
	}
	
	public <E extends BaseEntity>  Optional<E> find(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
>>>>>>> dev:src/org/com/ramboindustries/corp/sql/test/dao/BaseDAO.java
		return jdbc.findOne(clazz, where, SHOW_SQL);
	}

	public <E extends BaseEntity>   void delete(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		jdbc.delete(clazz, where, SHOW_SQL);
		jdbc.commit();
	}
	
	public <E extends BaseEntity>   void delete(Class<E> clazz, Object VALUE) throws SQLException {
		jdbc.delete(clazz, VALUE, SHOW_SQL);
		jdbc.commit();
	}
	
	public <E extends BaseEntity>   void delete(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
		jdbc.delete(clazz, where, SHOW_SQL);
		jdbc.commit();
	}
	
<<<<<<< HEAD:dao-example.md
=======
	
	public <E extends BaseEntity>  Optional<E> find(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		return jdbc.findOne(clazz, where, SHOW_SQL);
	}
	
	public <E extends BaseEntity>  Optional<E> find(Class<E> clazz, Object VALUE) throws SQLException {
		return jdbc.findOne(clazz, VALUE, SHOW_SQL);
	}
	
}
>>>>>>> dev:src/org/com/ramboindustries/corp/sql/test/dao/BaseDAO.java
