package org.com.ramboindustries.corp.sql.abstracts;

import org.com.ramboindustries.corp.sql.enums.SQLSystem;

/**
 * Abstract class to use when initialize the Connection
 * @author matheus_rambo
 *
 */
public abstract class SQLConnection {

	private String driver;
	private String url;
	private String user;
	private String password;
	private SQLSystem system;
	
	public SQLConnection(String driver, String url, String user, String password, SQLSystem system) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.system = system;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SQLSystem getSystem() {
		return system;
	}

	public void setSystem(SQLSystem system) {
		this.system = system;
	}

	
	
}
