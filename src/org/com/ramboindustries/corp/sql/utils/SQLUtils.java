package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.com.ramboindustries.corp.sql.SQLConstants;
import org.com.ramboindustries.corp.sql.SQLInsert;
import org.com.ramboindustries.corp.sql.SQLJavaField;
import org.com.ramboindustries.corp.sql.SQLUpdate;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * @author kernelpanic_r
 */
public class SQLUtils {

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
		where.append( whereCondition.getFieldName() + " " + whereCondition.getConditionType().getType() + " " + whereCondition.getFieldValue());
		sqlUpdate.setWhereCondition(where.toString());
		return sqlUpdate;
	}

	private <E> String getTableName(E object) {
		if (object.getClass().isAnnotationPresent(SQLTable.class))
			return object.getClass().getAnnotation(SQLTable.class).table();
		else
			return object.getClass().getSimpleName();
	}

	public <E> String createInsertScriptSQL(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Set<SQLJavaField> sqlJavaField = ObjectAccessUtils.getAllFieldFromClassAndSuperClass(object, false);
		SQLInsert sqlColumnValues = makeInsertScript(sqlJavaField);
		return SQLConstants.insertSQL(getTableName(object), sqlColumnValues.getColumns(), sqlColumnValues.getValues());
	}

	public <E> String createUpdateScriptSQL(E object, List<SQLWhereCondition> whereConditions)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		String sql = null;
		Set<SQLJavaField> sqlJavaFields = ObjectAccessUtils.getAllFieldFromClassAndSuperClass(object, false);
		SQLUpdate sqlUpdate = makeUpdateScript(sqlJavaFields, whereConditions);
		sql = SQLConstants.UPDATE + getTableName(object) + SQLConstants.SET + sqlUpdate.getUpdateScript()
				+ sqlUpdate.getWhereCondition();
		return sql;
	}
	
	public <E> String createUpdateScriptSQL(E object, SQLWhereCondition whereCondition) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Set<SQLJavaField> sqlJavaFields = ObjectAccessUtils.getAllFieldFromClassAndSuperClass(object, false);
		SQLUpdate sqlUpdate = makeUpdateScript(sqlJavaFields, whereCondition);
		return SQLConstants.updateSQL(getTableName(object), sqlUpdate.getUpdateScript(), sqlUpdate.getWhereCondition());
	}

}
