package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.com.ramboindustries.corp.sql.SQLClassHelper;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.commands.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.commands.SQLDataManipulation;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * Class that contain the SQLs scripts
 * @author kernelpanic_r
 *
 */
public class SQLScripts {

	private final SQLUtils SQL_UTILS;

	public SQLScripts() {
		this.SQL_UTILS = new SQLUtils();
	}
	
	public SQLUtils getSQLUtils() {
		return SQL_UTILS;
	}
	
	/**
	 * Creates an INSERT script for SQL
	 * @param object that contains the values
	 * @return a string that represent the script
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public <E> String createInsertScriptSQL(E object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Map<String, String> map = SQL_UTILS.createInsert(ObjectAccessUtils.getAllFieldFromClassAndSuperClass(object, false));
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
		return SQLDataManipulation.INSERT + SQL_UTILS.getTableName(object.getClass()) + columns.toString() + SQLDataManipulation.VALUES + values.toString() + ";";
		}
	
	
	/**
	 * Creates a dynamic SQL Table as a class argument
	 * 
	 * @param clazz
	 * @return
	 * @throws SQLIdentifierException
	 */
	public String createTableScript(Class<?> clazz) throws SQLIdentifierException {
		List<Field> fields = SQL_UTILS.allFieldsToTable(clazz);
		StringBuilder sql = new StringBuilder();

		sql.append(SQLDataDefinition.CREATE_TABLE + SQL_UTILS.getTableName(clazz) + " (\n");

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
					foreignConstraints.add(SQL_UTILS.createForeignKeyConstraint(
							("FK_" + SQL_UTILS.getTableName(clazz) + "_" + SQL_UTILS.getTableName(field.getType())), field,
							field.getType()));
				}
				// creates a sql line
				sql.append(SQLClassHelper.attributeToSQLColumn(field));
				sql.append(",\n");
			}
		}

		if (primaryKey != null) {
			sql.append(SQL_UTILS.createPrimaryKeyConstraint("PK_" + SQL_UTILS.getTableName(clazz), primaryKey, clazz));
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
	 * Creates a SELECT * FROM TABLE
	 * @param CLAZZ that represents the TABLE
	 * @return the SCRIPT
	 */
	public <E> String createSQLSelectScript(final Class<E> CLAZZ) {
		return SQLDataManipulation.SELECT_FROM + SQL_UTILS.getTableName(CLAZZ) + ";";
	}

	/**
	 * Creates a SELECT A,B,C FROM TABLE
	 * @param CLAZZ 
	 * @param COLUMNS
	 * @return
	 */
	public <E> String createSQLSelectScript(final Class<E> CLAZZ, final Field[] COLUMNS) {
		final String FIELDS = SQL_UTILS.createFieldsToSelect(COLUMNS);
		return SQLDataManipulation.SELECT + FIELDS + SQLDataManipulation.FROM + SQL_UTILS.getTableName(CLAZZ) + ";";
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final SQLWhereCondition WHERE) {
		return SQLDataManipulation.SELECT_FROM + SQL_UTILS.getTableName(CLAZZ) + SQL_UTILS.createWhereCondition(WHERE);
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final Field[] COLUMNS, final SQLWhereCondition WHERE) {
		final String FIELDS = SQL_UTILS.createFieldsToSelect(COLUMNS);
		return SQLDataManipulation.SELECT + FIELDS + SQLDataManipulation.FROM + SQL_UTILS.getTableName(CLAZZ) + SQL_UTILS.createWhereCondition(WHERE) + ";";
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE) {
		final String CONDITIONS = SQL_UTILS.createWhereCondition(WHERE);
		return SQLDataManipulation.SELECT_FROM + SQL_UTILS.getTableName(CLAZZ) + CONDITIONS + ";";
	}

	public <E>String createSQLSelectScript(final Class<E> CLAZZ, final Field[] COLUMNS,
			final List<SQLWhereCondition> WHERE) {
		final String FIELDS = SQL_UTILS.createFieldsToSelect(COLUMNS);
		final String WHERE_CONDITIONS = SQL_UTILS.createWhereCondition(WHERE);
		return SQLDataManipulation.SELECT + FIELDS + SQL_UTILS.getTableName(CLAZZ) + WHERE_CONDITIONS + ";";
	}
	
	public <E> String createSQLMaxSelectScript(final Class<E> CLAZZ) throws SQLIdentifierException {
		final String PK_NAME = SQL_UTILS.getPrimaryKeyName(CLAZZ);
		return SQLDataManipulation.SELECT_MAX +  "(" + PK_NAME + ")" +  SQLDataManipulation.FROM +  SQL_UTILS.getTableName(CLAZZ) + ";"; 
	}
	
}
