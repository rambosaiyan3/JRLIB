package org.com.ramboindustries.corp.sql;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
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

				if (field.getType() == String.class) {
					String sqltype = type.getSqlType().substring(0, type.getSqlType().length() - 1);
					return column.name() + " " + sqltype + "(" + column.length() + ") "
							+ (column.required() ? SQLDataDefinition.NOT_NULL : " ");
				} else {
					return column.name() + " " + type.getSqlType()
							+ (column.required() ? SQLDataDefinition.NOT_NULL : " ");
				}
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
			// the class does not have a SQL annotation
			String sqlType = type.getSqlType().substring(0, type.getSqlType().length() - 1);
			if (!field.getType().isAssignableFrom(Boolean.class) && !field.getType().isAssignableFrom(Date.class)) {
				if (field.getType().isAssignableFrom(String.class)) {
					return field.getName() + " " + sqlType + "(" + type.getDefaultSize() + ")";
				} else {
					return field.getName() + " " + sqlType;
				}
			}
			return field.getName() + " " + sqlType;
		}
	}

	/**
	 * Returns the field that is the PK of the class
	 * 
	 * @param clazz that we want to know its PK
	 * @return
	 */
	public static Field getPrimaryKey(final Class<?> CLAZZ) {
		final Field[] FIELDS = CLAZZ.getDeclaredFields();
		for (Field FIELD : FIELDS) {
			if (FIELD.isAnnotationPresent(SQLIdentifier.class)) {
				return FIELD;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <E, V> V getPrimaryKeyValue(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		if(object == null){
			return null;
		}
		
		Field field = getPrimaryKey(object.getClass());
		// if the class does not have an PK
		if (field == null) {
			return null;
		}
		Class<V> clazz = (Class<V>) field.getType();
		return ObjectAccessUtils.<E, V>callGetter(field.getName(), object, clazz);
	}

	/**
	 * Call it to change the name of the PK
	 * 
	 * @param field
	 * @param clazz
	 * @return
	 */
	public static String attributeToSQLColumn(Field field, Class<?> clazz) {
		if (field.isAnnotationPresent(SQLIdentifier.class) && clazz.isAnnotationPresent(SQLInheritancePK.class)) {
			SQLMySqlType type = SQLMySqlType.getSqlType(field.getType());
			if (!field.getType().isAssignableFrom(Date.class) && !field.getType().isAssignableFrom(Boolean.class)) {
				return clazz.getAnnotation(SQLInheritancePK.class).primaryKeyName() + " " + type.getSqlType()
						+ SQLDataDefinition.NOT_NULL + SQLDataDefinition.AUTO_INCREMENT;
			}

			return clazz.getAnnotation(SQLInheritancePK.class).primaryKeyName() + " " + type.getSqlType()
					+ SQLDataDefinition.NOT_NULL + SQLDataDefinition.AUTO_INCREMENT;
		}
		return attributeToSQLColumn(field);

	}

}
