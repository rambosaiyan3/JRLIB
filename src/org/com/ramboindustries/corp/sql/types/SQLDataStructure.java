package org.com.ramboindustries.corp.sql.types;

import java.util.List;

public interface SQLDataStructure {

	/**
	 * Maybe the increment key word is different from some SGDBS
	 * @return
	 */
	public String increment();
	
	/**
	 * Keywords from SGDB
	 * @return
	 */
	public List<String> keywords();
	
}
