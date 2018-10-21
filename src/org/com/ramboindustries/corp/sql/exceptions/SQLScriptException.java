package org.com.ramboindustries.corp.sql.exceptions;

import java.sql.SQLException;

public class SQLScriptException extends SQLException {

	private static final long serialVersionUID = 5358366133652259611L;

	private String script;

	public SQLScriptException(final String MSG, final String SCRIPT) {
		super(MSG);
		this.script = SCRIPT;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
