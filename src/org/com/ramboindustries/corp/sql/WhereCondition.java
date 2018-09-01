package org.com.ramboindustries.corp.sql;

public class WhereCondition {
	private String fieldName;
	private Object fieldValue;
	private ConditionType conditionType;

	public WhereCondition(String fieldName, Object fieldValue, ConditionType conditionType) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.conditionType = conditionType;
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

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}


	
}
