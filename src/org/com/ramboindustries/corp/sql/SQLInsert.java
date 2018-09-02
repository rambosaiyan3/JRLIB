package org.com.ramboindustries.corp.sql;

/**
 * Use this to create the SQL script
 * @author kernelpanic_r
 *
 */
public class SQLInsert {

	private String columns;
	private String values;

	public SQLInsert(String columns, String values) {
		super();
		this.columns = columns;
		this.values = values;
	}

	public SQLInsert() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns.substring(0, columns.length() - 1);
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values.substring(0, values.length() - 1);
	}

}
