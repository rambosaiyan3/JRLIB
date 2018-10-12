package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SQLColumn {

	/**
	 * We can use this annotation on a field that we want to map, and then, we can
	 * create the automatic SQL script
	 * 
	 * @return
	 */
	public String name();
	public int length() default 4;
	public boolean required () default false;
	public int precision () default 2;
}
