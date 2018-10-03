package org.com.ramboindustries.corp.sql.types;

public enum SQLOracleTypes {

	LONG(" NUMBER "), INTEGER(" NUMBER ");

	private SQLOracleTypes(String sqlType) {
		this.sqlType = sqlType;
	}

	private String sqlType;

	public String getSqlType() {
		return sqlType;
	}

}
