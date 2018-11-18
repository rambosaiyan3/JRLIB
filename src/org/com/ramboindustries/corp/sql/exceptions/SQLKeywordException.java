package org.com.ramboindustries.corp.sql.exceptions;

import java.sql.SQLException;

import org.com.ramboindustries.corp.sql.enums.SQLSystem;

public class SQLKeywordException extends SQLException{

	private static final long serialVersionUID = 3788228384419054435L;
	
	public SQLKeywordException(String keyword, SQLSystem system) {
		super(keyword + " is a " + system.getDescription() + " keyword!");
	}
	
}
