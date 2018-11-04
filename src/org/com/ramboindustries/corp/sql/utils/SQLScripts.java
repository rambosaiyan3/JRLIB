package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.commands.SQLDataManipulation;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;
import org.com.ramboindustries.corp.sql.system.SQLSystem;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * Class that contain the SQLs scripts
 * @author kernelpanic_r
 *
 */
public class SQLScripts {

	/**
	 * Creates an INSERT script for SQL
	 * @param object that contains the values
	 * @return a string that represent the script
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public <E> String createSQLInsertScript(final E OBJECT) throws Exception {
		Map<String, String> map = SQLUtils.mapAttributes(ObjectAccessUtils.getAllFieldFromClassAndSuperClass(OBJECT, false));
		
		// we have to remove the primary key, to avoid the MySQLIntegrityConstraintViolationException when insert
		// we do not need the primary key of the table when inserting or updating
		map.remove(SQLUtils.getPrimaryKeyName(OBJECT.getClass()));
		
		StringBuilder columns = new StringBuilder(" ( ");
		StringBuilder values = new StringBuilder(" ( ");
		map.forEach((column, value) -> {
			columns.append(column + ", ");
			values.append(value + ", ");
		});
		columns.delete(columns.lastIndexOf(","), columns.length());
		columns.append(")");
		values.delete(values.lastIndexOf(","), values.length());
		values.append(")");
		return SQLDataManipulation.INSERT + SQLUtils.getTableName(OBJECT.getClass()) + columns.toString() + SQLDataManipulation.VALUES + values.toString() + ";";
	}
	
	/**
	 * Creates a dinamic update script with a where condition
	 * @param OBJECT
	 * @param WHERE
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 * @throws SQLIdentifierException 
	 */
	public <E> String createSQLUpdateScript(final E OBJECT, final SQLWhereCondition WHERE) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException, SQLIdentifierException {
		Map<String, String> map = SQLUtils.mapAttributes(ObjectAccessUtils.getAllFieldFromClassAndSuperClass(OBJECT, false));
		StringBuilder sql = new StringBuilder(SQLDataManipulation.UPDATE + SQLUtils.getTableName(OBJECT.getClass()) + SQLDataManipulation.SET);
		
		// we have to remove the primary key, to avoid the MySQLIntegrityConstraintViolationException when UPDATE
		// we do not need the primary key of the table when inserting or updating
		map.remove(SQLUtils.getPrimaryKeyName(OBJECT.getClass()));
		
		map.forEach((column, value) -> {
			sql.append(column + " = " + value + ", ");
		});
		sql.delete(sql.lastIndexOf(","), sql.length());
		sql.append(SQLUtils.createWhereCondition(WHERE));
		return sql.toString();
	}
	
	public <E> String createSQLUpdateScript(final E OBJECT, final List<SQLWhereCondition> WHERE) throws Exception {
		Map<String, String> map = SQLUtils.mapAttributes(ObjectAccessUtils.getAllFieldFromClassAndSuperClass(OBJECT, false));
		StringBuilder sql = new StringBuilder(SQLDataManipulation.UPDATE + SQLUtils.getTableName(OBJECT.getClass()) + SQLDataManipulation.SET);
		
		// we have to remove the primary key, to avoid the MySQLIntegrityConstraintViolationException when UPDATE
		// we do not need the primary key of the table when inserting or updating
		map.remove(SQLUtils.getPrimaryKeyName(OBJECT.getClass()));
				
		map.forEach((column, value) -> {
			sql.append(column + " = " + value + ", ");
		});
		sql.delete(sql.lastIndexOf(","), sql.length());
		sql.append(SQLUtils.createWhereCondition(WHERE));
		return sql.toString();
	}
	
	/**
	 * Creates a dynamic SQL Table as a class argument
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLIdentifierException
	 */
	public String createSQLTableScript(Class<?> clazz, final SQLSystem SYSTEM) throws SQLIdentifierException {
		List<Field> fields = SQLUtils.allFieldsToTable(clazz);
		StringBuilder sql = new StringBuilder();

		sql.append(SQLDataDefinition.CREATE_TABLE + SQLUtils.getTableName(clazz) + " (\n");

		// the first element will always be the 1
		Field primaryKey = fields.get(0);

		// we remove him to avoid problems
		fields.remove(0);

		// if the clazz has the SQLInheritancePK it will get the name
		sql.append(SQLClassHelper.attributeToSQLColumn(primaryKey, clazz, SYSTEM));
		sql.append(",\n");

		// list that will have the foreign key constraints
		List<String> foreignConstraints = new ArrayList<>();

		for (Field field : fields) {
			if (!field.isAnnotationPresent(SQLIgnore.class)) {

				// that has an object that represents another table
				if (field.isAnnotationPresent(SQLForeignKey.class)) {
					// we create and add a constraint line to it
					foreignConstraints.add(SQLUtils.createForeignKeyConstraint(
							("FK_" + SQLUtils.getTableName(clazz) + "_" + SQLUtils.getTableName(field.getType())), field,
							field.getType()));
				}
				// creates a sql line
				sql.append(SQLClassHelper.attributeToSQLColumn(field, SYSTEM));
				sql.append(",\n");
			}
		}

		if (primaryKey != null) {
			sql.append(SQLUtils.createPrimaryKeyConstraint("PK_" + SQLUtils.getTableName(clazz), primaryKey, clazz));
			sql.append(",\n");
		}

		foreignConstraints.forEach(constraint -> {
			sql.append(constraint);
			sql.append(",\n");
		});

		int last = sql.toString().lastIndexOf(",");
		return sql.delete(last, sql.length()) + ");";
	}
	
	/**
	 * Generates an dynamic script to drop the table
	 * @param CLAZZ
	 * @return
	 */
	public <E> String createSQLDropTableScript(final Class<E> CLAZZ) {
		return SQLDataDefinition.DROP_TABLE_IF_EXISTS + SQLUtils.getTableName(CLAZZ) + " ;";
	}
	
	
	/**
	 * Creates a SELECT * FROM TABLE
	 * @param CLAZZ that represents the TABLE
	 * @return the SCRIPT
	 */
	public <E> String createSQLSelectScript(final Class<E> CLAZZ) {
		return SQLDataManipulation.SELECT_FROM + SQLUtils.getTableName(CLAZZ) + ";";
	}

	/**
	 * Creates a SELECT A,B,C FROM TABLE
	 * @param CLAZZ 
	 * @param COLUMNS
	 * @return
	 */
	public <E> String createSQLSelectScript(final Class<E> CLAZZ, final Field[] COLUMNS) {
		final String FIELDS = SQLUtils.createFieldsToSelect(COLUMNS);
		return SQLDataManipulation.SELECT + FIELDS + SQLDataManipulation.FROM + SQLUtils.getTableName(CLAZZ) + ";";
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final SQLWhereCondition WHERE) {
		return SQLDataManipulation.SELECT_FROM + SQLUtils.getTableName(CLAZZ) + SQLUtils.createWhereCondition(WHERE);
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final Field[] COLUMNS, final SQLWhereCondition WHERE) {
		final String FIELDS = SQLUtils.createFieldsToSelect(COLUMNS);
		return SQLDataManipulation.SELECT + FIELDS + SQLDataManipulation.FROM + SQLUtils.getTableName(CLAZZ) + SQLUtils.createWhereCondition(WHERE) + ";";
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE) {
		final String CONDITIONS = SQLUtils.createWhereCondition(WHERE);
		return SQLDataManipulation.SELECT_FROM + SQLUtils.getTableName(CLAZZ) + CONDITIONS + ";";
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final Field[] COLUMNS,
			final List<SQLWhereCondition> WHERE) {
		final String FIELDS = SQLUtils.createFieldsToSelect(COLUMNS);
		final String WHERE_CONDITIONS = SQLUtils.createWhereCondition(WHERE);
		return SQLDataManipulation.SELECT + FIELDS + SQLUtils.getTableName(CLAZZ) + WHERE_CONDITIONS + ";";
	}
	
	public <E> String createSQLMaxSelectScript(final Class<E> CLAZZ) throws SQLIdentifierException {
		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);
		return SQLDataManipulation.SELECT_MAX +  "(" + PK_NAME + ")" +  SQLDataManipulation.FROM +  SQLUtils.getTableName(CLAZZ) + ";"; 
	}
	
	public <E> String createSQLDeleteScript(final Class<E> CLAZZ, final SQLWhereCondition WHERE) {
		final String WHERE_CONDITION = SQLUtils.createWhereCondition(WHERE);
		return SQLDataManipulation.DELETE_FROM + SQLUtils.getTableName(CLAZZ) + " " + WHERE_CONDITION + " ;";
	}
	
	public <E> String createSQLDeleteScript(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE) {
		final String WHERE_CONDITIONS = SQLUtils.createWhereCondition(WHERE);
		return SQLDataManipulation.DELETE_FROM + SQLUtils.getTableName(CLAZZ) + " " + WHERE_CONDITIONS + " ;";
	}
	
}
