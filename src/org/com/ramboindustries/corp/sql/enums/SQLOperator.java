package org.com.ramboindustries.corp.sql.enums;

public enum SQLOperator {

	AND(" AND "), OR(" OR ");

	private String operator;

	private SQLOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	public static SQLOperator getOperator(String operator) {
		if (operator == null)
			return null;
		for (SQLOperator ope : SQLOperator.values())
			if (ope.name().equalsIgnoreCase(operator))
				return ope;
		return null;
	}

}
