package org.com.ramboindustries.corp.sql.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;

public final class JDBCUtils {

	private  JDBCUtils() {}
	
	/**
	 * Set the Primary key value to the object
	 * @param object
	 * @param CLAZZ
	 * @param RESULT_SET
	 * @param PRIMARY_KEY
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static <E>  E setPrimaryKeyValue(E object, final Class<?> CLAZZ, final ResultSet RESULT_SET,	final Field PRIMARY_KEY) throws SQLException, Exception {
		Object value = null;
		/**
		 * If the class has the annotation that set the name of the Primary Keys
		 */
		if (CLAZZ.isAnnotationPresent(SQLInheritancePK.class)) {
			value = SQLUtils.getSQLValue(CLAZZ.getAnnotation(SQLInheritancePK.class).primaryKeyName(), RESULT_SET,
					PRIMARY_KEY.getType());
		} else {
			value = SQLUtils.getSQLValue(SQLUtils.getColumnNameFromField(PRIMARY_KEY), RESULT_SET,
					PRIMARY_KEY.getType());
		}
		ObjectAccessUtils.<E>callSetter(object, PRIMARY_KEY.getName(), value);
		return object;
	}
	
	
	
}
