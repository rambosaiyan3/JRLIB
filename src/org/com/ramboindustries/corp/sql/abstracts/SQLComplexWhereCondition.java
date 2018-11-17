package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLComplexConditionType;
import org.com.ramboindustries.corp.sql.enums.SQLOperator;

public class SQLComplexWhereCondition extends SQLWhereCondition {

	private Object lelfValue;
	private Object rightValue;
	private SQLComplexConditionType conditionType;
	private SQLOperator sqlOperator;

	public SQLComplexWhereCondition(String fieldName, Object lelfValue, Object rightValue,
			SQLComplexConditionType conditionType, SQLOperator sqlOperator) {
		super(fieldName);
		this.lelfValue = lelfValue;
		this.rightValue = rightValue;
		this.conditionType = conditionType;
		this.sqlOperator = sqlOperator;
	}

	public Object getLelfValue() {
		return lelfValue;
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
