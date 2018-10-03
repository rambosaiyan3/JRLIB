package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE })
public @interface SQLTable {

	/**
	 * you will use this at the top of your class
	 */
	public String table();
	
	/**
	 * If there is a table with the name, will drop and create again
	 */
	public boolean dropTableIfExists() default false;

}
