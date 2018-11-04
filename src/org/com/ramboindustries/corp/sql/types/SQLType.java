package org.com.ramboindustries.corp.sql.types;

/**
 * All classes that will be created, need to implement this interface
 * 
 * @author matheus_rambo
 *
 */
public interface SQLType {

	public String getSqlType();
	public Integer defaultSize();
}
