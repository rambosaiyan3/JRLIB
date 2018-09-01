package org.com.ramboindustries.corp.sql.exceptions;

import java.lang.reflect.Field;

public class SqlColumnException extends Exception {

	private static final long serialVersionUID = 7657682374249570870L;
	private static final String MSG = "Invalid Column name ";

	public SqlColumnException(String msg) {
		super(msg);
	}
	
	public SqlColumnException(Field field) {
		super(MSG + " for the field: " + field);
	}
	
	
}
