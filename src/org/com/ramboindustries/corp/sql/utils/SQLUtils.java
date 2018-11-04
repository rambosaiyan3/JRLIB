package org.com.ramboindustries.corp.sql.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.com.ramboindustries.corp.sql.SQLJavaField;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.commands.SQLDataManipulation;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;
import org.com.ramboindustries.corp.text.Type;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * Utility to work with SQL
 * @author kernelpanic_r
 */
public final class SQLUtils {

	private SQLUtils() {}
	
	private static String convertToString(Object value) {
		if (value == null)
			return null;
		else if (value instanceof String || value instanceof java.util.Date)
			return "'" + value.toString() + "'";
		else
			return value.toString();
	}

	protected static Map<String, String> mapAttributes(final Set<SQLJavaField> SQL_JAVA) {
		Map<String, String> map = new HashMap<>();
		SQL_JAVA.forEach(item -> {
			map.put(item.getSqlColumn(), SQLUtils.convertToString(item.getValue()));
		});
		return map;
	}

	protected static String createWhereCondition(final SQLWhereCondition WHERE_CONDITION) {
		return SQLDataManipulation.WHERE + WHERE_CONDITION.getFieldName() + " " + 
				WHERE_CONDITION.getConditionType().getType() + " " + 
				(WHERE_CONDITION.getFieldValue() instanceof String ?  "'" + WHERE_CONDITION.getFieldValue() + "'" : WHERE_CONDITION.getFieldValue()) ;
			
	}

	protected static String createWhereCondition(final List<SQLWhereCondition> WHERE_CONDITION) {
		StringBuilder builder = new StringBuilder(" " + SQLDataManipulation.WHERE_TRUE);
		WHERE_CONDITION.forEach(WHERE -> {
			builder.append( SQLDataManipulation.AND + 
					WHERE.getFieldName() + " " +
					WHERE.getConditionType().getType() + " " + 
					(WHERE.getFieldValue() instanceof String ? "'" + WHERE.getFieldValue() + "'" : WHERE.getFieldValue()));
		});
		return builder.toString();
	}

	protected static String createFieldsToSelect(final Field[] COLUMNS) {
		StringBuilder builder = new StringBuilder(" ");
		for (final Field FIELD : COLUMNS) {
			builder.append(SQLUtils.getColumnNameFromField(FIELD) + ", ");
		}
		builder.delete(builder.lastIndexOf(","), builder.length());
		return builder.toString();
	}

	/**
	 * Gets the name of the table on DB
	 * 
	 * @param clazz that represents the table
	 * @return table name
	 */
	protected static String getTableName(Class<?> clazz) {
		return clazz.isAnnotationPresent(SQLTable.class) ? clazz.getAnnotation(SQLTable.class).table()
				: clazz.getSimpleName();
	}

	/**
	 * Creates a Primary Key constraint for the table
	 * 
	 * @param constraint the name of constraint
	 * @param field      the field that is the primary key
	 * @return a String that contains the line
	 */
	protected static String createPrimaryKeyConstraint(String constraint, Field field, Class<?> clazz) {
		String name = null;
		if (clazz.isAnnotationPresent(SQLInheritancePK.class))
			name = clazz.getAnnotation(SQLInheritancePK.class).primaryKeyName();
		else
			name = field.getAnnotation(SQLIdentifier.class).identifierName();
		return SQLDataDefinition.CONSTRAINT + " " + constraint + " " + SQLDataDefinition.PRIMARY_KEY + "(" + name + ")";
	}

	/**
	 * Creates a Foreign Key constraint for the table
	 * 
	 * @param constraint      the name of constraint
	 * @param field           the field that is the foreign key
	 * @param clazzReferenced class that has the primary key
	 * @return a String that contains the line
	 */
	protected static String createForeignKeyConstraint(String constraint, Field field, Class<?> clazzReferenced) {
		String fieldReferenced = null;
		if (clazzReferenced.isAnnotationPresent(SQLInheritancePK.class)) {
			// if the class has this annotation
			fieldReferenced = clazzReferenced.getAnnotation(SQLInheritancePK.class).primaryKeyName();
		} else {
			fieldReferenced = SQLClassHelper.getPrimaryKey(clazzReferenced).getAnnotation(SQLIdentifier.class)
					.identifierName();
		}
		return SQLDataDefinition.CONSTRAINT + constraint + SQLDataDefinition.FOREIGN_KEY + "("
				+ field.getAnnotation(SQLForeignKey.class).name() + ")" + SQLDataDefinition.REFERENCES
				+ getTableName(clazzReferenced) + "(" + fieldReferenced + ")";
	}

	public static List<Field> allFieldsToTable(Class<?> clazz) throws SQLIdentifierException {
		List<Field> fields = new ArrayList<>();

		// gets all the superclass from the clazz
		List<Class<?>> classes = ObjectAccessUtils.getSuperclassesFromClass(clazz, false);
		if (classes != null && !classes.isEmpty()) {

			// reverse the list, to get the fields of superclass first
			Collections.reverse(classes);

			// we add the fields to the top
			classes.forEach(classe -> {
				fields.addAll(Arrays.asList(classe.getDeclaredFields()));
			});
		}

		// we add the fields from clazz
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		// we set the primary key for the first element
		classes.add(clazz);
		SQLUtils.setPrimaryKeyPosition(fields, classes);

		return fields;
	}

	private static void setPrimaryKeyPosition(List<Field> fields, List<?> classes) throws SQLIdentifierException {
		int position = 0;
		byte number = 0;
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).isAnnotationPresent(SQLIdentifier.class)) {
				++number;

				// saves the position of the primary key
				position = i;
			}
			// if there is more than a primary key
			if (number > 1) {
				throw new SQLIdentifierException();
			}
		}
		// if there is not a primary key
		if (number == 0) {
			throw new SQLIdentifierException(classes);
		}
		// we get the field that is the primary key
		Field primaryKey = fields.get(position);

		// we remove him from the list
		fields.remove(position);

		// we add him to the first position
		fields.add(0, primaryKey);
	}

	public static String getColumnNameFromField(final Field FIELD) {
		if (FIELD.isAnnotationPresent(SQLIdentifier.class))
			return FIELD.getAnnotation(SQLIdentifier.class).identifierName();
		else if (FIELD.isAnnotationPresent(SQLColumn.class))
			return FIELD.getAnnotation(SQLColumn.class).name();
		else if (FIELD.isAnnotationPresent(SQLForeignKey.class))
			return FIELD.getAnnotation(SQLForeignKey.class).name();
		else
			return FIELD.getName();
	}

	public static boolean isFieldRelationship(final Field FIELD) {
		return FIELD.isAnnotationPresent(SQLForeignKey.class);
	}

	/**
	 * Returns the value of the SQLColumn
	 * 
	 * @param name      column name
	 * @param resultSet
	 * @param clazz     type of object that will be converted
	 * @return
	 * @throws SQLException
	 */
	public static Object getSQLValue(String name, ResultSet resultSet, Class<?> clazz) throws SQLException {
		Type type = Type.getTypeByName(clazz.getSimpleName());
		if (type == null) {
			Field field = SQLClassHelper.getPrimaryKey(clazz);
			type = Type.getTypeByName(field.getType().getSimpleName());
			clazz = field.getType();
		}

		switch (type) {
		case BYTE:
			return clazz.cast(resultSet.getByte(name));
		case SHORT:
			return clazz.cast(resultSet.getShort(name));
		case INTEGER:
			return clazz.cast(resultSet.getInt(name));
		case LONG:
			return clazz.cast(resultSet.getLong(name));
		case FLOAT:
			return clazz.cast(resultSet.getFloat(name));
		case DOUBLE:
			return clazz.cast(resultSet.getDouble(name));
		case BIG_DECIMAL:
			return clazz.cast(resultSet.getBigDecimal(name));
		case BOOLEAN:
			return clazz.cast(resultSet.getBoolean(name));
		case STRING:
			return clazz.cast(resultSet.getString(name));
		default:
			return null;
		}
	}

	/**
	 * Gets the name of a Primary Key
	 * @param CLAZZ that represents the table
	 * @return the name of Primary Key
	 * @throws SQLIdentifierException if we do not find a primary key
	 */
	public static String getPrimaryKeyName(final Class<?> CLAZZ) throws SQLIdentifierException {
		if (CLAZZ.isAnnotationPresent(SQLInheritancePK.class)) {
			return CLAZZ.getAnnotation(SQLInheritancePK.class).primaryKeyName();
		}
		Field field = SQLClassHelper.getPrimaryKey(CLAZZ);
		if (field == null) {
			if (CLAZZ.getSuperclass() != null && !CLAZZ.getSuperclass().getName().equals(Object.class.getName())) {
				return SQLUtils.getPrimaryKeyName(CLAZZ.getSuperclass());
			} else {
				throw new SQLIdentifierException("A " + SQLIdentifier.class.getSimpleName() + " was not found!");
			}
		}
		return SQLUtils.getColumnNameFromField(field);

	}

}
