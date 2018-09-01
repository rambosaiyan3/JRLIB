package org.com.ramboindustries.corp.sql.exceptions;

public class SqlTableException extends Exception {

	private static final long serialVersionUID = -1219763292875801447L;
	private static final String MSG = "Table name not defined on: ";
	
	public SqlTableException(Class<?> clazz) {
		super(MSG + clazz.getSimpleName());
	}
	
	public SqlTableException(String msg) {
		super(msg);
	}
	
}
