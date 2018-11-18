package org.com.ramboindustries.corp.sql.enums;

public enum SQLBasicConditionType {

	EQUAL(" = "),
	DIFFERENT(" != "),
	LIKE(" LIKE "),
	GREATER_THAN(" > "),
	LESS_THAN(" < "),
	GREATER_THAN_OR_EQUAL(" >= "),
	LESS_THAN_OR_EQUAL(" <= ");
	
	private String type;
	
	private SQLBasicConditionType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}


