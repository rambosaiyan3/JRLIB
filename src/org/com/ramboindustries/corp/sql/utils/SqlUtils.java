package org.com.ramboindustries.corp.sql.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.com.ramboindustries.corp.sql.SqlColumnValue;
import org.com.ramboindustries.corp.sql.WhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SqlTable;
import org.com.ramboindustries.corp.sql.exceptions.SqlTableException;
import org.com.ramboindustries.corp.utils.JRFieldValue;
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
	public static <E> String createInsertScript(String tableName, E object) throws IllegalAccessException {
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
	 * @param object the instance
	 * @return SQL script
	 * @throws IllegalAccessException, SqlTableException
	 */
	public static <E> String createInsertScript(E object) throws IllegalAccessException, SqlTableException {
		if (!object.getClass().isAnnotationPresent(SqlTable.class))
			throw new SqlTableException(object.getClass());
		String tableName = object.getClass().getAnnotation(SqlTable.class).table();
		return createInsertScript(tableName, object);
	}

	/**
	 * @author kernelpanic_r
	 * @param tableName name of the entity
	 * @param object instance
	 * @param where conditions
	 * @return a SQL script
	 * @throws IllegalAccessException
	 */
	public static <E> String createUpdateScript(String tableName, E object, List<WhereCondition> where)
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
	
	public static <E> String createUpdateScript(E object, List<WhereCondition> where) throws IllegalAccessException, SqlTableException  {
		if(!object.getClass().isAnnotationPresent(SqlTable.class))
			throw new SqlTableException(object.getClass());
		String tableName = object.getClass().getAnnotation(SqlTable.class).table();
		return  createUpdateScript(tableName, object, where);
	}

	public static String createSelectScript(String tableName, String[] fieldsName, List<WhereCondition> where) {
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
	
	private static <E> SqlColumnValue getFieldsNames(E object) throws IllegalArgumentException, IllegalAccessException {
		SqlColumnValue sqlColumnValue = new SqlColumnValue();
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<JRFieldValue> jrFieldValue = ObjectAccessUtils.getFieldValue(object);
		jrFieldValue.forEach(x -> {
			fields.append(" " + x.getFieldName() + ", ");
			if (x.getFieldValue() instanceof String || x.getFieldValue() instanceof Date) {
				values.append(" '" + x.getFieldValue() + "', ");
			} else {
				values.append(" " + x.getFieldValue() + ", ");
			}
		});
		sqlColumnValue.setColumns(fields.delete(fields.length() - 1, fields.length()).toString());
		sqlColumnValue.setValues(values.delete(values.length() - 1, values.length()).toString());
		return sqlColumnValue;
	}
	
	public static <E> String teste(E object) throws IllegalArgumentException, IllegalAccessException {
		List<Class<?>> classes = ObjectAccessUtils.getSuperclassesFromClass(object.getClass(), false);
		String sql = null;
		if(classes != null && !classes.isEmpty()) {
			List<SqlColumnValue> sqlColumns = new ArrayList<>();
			for(Class<?> clazz : classes) {
				sqlColumns.add(getFieldsNames(clazz.cast(object)));
			}
		}else {
			
		}
		return sql;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		System.out.println(teste(new He()));
	}
	
	
}


class Te {
	private String e;
}

class He extends Te {
	private int a;
}