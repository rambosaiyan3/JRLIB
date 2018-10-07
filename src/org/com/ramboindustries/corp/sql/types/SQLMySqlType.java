package org.com.ramboindustries.corp.sql.types;

public enum SQLMySqlType {
	
	BYTE("TINYINT ", 4),
	SHORT("SMALLINT " , 4),
	INT("INT ", 4),
	LONG("BIGINT ", 4),
	STRING("VARCHAR ", 10),
	DOUBLE("DOUBLE ", 4),
	BOOLEAN("BOOLEAN "),
	DATE("DATE ");

	private SQLMySqlType(String sqlType, Integer defaultSize) {
		this.sqlType = sqlType;
		this.defaultSize = defaultSize;
	}
	
	private SQLMySqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	private String sqlType;
	private Integer defaultSize;
	

	public String getSqlType() {
		return sqlType;
	}
	
	public Integer getDefaultSize() {
		return defaultSize;
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
