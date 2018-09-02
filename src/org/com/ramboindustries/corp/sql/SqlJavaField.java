package org.com.ramboindustries.corp.sql;

public class SqlJavaField {

	private String attributeName;
	private String sqlColumn;
	private Object value;

	public SqlJavaField(String attributeName, String sqlColumn, Object value) {
		super();
		this.attributeName = attributeName;
		this.sqlColumn = sqlColumn;
		this.value = value;
	}

	public SqlJavaField() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
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
