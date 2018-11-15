package org.com.ramboindustries.corp.sql;

import java.util.List;

/**
 * Class to work with PreparedStatement
 * @author matheus_rambo
 *
 */
public class SQLJavaStatement {

	private String sql;
	private List<Object> values;
	
	public SQLJavaStatement(String sql, List<Object> values) {
		this.sql = sql;
		this.values = values;
	}
	
	public SQLJavaStatement() {
		
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
	
	
}
