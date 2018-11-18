package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLComplexConditionType;
import org.com.ramboindustries.corp.sql.enums.SQLOperator;

public class SQLComplexWhereCondition extends SQLWhereCondition {

	private Object leftValue;
	private Object rightValue;
	private SQLComplexConditionType conditionType;
	private SQLOperator sqlOperator;

	public SQLComplexWhereCondition(String fieldName, Object leftValue, Object rightValue,
			SQLComplexConditionType conditionType, SQLOperator sqlOperator) {
		super(fieldName);
		this.leftValue = leftValue;
		this.rightValue = rightValue;
		this.conditionType = conditionType;
		this.sqlOperator = sqlOperator;
	}

	public Object getLeftValue() {
		return leftValue;
	}

	public Object getRightValue() {
		return rightValue;
	}

	public SQLComplexConditionType getConditionType() {
		return conditionType;
	}
	
	public SQLOperator getSQLOperator() {
		return sqlOperator;
	}

	
	
}
