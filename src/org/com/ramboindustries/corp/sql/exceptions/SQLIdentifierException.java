package org.com.ramboindustries.corp.sql.exceptions;

public class SQLIdentifierException extends Exception{


	private static final long serialVersionUID = -7136599636089999198L;
	private static final String MSG = "You can have only one identifier per class!";
	
	public SQLIdentifierException() {
		super(MSG);
	}
	
	public SQLIdentifierException(String msg) {
		super(msg);
	}

}
