package org.com.ramboindustries.corp.sql.classsql;

public class SQLJavaField {

	private String sqlColumn;
	private Object value;

	public SQLJavaField(String sqlColumn, Object value) {
		super();
		this.sqlColumn = sqlColumn;
		this.value = value;
	}

	public SQLJavaField() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSqlColumn() {
		return sqlColumn;
	}

	public void setSqlColumn(String sqlColumn) {
		this.sqlColumn = sqlColumn;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
