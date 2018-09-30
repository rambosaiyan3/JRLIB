package org.com.ramboindustries.corp.sql;

import java.util.List;

public class SQLClassTable {

	private String tableName;
	private List<SQLClassColumn> columns;

	public SQLClassTable() {
		
	}
	
	public SQLClassTable(String tableName, List<SQLClassColumn> columns) {
		super();
		this.tableName = tableName;
		this.columns = columns;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<SQLClassColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<SQLClassColumn> columns) {
		this.columns = columns;
	}

}
