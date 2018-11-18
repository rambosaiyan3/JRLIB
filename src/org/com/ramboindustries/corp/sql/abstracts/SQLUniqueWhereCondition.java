package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLUniqueConditionType;

public class SQLUniqueWhereCondition extends SQLWhereCondition{

	private SQLUniqueConditionType conditionType;

	public SQLUniqueWhereCondition(String fieldName,  SQLUniqueConditionType conditionType) {
		super(fieldName);
		this.conditionType = conditionType;
	}

	public SQLUniqueConditionType getConditionType() {
		return conditionType;
	}
	
	

	
	
}
