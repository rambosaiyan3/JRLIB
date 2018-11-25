package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When we extends a class, and that class has a SQLIdentifier
 * we do need want the same Primary Key name
 * @author matheus_rambo
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE_USE })
public @interface SQLInheritancePK {

	
	/**
	 * Primary Key name
	 * @return
	 */
	public String primaryKeyName();
	
	/**
	 * Constraint name
	 * @return
	 */
	public String constraintName() default "";
	
}
