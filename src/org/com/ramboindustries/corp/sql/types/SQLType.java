package org.com.ramboindustries.corp.sql.types;

/**
 * All classes that will be created, need to implement this interface
 * 
 * @author matheus_rambo
 *
 */
public interface SQLType {

	/**
	 * The Java type that represents the SQL data type
	 * Example: 
	 * Java Long MySQL BigInt
	 * Java Long Oracle Number
	 * @return
	 */
	public String getSqlType();
	
	/**
	 * The default size of a field
	 * @return
	 */
	public Integer defaultSize();
}
