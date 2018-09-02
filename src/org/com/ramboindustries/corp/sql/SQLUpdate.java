package org.com.ramboindustries.corp.sql;

public class SQLUpdate {

	private String updateScript;
	private String whereCondition;

	public SQLUpdate(String updateScript, String whereCondition) {
		super();
		this.updateScript = updateScript;
		this.whereCondition = whereCondition;
	}

	public SQLUpdate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUpdateScript() {
		return updateScript;
	}

	public void setUpdateScript(String updateScript) {
		this.updateScript = updateScript;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}

}
