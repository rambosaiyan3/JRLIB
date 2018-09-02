package org.com.ramboindustries.corp.sql;

public class SQLWhereCondition {

	private String fieldName;
	private Object fieldValue;
	private SQLConditionType conditionType;

	public SQLWhereCondition(String fieldName, Object fieldValue, SQLConditionType conditionType) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.conditionType = conditionType;
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

}
