package org.com.ramboindustries.corp.sql.enums;

public enum SQLConditionType {

	EQUAL(" = "),
	DIFFERENT(" != "),
	LIKE(" LIKE "),
	IS_NULL(" IS NULL "),
	IS_NOT_NULL(" IS NOT NULL "),
	GREATER_THAN(" > "),
	LESS_THAN(" < "),
	GREATER_THAN_OR_EQUAL(" >= "),
	LESS_THAN_OR_EQUAL(" <= ");
	
	private String type;
	
	private SQLConditionType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}


