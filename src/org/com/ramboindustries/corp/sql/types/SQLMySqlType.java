package org.com.ramboindustries.corp.sql.types;

public enum SQLMySqlType {
	
	BYTE("TINYINT "),
	SHORT("SMALLINT "),
	INT("INT "),
	LONG("BIGINT "),
	STRING("VARCHAR "),
	DOUBLE("DOUBLE "),
	BOOLEAN("BOOLEAN "),
	DATE("DATE ");

	private SQLMySqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	private String sqlType;

	public String getSqlType() {
		return sqlType;
	}
	
	public static SQLMySqlType getSqlType(Class<?> clazz) {
		String sql = clazz.getSimpleName();
		sql = sql.toUpperCase();
		for(SQLMySqlType type : SQLMySqlType.values()) {
			if(type.name().equals(sql)) return type;
		}
		return null;
	}
	
}
