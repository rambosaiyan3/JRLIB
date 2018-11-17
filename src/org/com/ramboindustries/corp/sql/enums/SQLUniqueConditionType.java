package org.com.ramboindustries.corp.sql.enums;

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
