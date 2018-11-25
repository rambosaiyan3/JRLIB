package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.com.ramboindustries.corp.sql.annotations.enums.FetchType;
import org.com.ramboindustries.corp.sql.annotations.enums.Relationship;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SQLForeignKey {

	/**
	 * Name of the foreign key
	 * @return
	 */
	public String name();
	
	/**
	 * The column can be null
	 * @return
	 */
	public boolean required() default false;
	
	/**
	 * Name to the constraint
	 * @return
	 */
	public String constraintName() default "";
	
	/**
	 * If when we find a object, we find for all his relationships
	 * @return
	 */
	public FetchType fetch();
	
	/**
	 * The type of relationship
	 * @return
	 */
	public Relationship relationship ();
	
	
	
}
