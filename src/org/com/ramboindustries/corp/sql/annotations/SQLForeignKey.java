package org.com.ramboindustries.corp.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SQLForeignKey {

	public String name();
	public boolean required() default false;
	public String constraintName() default "";
	public boolean lazyLoad() default true;
}
