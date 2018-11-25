package org.com.ramboindustries.corp.sql.enums;

/**
 * ENUM to compare values
 * @author matheus_rambo
 *
 */
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
