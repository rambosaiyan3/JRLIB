package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLBasicConditionType;

public class SQLBasicWhereCondition extends SQLWhereCondition {

	private Object value;
	private SQLBasicConditionType conditionType;

	public SQLBasicWhereCondition(String fieldName , Object value, SQLBasicConditionType conditionType) {
		super(fieldName);
		this.value = value;
		this.conditionType = conditionType;
	}

	public Object getValue() {
		return value;
	}

	public SQLBasicConditionType getConditionType() {
		return conditionType;
	}


}
