package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.com.ramboindustries.corp.sql.SqlColumnValue;
import org.com.ramboindustries.corp.sql.SqlJavaField;
import org.com.ramboindustries.corp.sql.WhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SqlTable;
import org.com.ramboindustries.corp.sql.exceptions.SqlTableException;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

public class SqlUtils {

	/**
	 * Creates a dynamic SQL script, you just need to inform the table name, and
	 * give me the instance note, remember to choose to use @Sql annotations
	 * 
	 * @author kernelpanic_r
	 * @param tableName name of the table
	 * @param object    instance of the object
	 * @return
	 * @throws IllegalAccessException
	 */
	public <E> String createInsertScript(String tableName, E object) throws IllegalAccessException {
		StringBuilder sqlFields = new StringBuilder();
		StringBuilder sqlValues = new StringBuilder();
		String sql = null;
		Map<String, Object> keyValue = ObjectAccessUtils.getFieldsValuesFromSQLEntity(object);
		keyValue.forEach((key, value) -> {
			sqlFields.append(key + ",");
			if (value instanceof String || value instanceof Date) {
				sqlValues.append("'" + value + "',");
			} else {
				sqlValues.append(value + ",");
			}
		});
		sqlFields.delete(sqlFields.length() - 1, sqlFields.length());
		sqlValues.delete(sqlValues.length() - 1, sqlValues.length());
		sql = " INSERT INTO " + tableName + "(" + sqlFields.toString() + ") VALUES (" + sqlValues.toString() + ") ";
		return sql;
	}

	/**
	 * If you used the SqlTable annotation, we will get the name that you choose and
	 * create a dynamic script
	 * 
	 * @param object the instance
	 * @return SQL script
	 * @throws IllegalAccessException, SqlTableException
	 */
	public <E> String createInsertScript(E object) throws IllegalAccessException, SqlTableException {
		if (!object.getClass().isAnnotationPresent(SqlTable.class))
			throw new SqlTableException(object.getClass());
		String tableName = object.getClass().getAnnotation(SqlTable.class).table();
		return createInsertScript(tableName, object);
	}

	/**
	 * @author kernelpanic_r
	 * @param tableName name of the entity
	 * @param object    instance
	 * @param where     conditions
	 * @return a SQL script
	 * @throws IllegalAccessException
	 */
	public <E> String createUpdateScript(String tableName, E object, List<WhereCondition> where)
			throws IllegalAccessException {
		StringBuilder script = new StringBuilder(" SET");
		String sql = null;
		Map<String, Object> keyValue = ObjectAccessUtils.getFieldsValuesFromSQLEntity(object);
		keyValue.forEach((key, value) -> {
			if (value instanceof String || value instanceof Date) {
				script.append(" " + key + " = '" + value + "',");
			} else {
				script.append(" " + key + " = " + value + ",");
			}
		});
		script.delete(script.length() - 1, script.length());
		sql = " UPDATE " + tableName + script.toString();
		return sql;
	}

	public <E> String createUpdateScript(E object, List<WhereCondition> where)
			throws IllegalAccessException, SqlTableException {
		if (!object.getClass().isAnnotationPresent(SqlTable.class))
			throw new SqlTableException(object.getClass());
		String tableName = object.getClass().getAnnotation(SqlTable.class).table();
		return createUpdateScript(tableName, object, where);
	}

	public String createSelectScript(String tableName, String[] fieldsName, List<WhereCondition> where) {
		char alias = tableName.toUpperCase().charAt(0);
		StringBuilder sql = new StringBuilder(" SELECT ");
		for (String field : fieldsName) {
			sql.append(" " + alias + "." + field.toUpperCase() + ",");
		}
		sql.delete(sql.length() - 1, sql.length());
		sql.append(" FROM " + tableName + " AS " + alias);
		sql.append(" WHERE 1 = 1 ");
		where.forEach(condition -> {
			sql.append(" AND " + alias + ".");
			sql.append(condition.getFieldName().toUpperCase());
			sql.append(" " + condition.getConditionType().getType() + " ");
			sql.append(condition.getFieldValue());
		});
		return sql.toString();
	}

	private SqlColumnValue insertScript(Set<SqlJavaField> sqlJavaField) {
		SqlColumnValue sqlColumnValue = new SqlColumnValue();
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		sqlJavaField.forEach(sql -> {
			fields.append(" " + sql.getSqlColumn() + ",");
			if (sql.getValue() instanceof String || sql.getValue() instanceof java.util.Date)
				values.append(" '" + sql.getValue() + "',");
			else
				values.append(" " + sql.getValue() + ",");
		});
		sqlColumnValue.setColumns(fields.toString());
		sqlColumnValue.setValues(values.toString());
		return sqlColumnValue;
	}

	private <E> String getTableName(E object) {
		if (object.getClass().isAnnotationPresent(SqlTable.class))
			return object.getClass().getAnnotation(SqlTable.class).table();
		else
			return object.getClass().getSimpleName();
	}

	public <E> String createInsertScriptSQL(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Set<SqlJavaField> sqlJavaField = ObjectAccessUtils.getAllFieldFromClassAndSuperClass(object, false);
		SqlColumnValue sqlColumnValues = insertScript(sqlJavaField);
		String sql = " INSERT INTO " + getTableName(object) + " (" + sqlColumnValues.getColumns() + " ) VALUES ("
				+ sqlColumnValues.getValues() + " )";
		return sql;
	}
}
