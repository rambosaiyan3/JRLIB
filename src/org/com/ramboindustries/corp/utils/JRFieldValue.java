package org.com.ramboindustries.corp.utils;

/**
 * @author kernelpanic_r
 */
public class JRFieldValue {

	private String fieldName;
	private Object fieldValue;

	public JRFieldValue(String fieldName, Object fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public JRFieldValue() {

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

}
