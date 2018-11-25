package org.com.ramboindustries.corp.sql.enums;

/**
 * Servers that the framework can work
 * @author matheus_rambo
 *
 */
public enum SQLSystem {
	
	MY_SQL("MySQL"),
	ORACLE("Oracle "),
	SQL_SERVER("MSSQL Server");	
	
	private String description;
	
	private  SQLSystem(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
