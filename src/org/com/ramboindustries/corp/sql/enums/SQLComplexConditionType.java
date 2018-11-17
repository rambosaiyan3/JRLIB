package org.com.ramboindustries.corp.sql.enums;

public enum SQLComplexConditionType {
	
	BETWEEN(" between ");
	
	private String type;
	
	private SQLComplexConditionType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	
	
}
