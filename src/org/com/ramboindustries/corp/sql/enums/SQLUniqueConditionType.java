package org.com.ramboindustries.corp.sql.enums;

/**
 * ENUM that does not receive a value from user
 * @author matheus_rambo
 *
 */
public enum SQLUniqueConditionType {

	IS_NOT_NULL(" IS NOT NULL "), IS_NULL(" IS NULL ");

	private String type;

	private SQLUniqueConditionType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
