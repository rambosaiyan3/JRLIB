package org.com.ramboindustries.corp.sql.exceptions;

import java.sql.SQLException;
import java.util.List;

import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;

public class SQLIdentifierException extends SQLException{


	private static final long serialVersionUID = -7136599636089999198L;
	private static final String MSG = "You can have only one " + SQLIdentifier.class.getSimpleName() +  " identifier per class and super classes!";
	
	public SQLIdentifierException() {
		super(MSG);
	}
	
	public SQLIdentifierException(String msg) {
		super(msg);
	}

	public SQLIdentifierException(List<?> classes) {
		super(createStatement(classes));
		
	}
	
	private static String createStatement(List<?> classes) {
		StringBuilder  msg =  new StringBuilder(" Was not possible to find the " + SQLIdentifier.class.getSimpleName() + " on: ");
		classes.forEach( clazz -> {
			msg.append(((Class<?>)clazz).getSimpleName());
			msg.append(", ");
		});
		msg.delete(msg.lastIndexOf(","), msg.length());
		return msg.toString();
	}
	
}
