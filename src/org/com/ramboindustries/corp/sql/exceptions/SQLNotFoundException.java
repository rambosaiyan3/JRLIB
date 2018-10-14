package org.com.ramboindustries.corp.sql.exceptions;

import java.sql.SQLException;

public class SQLNotFoundException extends SQLException {

	private static final long serialVersionUID = 889861602320474139L;

		public SQLNotFoundException(final String MSG) {
			super(MSG);
		}
	
}
