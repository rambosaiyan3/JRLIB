package org.com.ramboindustries.corp.sql.enums;

public enum SQLConditionType {

	EQUAL(" = "),
	LIKE(" LIKE "),
	BETWEEN(" BETWEEN "),
	IS_NULL(" IS NULL "),
	GREATER_THAN(" > "),
	LESS_THAN(" < "),
	GREATER_THAN_OR_EQUAL(" >= "),
	LESS_THAN_OR_EQUAL(" <= "),
	IN(" IN ");
	
	private String type;
	
	private SQLConditionType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}


