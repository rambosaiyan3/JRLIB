package org.com.ramboindustries.corp.sql.test.dao;

import java.sql.SQLException;
import java.util.List;

import org.com.ramboindustries.corp.sql.JDBCConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConstants;
import org.com.ramboindustries.corp.sql.test.entity.BaseEntity;

public class BaseDAO<E extends BaseEntity> {

	private final String DATABASE = "sistema";
	private final String[] ACCESS = { SQLMySQLConstants.URL_LOCALHOST + DATABASE, "root", "" };

	private JDBCConnection jdbc = null;
	private final boolean SHOW_SQL = true;

	public BaseDAO() {
		jdbc = new JDBCConnection(ACCESS[0], ACCESS[1], ACCESS[2]);
	}

	public List<E> findAll(Class<E> clazz) throws SQLException {
		return jdbc.selectFrom(clazz, SHOW_SQL);
	}

	public void createTable(Class<E> clazz) throws Exception {
		jdbc.createSQLTable(clazz, SHOW_SQL);
		jdbc.commit();
	}

	public void executeSQL(String SQL) throws SQLException {
		jdbc.executeSQL(SQL);
		jdbc.commit();
	}

	public void save(E object)  {
		try {
			jdbc.persistObject(object, SHOW_SQL);
			jdbc.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
