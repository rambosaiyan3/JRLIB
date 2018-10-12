package org.com.ramboindustries.corp.sql;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.com.ramboindustries.corp.sql.abstracts.SQLConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConstants;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;
import org.com.ramboindustries.corp.test.Matheus;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

public class JDBCConnection extends SQLConnection {

	private SQLUtils sqlUtils;

	public JDBCConnection(String URL, String USER, String PASS) {
		super(URL, USER, PASS);
		sqlUtils = new SQLUtils();
	}

	/**
	 * Simple SELECT * FROM TABLE
	 * @param clazz that represents the table
	 * @return an array list with all the objects and it's relationships
	 * @throws Exception
	 */
	public <E> List<E> selectFrom(Class<E> clazz, boolean showSql) throws Exception {
		String script = sqlUtils.<E>createSQLSelectScript(clazz);
		
		if(showSql) {
			System.out.println(" INIT SQL >>  " + script + " << END SQL ");
		}
		List<Field> fields = sqlUtils.allFieldsToTable(clazz);
		
		// init the list of objects
		List<E> objects = new ArrayList<>();

		// will capture the lines of the table
		ResultSet resultSet = super.executeSQLSelect(script);

		while (resultSet.next()) {
			objects.add(this.createObjectFromLine(resultSet, fields, clazz, showSql));
		}

		return objects;
	}
	
	/**
	 * Select FROM Table with a WHERE clause
	 * @param clazz that represents the table
	 * @param sqlWhereCondition the condidion
	 * @return an array list
	 * @throws Exception
	 */
	public <E> List<E> selectFrom(Class<E> clazz, SQLWhereCondition sqlWhereCondition, boolean showSql) throws Exception{
		String script = sqlUtils.createSQLSelectScript(clazz, sqlWhereCondition);

		if (showSql) {
			System.out.println(" INIT SQL >>  " + script + " << END SQL ");
		}
		
		List<Field> fields = sqlUtils.allFieldsToTable(clazz);
		
		List<E> objects = new ArrayList<>();
		ResultSet resultSet = super.executeSQLSelect(script);

		while (resultSet.next()) {
			objects.add(this.createObjectFromLine(resultSet, fields, clazz, showSql));
		}

		return objects;
	}
	
	
	public <E> List<E> selectFrom(Class<E> clazz, List<SQLWhereCondition> sqlWhereConditions, boolean showSql) throws Exception {
	
		String script = sqlUtils.createSQLSelectScript(clazz, sqlWhereConditions);
		if (showSql) {
			System.out.println(" INIT SQL >>  " + script + " << END SQL ");
		}
		List<Field> fields = sqlUtils.allFieldsToTable(clazz);

		List<E> objects = new ArrayList<>();
		ResultSet resultSet = super.executeSQLSelect(script);

		while (resultSet.next()) {
			objects.add(this.createObjectFromLine(resultSet, fields, clazz, showSql));
		}

		return objects;

	}
	
	
	private <E> E createObjectFromLine(ResultSet resultSet, List<Field> fields, Class<E> clazz, boolean showSql)throws Exception{

		// create and initialize the object
		E object = ObjectAccessUtils.<E>initObject(clazz);
		
		
		// set the primary key value, for the first item of the element
		// primary key, will always be the first element
		this.setPrimaryKeyValue(object, clazz, resultSet, fields.get(0));
				
		// iterate over fields, the primary key was already set, so we can start at 1
		for (byte i = 1; i < fields.size(); i ++) {

			// get the name of the field
			final String FIELD_NAME = fields.get(i).getName();
		
			if (sqlUtils.isFieldRelationship(fields.get(i))) {
				// if the field is a foreign key, so we have to create an object
				
				final String COLUMN_RELATIONSHIP = sqlUtils.getColumnNameFromField(SQLClassHelper.getPrimaryKey(fields.get(i).getType()));
				final Object COLUMN_VALUE = super.getSQLValue(COLUMN_RELATIONSHIP, resultSet, fields.get(i).getType());

				// creates a where condition
				SQLWhereCondition sqlWhereCondition = new SQLWhereCondition(COLUMN_RELATIONSHIP, COLUMN_VALUE,SQLConditionType.EQUAL);
				Object value =	this.getSQLColumnValue(fields.get(i).getType(), sqlWhereCondition, showSql);
			
				ObjectAccessUtils.<E>callSetter(object, FIELD_NAME, value);
				
			} else {
				// this is a normal field, that we do not need to create an object to them
				
				final String COLUMN_NAME = sqlUtils.getColumnNameFromField(fields.get(i));
				
				final Object COLUMN_VALUE = super.getSQLValue(COLUMN_NAME, resultSet, fields.get(i).getType());

				// calls the object setter
				ObjectAccessUtils.<E>callSetter(object, FIELD_NAME, COLUMN_VALUE);

			}
		}
		return object;
	}
	

	private Object getSQLColumnValue(Class<?> clazz, SQLWhereCondition sqlWhereCondition, boolean showSql) throws Exception {

		// creates the select script
		String script = sqlUtils.createSQLSelectScript(clazz, sqlWhereCondition);

		if(showSql) {
			System.out.println(" INIT SQL >>  " + script + " << END SQL ");
		}
		
		// gets all the fields
		List<Field> fields = sqlUtils.allFieldsToTable(clazz);

		// creates the result
		ResultSet resultSet = super.executeSQLSelect(script);

		// get the first and unique row
		resultSet.next();

		// initialize the object
		Object object = ObjectAccessUtils.initObject(clazz);

		// iterate over the fields
		for (Field field : fields) {
			this.setValueToObject(object, field.getType(), field, resultSet, showSql);
		}

		return object;
	}

	
	private void setValueToObject(Object object, Class<?> clazz, Field field, ResultSet resultSet, boolean showSql) throws Exception {
		final String FIELD_NAME = field.getName();

		if (sqlUtils.isFieldRelationship(field)) {
			// get the name of table of the field
			final String COLUMN_NAME = sqlUtils.getColumnNameFromField(field);

			// get the value
			final Object VALUE = super.getSQLValue(COLUMN_NAME, resultSet, clazz);

			// get the class that represents the table
			final Class<?> RELATIONSHIP = field.getType();

			// get the name of the primary key
			final String PK_NAME = SQLClassHelper.getPrimaryKey(RELATIONSHIP).getAnnotation(SQLIdentifier.class).identifierName();

			// creates a where condition to find the value of relationship
			SQLWhereCondition where = new SQLWhereCondition(PK_NAME, VALUE, SQLConditionType.EQUAL);

			// calls it recursively
			Object value = getSQLColumnValue(RELATIONSHIP, where, showSql );

			ObjectAccessUtils.callSetter(object, FIELD_NAME, value);

		} else {
			// if the field is just a normal column
			final String COLUMN_NAME = sqlUtils.getColumnNameFromField(field);

			final Object COLUMN_VALUE = super.getSQLValue(COLUMN_NAME, resultSet, field.getType());

			ObjectAccessUtils.callSetter(object, FIELD_NAME, COLUMN_VALUE);
		}

	}
 	
	private <E> void setPrimaryKeyValue(E object, Class<?> clazz, ResultSet resultSet, Field primaryKey) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		if (clazz.isAnnotationPresent(SQLInheritancePK.class)) {
			Object value = super.getSQLValue(clazz.getAnnotation(SQLInheritancePK.class).primaryKeyName(),resultSet, primaryKey.getType());
			ObjectAccessUtils.<E>callSetter(object, primaryKey.getName(), value);
		} else {
			Object value = super.getSQLValue(sqlUtils.getColumnNameFromField(primaryKey), resultSet, primaryKey.getType());
			ObjectAccessUtils.<E>callSetter(object, primaryKey.getName(), value);
		}
	}
	
	

	public static void main(String[] args) throws Exception {

		JDBCConnection co = new JDBCConnection(SQLMySQLConstants.URL_LOCALHOST + "teste", "root", "");
		List<SQLWhereCondition> cod = new ArrayList<>();
		cod.add(new SQLWhereCondition("NOTA", 10, SQLConditionType.GREATER_THAN_OR_EQUAL));
		cod.add(new SQLWhereCondition("Departamento_ID", 3, SQLConditionType.EQUAL));
		List<Matheus> ll = co.selectFrom(Matheus.class, cod ,true);
		ll.forEach(x -> {
			System.out.println("Ma");
			System.out.println("ID:" + x.getId());
			System.out.println("Nome: " +x.getNome());
			System.out.println("Nota:" + x.getNota());
			System.out.println();
			System.out.println("DE");
			System.out.println("ID:" + x.getDepartamento().getId());
			System.out.println("Nome:" + x.getDepartamento().getNome());
			System.out.println("Sigla: " + x.getDepartamento().getSigla());
			System.out.println();
			System.out.println("LI");
			System.out.println("Lider Nome: " + x.getDepartamento().getLider().getNome());
			System.out.println("ID: " + x.getDepartamento().getLider().getId());
			System.out.println("\n");
		});
		
		
	}

}








