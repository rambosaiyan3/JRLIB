package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SQLForeignKey {

	public Class<?> classReferenced();
	public String name();
	public boolean required() default false;
	
}
