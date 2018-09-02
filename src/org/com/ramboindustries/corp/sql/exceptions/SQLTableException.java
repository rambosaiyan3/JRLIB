package org.com.ramboindustries.corp.sql.exceptions;

public class SQLTableException extends Exception {

	private static final long serialVersionUID = -1219763292875801447L;
	private static final String MSG = "Table name not defined on: ";
	
	public SQLTableException(Class<?> clazz) {
		super(MSG + clazz.getSimpleName());
	}
	
	public SQLTableException(String msg) {
		super(msg);
	}
	
}
