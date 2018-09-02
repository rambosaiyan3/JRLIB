package org.com.ramboindustries.corp.sql;

public class SqlColumnValue {

	private String columns;
	private String values;

	public SqlColumnValue(String columns, String values) {
		super();
		this.columns = columns;
		this.values = values;
	}

	public SqlColumnValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}
