package org.com.ramboindustries.corp.sql;

import org.com.ramboindustries.corp.sql.enums.SQLConditionType;
import org.com.ramboindustries.corp.sql.enums.SQLOperator;

public class SQLWhereCondition {

	private String fieldName;
	private Object fieldValue;
	private SQLConditionType conditionType;
	private SQLOperator operator;

	public SQLWhereCondition(String fieldName, Object fieldValue, SQLConditionType conditionType) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.conditionType = conditionType;
		this.operator = operator == null ? SQLOperator.AND : operator;
	}
	
	public SQLWhereCondition(String fieldName, Object fieldValue, SQLConditionType conditionType,
			SQLOperator operator) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.conditionType = conditionType;
		this.operator = operator == null ? SQLOperator.AND : operator;
	}

	public SQLWhereCondition() {

	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public SQLConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(SQLConditionType conditionType) {
		this.conditionType = conditionType;
	}

	public SQLOperator getOperator() {
		return operator;
	}

	public void setOperator(SQLOperator operator) {
		this.operator = operator;
	}

}
