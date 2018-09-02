package org.com.ramboindustries.corp.sql;

public final class SQLConstants {

	private SQLConstants() {
	}

	public static final String INSERT = " INSERT INTO ";
	public static final String UPDATE = " UPDATE ";
	public static final String FROM = " FROM ";
	public static final String SET = " SET ";
	public static final String WHERE = " WHERE ";
	public static final String VALUES = " VALUES ";
	public static final String EQUAL = " = ";
	public static final String AND = " AND ";

	public static final String updateSQL(String table, String updateScript, String whereConditions) {
		return UPDATE + table + SET + updateScript + whereConditions;
	}

	public static final String insertSQL(String table, String columns, String values) {
		return INSERT + table + "(" + columns + ")" + VALUES + "(" + values + ")";
	}

}
