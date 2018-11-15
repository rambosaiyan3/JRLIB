package org.com.ramboindustries.corp.sql.test.dao;

import java.sql.SQLException;
import java.util.List;

import org.com.ramboindustries.corp.sql.JDBCConnection;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConstants;
import org.com.ramboindustries.corp.sql.enums.SQLSystem;
import org.com.ramboindustries.corp.sql.test.entity.BaseEntity;

public class BaseDAO  {

	private final String DATABASE = "teste";
	private final String[] ACCESS = { SQLMySQLConstants.URL_LOCALHOST + DATABASE, "root", "" };

	private JDBCConnection jdbc = null;
	private final boolean SHOW_SQL = true;

	public BaseDAO() {
		jdbc = new JDBCConnection(ACCESS[0], ACCESS[1], ACCESS[2], SQLSystem.MY_SQL);
	}

	public <E extends BaseEntity>  List<E> findAll(Class<E> clazz) throws SQLException {
		return jdbc.selectFrom(clazz, SHOW_SQL);
	}

	public <E extends BaseEntity>  void createTable(Class<E> clazz) throws Exception {
		jdbc.createSQLTable(clazz, SHOW_SQL);
		jdbc.commit();
	}

	public void executeSQL(String SQL) throws SQLException {
		jdbc.executeSQL(SQL);
		jdbc.commit();
	}

	public  <E extends BaseEntity>  E save(E object)  {
		try {
			E obj = jdbc.persistObject(object, SHOW_SQL);
			jdbc.commit();
			return obj;
		} catch (Exception e) {
			//jdbc.rollback();
			e.printStackTrace();
			return null;
		}
	}
	
	public  <E extends BaseEntity>  void update(E object, SQLWhereCondition where) throws Exception {
		jdbc.mergeObject(object, where, SHOW_SQL);
		jdbc.commit();
	}
	
	public <E extends BaseEntity>  void update(E object, List<SQLWhereCondition> where) throws Exception {
		jdbc.mergeObject(object, where, SHOW_SQL);
		jdbc.commit();
	}
	
	public <E extends BaseEntity>  E find(Class<E> clazz, List<SQLWhereCondition> where) throws SQLException {
		return jdbc.findOne(clazz, where, SHOW_SQL);
	}

	public <E extends BaseEntity>   void delete(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		jdbc.deleteObject(clazz, where, SHOW_SQL);
		jdbc.commit();
	}
	
	public <E extends BaseEntity>  E find(Class<E> clazz, SQLWhereCondition where) throws SQLException {
		return jdbc.findOne(clazz, where, SHOW_SQL);
	}
	
}
