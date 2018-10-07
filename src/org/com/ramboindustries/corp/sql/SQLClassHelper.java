package org.com.ramboindustries.corp.sql;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.types.SQLMySqlType;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

public class SQLClassHelper {

	public static String attributeToSQLColumn(Field field) {

		// return the type, ie INT, VARCHAR, etc..
		SQLMySqlType type = SQLMySqlType.getSqlType(field.getType());

		if (field.isAnnotationPresent(SQLColumn.class)) {
			SQLColumn column = field.getAnnotation(SQLColumn.class);

			if (field.getType() != Boolean.class && field.getType() != Date.class) {
				String sqltype = type.getSqlType().substring(0, type.getSqlType().length() - 1) ;
				return column.name() + " " + sqltype + "(" + column.length() + ") "
						+ (column.required() ? SQLDataDefinition.NOT_NULL : " ");
			}

			return column.name() + " " + type.getSqlType() + (column.required() ? SQLDataDefinition.NOT_NULL : "");

		} else if (field.isAnnotationPresent(SQLIdentifier.class)) {

			return field.getAnnotation(SQLIdentifier.class).identifierName() + " " + type.getSqlType()
					+ SQLDataDefinition.NOT_NULL + SQLDataDefinition.AUTO_INCREMENT;

		} else if (field.isAnnotationPresent(SQLForeignKey.class)) {
			
			// because this field references another object
			// so, we will get the type of the PK of it

			SQLForeignKey foreign = field.getAnnotation(SQLForeignKey.class);
			Field fk = getPrimaryKey(field.getType());
			type = SQLMySqlType.getSqlType(fk.getType());
			return foreign.name() + " " + type.getSqlType() + (foreign.required() ? SQLDataDefinition.NOT_NULL : "");

		} else {

			return field.getName() + " " + type.getSqlType();

		}
	}

	/**
	 * Returns the field that is the PK of the class
	 * @param clazz that we want to know its PK
	 * @return 
	 */
	public static Field getPrimaryKey(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(SQLIdentifier.class)) {
				return field;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <E, V> V getPrimaryKeyValue(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Field field = getPrimaryKey(object.getClass());
		Class<V> clazz = (Class<V>) field.getType();
		return ObjectAccessUtils.<E, V>callGetter(field.getName(), object, clazz);
	}
	

	
}
