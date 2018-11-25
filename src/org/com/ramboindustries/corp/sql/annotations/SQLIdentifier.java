package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.com.ramboindustries.corp.sql.annotations.enums.SQLIdentifierType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SQLIdentifier {

	/**
	 * Name of the primary key
	 * @return
	 */
	public String identifierName();
	
	/**
	 * Name of the constraint
	 * @return
	 */
	public String constraintName() default "";
	
	/**
	 * The type of INCREMENT
	 * @return
	 */
	public SQLIdentifierType identifierType() default SQLIdentifierType.IDENTITY;
	
}


 




