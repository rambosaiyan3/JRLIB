package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLColumn {

	/**
	 * define the column name
	 * @return
	 */
	public String name();
	
	/**
	 * Length of the column
	 * @return
	 */
	public int length() default 4;
	
	/**
	 * The field can be null
	 * @return
	 */
	public boolean required () default false;
	
	/**
	 * Size after comma
	 * @return
	 */
	public int precision () default 2;
}
