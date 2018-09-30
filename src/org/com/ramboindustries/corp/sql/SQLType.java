package org.com.ramboindustries.corp.sql;

public enum SQLType {
	
	BYTE(" TINYINT "),
	SHORT(" SMALLINT "),
	INT(" INT "),
	LONG(" BIGINT "),
	STRING(" VARCHAR "),
	DOUBLE(" DOUBLE "),
	BOOLEAN(" BOOLEAN "),
	DATE(" DATE ");

	private SQLType(String sqlType) {
		this.sqlType = sqlType;
	}

	private String sqlType;

	public String getSqlType() {
		return sqlType;
	}
	
	public static SQLType getSqlType(Class<?> clazz) {
		String sql = clazz.getSimpleName();
		sql = sql.toUpperCase();
		for(SQLType type : SQLType.values()) {
			if(type.name().equals(sql)) return type;
		}
		return null;
	}
	
}
