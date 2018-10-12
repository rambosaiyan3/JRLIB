package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.com.ramboindustries.corp.sql.SQLClassHelper;
import org.com.ramboindustries.corp.sql.SQLConstants;
import org.com.ramboindustries.corp.sql.SQLInsert;
import org.com.ramboindustries.corp.sql.SQLJavaField;
import org.com.ramboindustries.corp.sql.SQLUpdate;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.commands.SQLDataManipulation;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * @author kernelpanic_r
 */
public final class SQLUtils {

	private Object setValue(Object value) {
		if (value instanceof String || value instanceof java.util.Date)
			return "'" + value + "'";
		else
			return value;
	}

	private SQLInsert makeInsertScript(Set<SQLJavaField> sqlJavaField) {
		SQLInsert sqlColumnValue = new SQLInsert();
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		sqlJavaField.forEach(sql -> {
			fields.append("" + sql.getSqlColumn() + ",");
			values.append("" + setValue(sql.getValue()) + ",");
		});
		sqlColumnValue.setColumns(fields.toString());
		sqlColumnValue.setValues(values.toString());
		return sqlColumnValue;
	}

	private SQLUpdate makeUpdateScript(Set<SQLJavaField> sqlJavaFields, List<SQLWhereCondition> whereConditions) {
		SQLUpdate sqlUpdate = new SQLUpdate();
		StringBuilder update = new StringBuilder();
		StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
		sqlJavaFields.forEach(x -> {
			update.append("" + x.getSqlColumn() + SQLConstants.EQUAL + setValue(x.getValue()) + ",");
		});
		whereConditions.forEach(x -> {
			where.append(SQLConstants.AND + x.getFieldName() + x.getConditionType().getType() + x.getFieldValue());
		});
		sqlUpdate.setUpdateScript(update.delete(update.length() - 1, update.length()).toString());
		sqlUpdate.setWhereCondition(where.toString());
		return sqlUpdate;
	}

	private SQLUpdate makeUpdateScript(Set<SQLJavaField> sqlJavaFields, SQLWhereCondition whereCondition) {
		SQLUpdate sqlUpdate = new SQLUpdate();
		StringBuilder update = new StringBuilder();
		StringBuilder where = new StringBuilder(SQLConstants.WHERE);
		sqlJavaFields.forEach(x -> {
			update.append("" + x.getSqlColumn() + SQLConstants.EQUAL + x.getValue() + ",");
		});
		sqlUpdate.setUpdateScript(update.delete(update.length() - 1, update.length()).toString());
		where.append(whereCondition.getFieldName() + " " + whereCondition.getConditionType().getType() + " "
				+ whereCondition.getFieldValue());
		sqlUpdate.setWhereCondition(where.toString());
		return sqlUpdate;
	}

	private <E> String getTableName(E object) {
		if (object.getClass().isAnnotationPresent(SQLTable.class))
			return object.getClass().getAnnotation(SQLTable.class).table();
		else
			return object.getClass().getSimpleName();
	}

	public String getTableName(Class<?> clazz) {
		return clazz.isAnnotationPresent(SQLTable.class) ? clazz.getAnnotation(SQLTable.class).table()
				: clazz.getSimpleName();
	}

	public <E> String createInsertScriptSQL(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Set<SQLJavaField> sqlJavaField = ObjectAccessUtils.<E>getAllFieldFromClassAndSuperClass(object, false);
		SQLInsert sqlColumnValues = makeInsertScript(sqlJavaField);
		return SQLConstants.insertSQL(getTableName(object), sqlColumnValues.getColumns(), sqlColumnValues.getValues());
	}

	public <E> String createUpdateScriptSQL(E object, List<SQLWhereCondition> whereConditions)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		String sql = null;
		Set<SQLJavaField> sqlJavaFields = ObjectAccessUtils.<E>getAllFieldFromClassAndSuperClass(object, false);
		SQLUpdate sqlUpdate = makeUpdateScript(sqlJavaFields, whereConditions);
		sql = SQLConstants.UPDATE + getTableName(object) + SQLConstants.SET + sqlUpdate.getUpdateScript()
				+ sqlUpdate.getWhereCondition();
		return sql;
	}

	public <E> String createUpdateScriptSQL(E object, SQLWhereCondition whereCondition)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Set<SQLJavaField> sqlJavaFields = ObjectAccessUtils.getAllFieldFromClassAndSuperClass(object, false);
		SQLUpdate sqlUpdate = makeUpdateScript(sqlJavaFields, whereCondition);
		return SQLConstants.updateSQL(getTableName(object), sqlUpdate.getUpdateScript(), sqlUpdate.getWhereCondition());
	}

	public static void setParametersPreparedStatement(PreparedStatement preparedStatement,
			Map<Integer, Object> parameters) {
		parameters.forEach((key, value) -> {
			try {
				if (value instanceof java.util.Date || value instanceof String) {
					preparedStatement.setString(key, (String) value);
				} else if (value instanceof Number) {
					if (value instanceof Integer) {
						preparedStatement.setInt(key, (Integer) value);
					} else if (value instanceof BigDecimal) {
						preparedStatement.setBigDecimal(key, (BigDecimal) value);
					} else if (value instanceof Long) {
						preparedStatement.setLong(key, (Long) value);
					} else if (value instanceof Short) {
						preparedStatement.setShort(key, (Short) value);
					} else if (value instanceof Float) {
						preparedStatement.setFloat(key, (Float) value);
					} else if (value instanceof Double) {
						preparedStatement.setDouble(key, (Double) value);
					} else {
						preparedStatement.setByte(key, (Byte) value);
					}
				} else if (value instanceof Boolean) {
					preparedStatement.setBoolean(key, (Boolean) value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Creates a Primary Key constraint for the table
	 * 
	 * @param constraint the name of constraint
	 * @param field      the field that is the primary key
	 * @return a String that contains the line
	 */
	private String createPrimaryKeyConstraint(String constraint, Field field, Class<?> clazz) {
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
	private String createForeignKeyConstraint(String constraint, Field field, Class<?> clazzReferenced) {
		String fieldReferenced = SQLClassHelper.getPrimaryKey(clazzReferenced).getAnnotation(SQLIdentifier.class)
				.identifierName();
		return SQLDataDefinition.CONSTRAINT + constraint + SQLDataDefinition.FOREIGN_KEY + "("
				+ field.getAnnotation(SQLForeignKey.class).name() + ")" + SQLDataDefinition.REFERENCES
				+ getTableName(clazzReferenced) + "(" + fieldReferenced + ")";
	}

	/**
	 * Creates a dinamic SQL Table as a class argument
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLIdentifierException
	 */
	public String createTableScript(Class<?> clazz) throws SQLIdentifierException {
		List<Field> fields = this.allFieldsToTable(clazz);
		StringBuilder sql = new StringBuilder();

		sql.append(SQLDataDefinition.CREATE_TABLE + getTableName(clazz) + " (\n");

		// the first element will always be the 1
		Field primaryKey = fields.get(0);

		// we remove him to avoid problems
		fields.remove(0);

		// if the clazz has the SQLInheritancePK it will get the name
		sql.append(SQLClassHelper.attributeToSQLColumn(primaryKey, clazz));
		sql.append(",\n");

		// list that will have the foreign key constraints
		List<String> foreignConstraints = new ArrayList<>();

		for (Field field : fields) {
			if (!field.isAnnotationPresent(SQLIgnore.class)) {

				// that has an object that represents another table
				if (field.isAnnotationPresent(SQLForeignKey.class)) {
					// we create and add a constraint line to it
					foreignConstraints.add(createForeignKeyConstraint(
							("FK_" + getTableName(clazz) + "_" + getTableName(field.getType())), field,
							field.getType()));
				}
				// creates a sql line
				sql.append(SQLClassHelper.attributeToSQLColumn(field));
				sql.append(",\n");
			}
		}

		if (primaryKey != null) {
			sql.append(createPrimaryKeyConstraint("PK_" + getTableName(clazz), primaryKey, clazz));
			sql.append(",\n");
		}

		foreignConstraints.forEach(constraint -> {
			sql.append(constraint);
			sql.append(",\n");
		});

		int last = sql.toString().lastIndexOf(",");
		return sql.delete(last, sql.length()) + ");";
	}

	public List<Field> allFieldsToTable(Class<?> clazz) throws SQLIdentifierException {
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
		this.setPrimaryKeyPosition(fields, classes);

		return fields;
	}

	private void setPrimaryKeyPosition(List<Field> fields, List<?> classes) throws SQLIdentifierException {
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

	public <E> String createSQLSelectScript(final Class<E> clazz) {
		return SQLDataManipulation.SELECT_FROM + this.getTableName(clazz) + ";";
	}

	public String createSQLSelectScript(final Class<?> clazz, final SQLWhereCondition where) {
		return SQLDataManipulation.SELECT_FROM + this.getTableName(clazz) + SQLDataManipulation.WHERE
				+ where.getFieldName() + where.getConditionType().getType() + where.getFieldValue() + ";";
	}

	public String createSQLSelectScript(final Class<?> clazz, final List<SQLWhereCondition> where) {
		StringBuilder conditions = new StringBuilder();
		where.forEach(x -> {
			conditions.append(SQLDataManipulation.AND);
			conditions.append(x.getFieldName() + x.getConditionType().getType() + x.getFieldValue());
		});
		return SQLDataManipulation.SELECT_FROM + this.getTableName(clazz) + SQLDataManipulation.WHERE_TRUE
				+ conditions.toString() + ";";
	}
	
	public String getColumnNameFromField(Field field) {
		if (field.isAnnotationPresent(SQLIdentifier.class))
			return field.getAnnotation(SQLIdentifier.class).identifierName();
		else if (field.isAnnotationPresent(SQLColumn.class))
			return field.getAnnotation(SQLColumn.class).name();
		else if (field.isAnnotationPresent(SQLForeignKey.class))
			return field.getAnnotation(SQLForeignKey.class).name();
		else
			return field.getName();
	}

	public boolean isFieldRelationship(Field field) {
		return field.isAnnotationPresent(SQLForeignKey.class);
	}
	
	

	
	
}
