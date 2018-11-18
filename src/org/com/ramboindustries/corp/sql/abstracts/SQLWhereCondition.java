package org.com.ramboindustries.corp.sql.abstracts;

public abstract  class SQLWhereCondition {

	private String fieldName;
	
	public SQLWhereCondition(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	
	
}
