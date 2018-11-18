package org.com.ramboindustries.corp.sql.utils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.com.ramboindustries.corp.sql.SQLJavaField;
import org.com.ramboindustries.corp.sql.abstracts.SQLBasicWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLComplexWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLUniqueWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinitionCons;
import org.com.ramboindustries.corp.sql.commands.SQLDataManipulationCons;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;
import org.com.ramboindustries.corp.sql.types.TypeClass;

/**
 * Utility to work with SQL
 * @author kernelpanic_r
 */
public final class SQLUtils {

	private SQLUtils() {}

	private static String createWhereByType(final SQLBasicWhereCondition WHERE) {
		return WHERE.getConditionType().getType() + " ? ";
	}
	
	private static String createWhereByType(final SQLComplexWhereCondition WHERE) {
		return WHERE.getConditionType().getType() + " ? " + WHERE.getSQLOperator().getOperator() + " ? ";
	}
	
	private static String createWhereByType(final SQLUniqueWhereCondition WHERE) {
		return WHERE.getConditionType().getType();
	}
	
	private static String createCondition(final SQLWhereCondition WHERE_CONDITION) {
		String where = null;
		if(WHERE_CONDITION instanceof SQLBasicWhereCondition) {
			where = createWhereByType((SQLBasicWhereCondition)WHERE_CONDITION);
		} else if(WHERE_CONDITION instanceof SQLComplexWhereCondition) {
			where = createWhereByType((SQLComplexWhereCondition)WHERE_CONDITION);
		} else {
			where = createWhereByType((SQLUniqueWhereCondition)WHERE_CONDITION);
		}
		return WHERE_CONDITION.getFieldName() + " " + where;
	}

	protected static String createWhereCondition(final SQLWhereCondition WHERE_CONDITION) {
		return SQLDataManipulationCons.WHERE + createCondition(WHERE_CONDITION) + ";";
	}
	
	
	
	protected static String createWhereCondition(final List<SQLWhereCondition> WHERE_CONDITION) {
		StringBuilder builder = new StringBuilder(" " + SQLDataManipulationCons.WHERE_TRUE);
		WHERE_CONDITION.forEach(WHERE -> {
			builder.append( " AND ");
			builder.append(createCondition(WHERE));
		});
		return builder.toString();
	}

	/**
	 * Gets the name of the table on DB
	 * 
	 * @param clazz that represents the table
	 * @return table name
	 */
	protected static String getTableName(Class<?> clazz) {
		return clazz.isAnnotationPresent(org.com.ramboindustries.corp.sql.annotations.SQLTable.class)
				? clazz.getAnnotation(org.com.ramboindustries.corp.sql.annotations.SQLTable.class).table()
				: clazz.getSimpleName();
	}

	/**
	 * Creates a Primary Key constraint for the table
	 * 
	 * @param constraint the name of constraint
	 * @param field      the field that is the primary key
	 * @return a String that contains the line
	 */
	protected static String createPrimaryKeyConstraint(Field field, Class<?> clazz) {
		final String CONSTRAINT = createPKConstraintName(field, clazz);
		String name = null;
		if (clazz.isAnnotationPresent(SQLInheritancePK.class))
			name = clazz.getAnnotation(SQLInheritancePK.class).primaryKeyName();
		else
			name = field.getAnnotation(SQLIdentifier.class).identifierName();
		return SQLDataDefinitionCons.CONSTRAINT + " " + CONSTRAINT + " " + SQLDataDefinitionCons.PRIMARY_KEY + "(" + name + ")";
	}

	/**
	 * Creates a Foreign Key constraint for the table
	 * 
	 * @param constraint      the name of constraint
	 * @param field           the field that is the foreign key
	 * @param clazzReferenced class that has the primary key
	 * @return a String that contains the line
	 */
	protected static String createForeignKeyConstraint(Field field, final Class<?> CLAZZ) {
		final String CONSTRAINT = createFKConstraintName(field, CLAZZ); 
		String fieldReferenced = null;
		if (field.getType().isAnnotationPresent(SQLInheritancePK.class)) {
			// if the class has this annotation
			fieldReferenced = field.getType().getAnnotation(SQLInheritancePK.class).primaryKeyName();
		} else {
			fieldReferenced = SQLClassHelper.getPrimaryKey(field.getType()).getAnnotation(SQLIdentifier.class)
					.identifierName();
		}
		return SQLDataDefinitionCons.CONSTRAINT + CONSTRAINT + SQLDataDefinitionCons.FOREIGN_KEY + "("
				+ field.getAnnotation(SQLForeignKey.class).name() + ")" + SQLDataDefinitionCons.REFERENCES
				+ getTableName(field.getType()) + "(" + fieldReferenced + ")";
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
		TypeClass type = TypeClass.getTypeByName(clazz.getSimpleName());
		if (type == null) {
			Field field = SQLClassHelper.getPrimaryKey(clazz);
			type = TypeClass.getTypeByName(field.getType().getSimpleName());
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
		case DATE:
			return clazz.cast(resultSet.getDate(name));
		case LOCAL_DATE:
			// we have to make to cast as java.sql.Date, and then convert to java.time.LocalDate
			clazz = java.sql.Date.class;
			java.sql.Date date = (java.sql.Date) clazz.cast(resultSet.getDate(name));
			return date != null ? date.toLocalDate() : null;
		case LOCAL_TIME:
			return null;
		case LOCAL_DATE_TIME: 
			clazz = java.sql.Timestamp.class;
			java.sql.Timestamp dateTime = (java.sql.Timestamp) clazz.cast(resultSet.getTimestamp(name));
			return dateTime != null ? dateTime.toLocalDateTime() : null;
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
				return getPrimaryKeyName(CLAZZ.getSuperclass());
			} else {
				throw new SQLIdentifierException("A " + SQLIdentifier.class.getSimpleName() + " was not found!");
			}
		}
		return getColumnNameFromField(field);

	}
	
	private static String createFKConstraintName(final Field FIELD, final Class<?> CLAZZ) {
		// we will call it only if it is a foreign key
		final String CONSTRAINT = FIELD.getAnnotation(SQLForeignKey.class).constraintName().trim();
		if(!CONSTRAINT.isEmpty()) 
			// if the user manually set a constraint name
			return CONSTRAINT;
		else 
			// if the user does not set a constraint
			// so, we set it here
			return "FK_" + getTableName(CLAZZ) + "_" + getTableName(FIELD.getType());
	}
	
	private static String createPKConstraintName(final Field FIELD, final Class<?> CLAZZ) {
		String constraint = null;
		if(CLAZZ.isAnnotationPresent(SQLInheritancePK.class)) 
			// if the class is inheritance an ID, and has the SQLInheritance annotation
			constraint = CLAZZ.getAnnotation(SQLInheritancePK.class).constraintName().trim();
		else 
			// the class is inheritance an ID, but does not has the SQLInheritance annotation
			constraint = FIELD.getAnnotation(SQLIdentifier.class).constraintName().trim();
		if(!constraint.isEmpty()) 
			// the user set a constraint name
			return constraint;
		// the user does not set a constraint name
		else return "PK_" + getTableName(CLAZZ);
	}
	
	/**
	 * When sending data of a object to database
	 * @param values a list of values from object
	 * @param statement
	 * @throws SQLException
	 */
	public static void createPreparedStatementObject(List<Object> values, PreparedStatement statement) throws SQLException {
		for(int i = 0; i < values.size(); i++) {
			setPreparedStatementType(values.get(i), statement, i + 1);
		}
	}

	public static void createPreparedStatementWhereCondition(SQLWhereCondition where, PreparedStatement statement, int position) throws SQLException {
		if(where instanceof SQLBasicWhereCondition) {
			preparedStatementByWhereType((SQLBasicWhereCondition)where, statement, position);
		}else if( where instanceof SQLComplexWhereCondition ) {
			preparedStatementByWhereType((SQLComplexWhereCondition)where, statement, position);
		}
		// the SQLUniqueWhereCondition does not have a statement
	}	
	
	private static void preparedStatementByWhereType(SQLBasicWhereCondition where, PreparedStatement statement, int position) throws SQLException {
		setPreparedStatementType(where.getValue(), statement, position);
	}
	
	private static void preparedStatementByWhereType(SQLComplexWhereCondition where, PreparedStatement statement, int position) throws SQLException {
		setPreparedStatementType(where.getLeftValue(), statement, position++);
		setPreparedStatementType(where.getRightValue(), statement, position);
	}
	
	public static void createPreparedStatementWhereCondition(List<SQLWhereCondition> wheres, PreparedStatement statement, int position) throws SQLException {
		for(int i = 0; i < wheres.size(); i++) {
			createPreparedStatementWhereCondition(wheres.get(i), statement, position++);
			// TODO
			if(wheres.get(i) instanceof SQLComplexWhereCondition) position++;
		}
	}

	/**
	 * Set the type of the object that has to be send to database
	 * @param value
	 * @param statement
	 * @param position
	 * @throws SQLException
	 */
	private static void setPreparedStatementType(Object value, PreparedStatement statement, int position) throws SQLException {
		
			if(Objects.isNull(value)) {
				statement.setNull(position, 0);
				return;
			}
			TypeClass type = TypeClass.getTypeByName(value.getClass().getSimpleName());
			if (type == null) {
				Field field = SQLClassHelper.getPrimaryKey(value.getClass());
				type = TypeClass.getTypeByName(field.getType().getSimpleName());
			}
			switch(type) {
			case BYTE:
				statement.setByte(position, (Byte) value);
				break;
			case SHORT:
				statement.setShort(position, (Short) value );
				break;
			case INTEGER:
				statement.setInt(position, (Integer) value );
				break;
			case LONG:
				statement.setLong(position, (Long) value );
				break;
			case FLOAT:
				statement.setFloat(position, (Float) value );
				break;
			case DOUBLE:
				statement.setDouble(position, (Double) value );
				break;
			case BIG_DECIMAL:
				statement.setBigDecimal(position, (java.math.BigDecimal) value );
				break;
			case BOOLEAN:
				statement.setBoolean(position, (Boolean) value );
				break;
			case STRING:
				statement.setString(position, (String) value );
				break;
			case DATE:
				statement.setDate(position, (java.sql.Date) value);
				break;
			case LOCAL_DATE:
				java.sql.Date date = java.sql.Date.valueOf((java.time.LocalDate) value);
				statement.setDate(position, date);
				break;
			case LOCAL_TIME:
				statement.setDate(position, null);
				break;
			case LOCAL_DATE_TIME:
				java.sql.Date date1 = java.sql.Date.valueOf(((java.time.LocalDateTime) value).toLocalDate());
				statement.setDate(position, date1);
				break;
			case CHARACTER:
				String val = value !=  null ? value .toString() : null;
				statement.setString(position,  val);
				break;
			}
	}
	
	/**
	 * We do not need the identifier when update or insert data, so we remove it from list
	 * @param sqljavaField
	 * @param name
	 */
	protected static void removePrimaryKeyFromList(Set<SQLJavaField> sqljavaField, String name) {
			sqljavaField.removeIf(item -> item.getSqlColumn().equals(name));
	}

	

}
