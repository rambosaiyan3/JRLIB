package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.system.SQLSystem;
import org.com.ramboindustries.corp.sql.types.SQLMySqlType;
import org.com.ramboindustries.corp.sql.types.SQLType;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * @author kernelpanic_r
 *
 */
public class SQLClassHelper {

	
	/**
	 * Convert an attribute to a column of SQL
	 * 
	 * @param field that will be converted to column
	 * @return a string that represents the column
	 */
	public static String attributeToSQLColumn(final Field FIELD, final SQLSystem SYSTEM) {
		return SQLClassHelper.transformJavaFieldToSqlColumn(FIELD, SYSTEM);
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
		// if the Primary key was not found, we can call it recursively
		if (CLAZZ.getSuperclass() != null && !CLAZZ.getSuperclass().getName().equals(Object.class.getName())) {
			return SQLClassHelper.getPrimaryKey(CLAZZ.getSuperclass());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <E, V> V getPrimaryKeyValue(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		if (object == null) {
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
	public static String attributeToSQLColumn(Field field, Class<?> clazz, final SQLSystem SYSTEM) {
		if (field.isAnnotationPresent(SQLIdentifier.class) && clazz.isAnnotationPresent(SQLInheritancePK.class)) {
			SQLInheritancePK COLUMN = clazz.getAnnotation(SQLInheritancePK.class);
			// set the enum used by user
			SQLType type = SQLClassHelper.setSQLEnum(SYSTEM, field.getType());
			return SQLClassHelper.createPrimaryKeyColumn(COLUMN, type);
		}
		return SQLClassHelper.attributeToSQLColumn(field, SYSTEM);

	}

	private static String transformJavaFieldToSqlColumn(final Field FIELD, final SQLSystem SYSTEM) {
		
		// set the enum used
		SQLType sqlType = SQLClassHelper.setSQLEnum(SYSTEM, FIELD.getType());
		
		if (FIELD.isAnnotationPresent(SQLIdentifier.class)) {

			final SQLIdentifier COLUMN = FIELD.getAnnotation(SQLIdentifier.class);
			return SQLClassHelper.createPrimaryKeyColumn(COLUMN, sqlType);

		} else if (FIELD.isAnnotationPresent(SQLColumn.class)) {

			final SQLColumn COLUMN = FIELD.getAnnotation(SQLColumn.class);

			// like the field is a String we need to set the length
			if (FIELD.getType().isAssignableFrom(String.class))
				// like the column is VARCHAR, we need to set the length of it
				return SQLClassHelper.createColumn(COLUMN, sqlType, true);
			else
				return SQLClassHelper.createColumn(COLUMN, sqlType, false);

		} else if (FIELD.isAnnotationPresent(SQLForeignKey.class)) {

			final SQLForeignKey COLUMN = FIELD.getAnnotation(SQLForeignKey.class);
			// get the field that represents the primary key of the foreign key field
			final Field FOREIGN_KEY = SQLClassHelper.getPrimaryKey(FIELD.getType());
			sqlType = SQLMySqlType.getSqlType(FOREIGN_KEY.getType());
			return SQLClassHelper.createForeignKeyColumn(COLUMN, sqlType);

		} else {
			if (FIELD.getType().isAssignableFrom(String.class) || FIELD.getType().isAssignableFrom(Character.class))
				return SQLClassHelper.createColumn(FIELD, sqlType, true);
			else
				return SQLClassHelper.createColumn(FIELD, sqlType, false);

		}
	}

	// verifies if the column can be null
	private static String isColumnRequired(final SQLColumn COLUMN) {
		return COLUMN.required() ? SQLDataDefinition.NOT_NULL : "";
	}

	// verifies if the foreign can be optional
	private static String isColumnRequired(final SQLForeignKey FOREIGN_KEY) {
		return FOREIGN_KEY.required() ? SQLDataDefinition.NOT_NULL : "";
	}

	// set the column length
	private static String setColumnLength(final SQLColumn COLUMN) {
		return "(" + COLUMN.length() + ")";
	}

	private static String createPrimaryKeyColumn(final SQLIdentifier PRIMARY_KEY, final SQLType SQL_TYPE) {
		if(SQL_TYPE instanceof SQLMySqlType) {
 		return PRIMARY_KEY.identifierName() + " " + SQL_TYPE.getSqlType() + " " + SQLDataDefinition.NOT_NULL
				+ SQLDataDefinition.AUTO_INCREMENT;
		} return null;
	}

	private static String createForeignKeyColumn(final SQLForeignKey FOREIGN_KEY, final SQLType SQL_TYPE) {
		return FOREIGN_KEY.name() + " " + SQL_TYPE.getSqlType() + " " + SQLClassHelper.isColumnRequired(FOREIGN_KEY);
	}

	private static String createColumn(final SQLColumn COLUMN, final SQLType SQL_TYPE, boolean isLength) {
		if (isLength)
			return COLUMN.name() + " " + SQL_TYPE.getSqlType()+ SQLClassHelper.setColumnLength(COLUMN) + " "
					+ SQLClassHelper.isColumnRequired(COLUMN);
		return COLUMN.name() + " " + SQL_TYPE.getSqlType() + " " + SQLClassHelper.isColumnRequired(COLUMN);
	}

	private static String createPrimaryKeyColumn(final SQLInheritancePK PRIMARY_KEY, final SQLType SQL_TYPE) {
		if (SQL_TYPE instanceof SQLMySqlType) {
			return PRIMARY_KEY.primaryKeyName() + " " + SQL_TYPE.getSqlType() + " " + SQLDataDefinition.NOT_NULL
					+ SQLDataDefinition.AUTO_INCREMENT;
		}
		return null;
	}
	
	private static String createColumn(final Field FIELD, final SQLType SQL_TYPE, boolean isLength) {
		if (isLength)
			return FIELD.getName().toUpperCase() + " " + SQL_TYPE.getSqlType() + "( " + SQL_TYPE.defaultSize() + ")";
		return FIELD.getName().toUpperCase() + " " + SQL_TYPE.getSqlType();
	}

	/*
	 * Set the SQLType used by the user
	 */
	private static SQLType setSQLEnum(final SQLSystem SYSTEM, final Class<?> CLAZZ) {
		switch (SYSTEM) {
		case MY_SQL:
			return SQLMySqlType.getSqlType(CLAZZ);
		default:
			return null;
		}
	}
	
}
