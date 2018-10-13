package org.com.ramboindustries.corp.sql;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.com.ramboindustries.corp.sql.abstracts.SQLConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLJdbc;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConstants;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;
import org.com.ramboindustries.corp.test.Aluno;
import org.com.ramboindustries.corp.test.Matheus;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

public final class JDBCConnection extends SQLConnection {

	private final SQLUtils SQL_UTILS;

	public JDBCConnection(String URL, String USER, String PASS) {
		super(URL, USER, PASS);
		SQL_UTILS = new SQLUtils();
	}

	/**
	 * Simple SELECT * FROM TABLE
	 * 
	 * @param clazz that represents the table
	 * @return an array list with all the objects and it's relationships
	 * @throws SQLException 
	 */
	public <E> List<E> selectFrom(final Class<E> CLAZZ, final boolean SHOW_SQL) throws SQLException {
		final String SCRIPT = SQL_UTILS.<E>createSQLSelectScript(CLAZZ);
		if (SHOW_SQL) {
			SQL_UTILS.SQL_LOGGER(SCRIPT);
		}
		final List<Field> FIELDS = SQL_UTILS.allFieldsToTable(CLAZZ);

		// init the list of objects
		List<E> objects = new ArrayList<>();

		// will capture the lines of the table
		final ResultSet RESULT_SET = super.executeSQLSelect(SCRIPT);

		while (RESULT_SET.next()) {
			try {
				objects.add(this.createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, SHOW_SQL));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
				throw new SQLException(e);
			}
		}

		return objects;
	}

	/**
	 * Select FROM Table with a WHERE clause
	 * 
	 * @param clazz             that represents the table
	 * @param sqlWhereCondition the condidion
	 * @return an array list
	 * @throws SQLException 
	 * @throws Exception
	 */
	public <E> List<E> selectFrom(final Class<E> CLAZZ, final SQLWhereCondition WHERE_CONDITION,
			final boolean SHOW_SQL) throws SQLException {
		final String SCRIPT = SQL_UTILS.createSQLSelectScript(CLAZZ, WHERE_CONDITION);

		if (SHOW_SQL) {
			SQL_UTILS.SQL_LOGGER(SCRIPT);
		}

		final List<Field> FIELDS = SQL_UTILS.allFieldsToTable(CLAZZ);

		List<E> objects = new ArrayList<>();
		final ResultSet RESULT_SET = super.executeSQLSelect(SCRIPT);

		while (RESULT_SET.next()) {
			try {
				objects.add(this.createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, SHOW_SQL));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
				throw new SQLException(e);
			}
		}

		return objects;
	}

	public <E> List<E> selectFrom(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE_CONDITIONS,
			final boolean SHOW_SQL) throws SQLException {

		final String SCRIPT = SQL_UTILS.createSQLSelectScript(CLAZZ, WHERE_CONDITIONS);
		if (SHOW_SQL) {
			SQL_UTILS.SQL_LOGGER(SCRIPT);
		}
		final List<Field> FIELDS = SQL_UTILS.allFieldsToTable(CLAZZ);

		List<E> objects = new ArrayList<>();
		final ResultSet RESULT_SET = super.executeSQLSelect(SCRIPT);

		while (RESULT_SET.next()) {
			try {
				objects.add(this.createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, SHOW_SQL));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
				throw new SQLException(e);
			}
		}

		return objects;
	}

	public List<Object[]> selectFrom(final Class<?> CLAZZ, final Field[] COLUMNS, boolean SHOW_SQL) throws SQLException {
		final String SCRIPT = SQL_UTILS.createSQLSelectScript(CLAZZ, COLUMNS);

		if (SHOW_SQL) {
			SQL_UTILS.SQL_LOGGER(SCRIPT);
		}
		List<Object[]> objects = new ArrayList<>();
		final ResultSet RESULT_SET = super.executeSQLSelect(SCRIPT);

		while (RESULT_SET.next()) {
			final byte LENGTH = (byte) COLUMNS.length;
			final Object[] OBJECT = new Object[LENGTH];
			for (byte i = 0; i < LENGTH; i++) {
				OBJECT[i] = super.getSQLValue(SQL_UTILS.getColumnNameFromField(COLUMNS[i]), RESULT_SET,
						COLUMNS[i].getType());
			}
			objects.add(OBJECT);
		}
		return objects;
	}

	private <E> E createObjectFromLine(final ResultSet RESULT_SET, final List<Field> FIELDS, final Class<E> CLAZZ,
			final boolean SHOW_SQL) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, IntrospectionException {

		// create and initialize the object
		E object = ObjectAccessUtils.<E>initObject(CLAZZ);

		// set the primary key value, for the first item of the element
		// primary key, will always be the first element
		this.setPrimaryKeyValue(object, CLAZZ, RESULT_SET, FIELDS.get(0));

		final byte FIELDS_SIZE = (byte) FIELDS.size();

		// iterate over fields, the primary key was already set, so we can start at 1
		for (byte i = 1; i < FIELDS_SIZE; i++) {

			// get the name of the field
			final String FIELD_NAME = FIELDS.get(i).getName();

			if (SQL_UTILS.isFieldRelationship(FIELDS.get(i))) {
				// if the field is a foreign key, so we have to create an object

				final String COLUMN_RELATIONSHIP = SQL_UTILS
						.getColumnNameFromField(SQLClassHelper.getPrimaryKey(FIELDS.get(i).getType()));
				final Object COLUMN_VALUE = super.getSQLValue(COLUMN_RELATIONSHIP, RESULT_SET, FIELDS.get(i).getType());

				// creates a where condition
				final SQLWhereCondition WHERE_CONDITION = new SQLWhereCondition(COLUMN_RELATIONSHIP, COLUMN_VALUE,
						SQLConditionType.EQUAL);
				final Object VALUE = this.getSQLColumnValue(FIELDS.get(i).getType(), WHERE_CONDITION, SHOW_SQL);

				ObjectAccessUtils.<E>callSetter(object, FIELD_NAME, VALUE);

			} else {
				// this is a normal field, that we do not need to create an object to them

				final String COLUMN_NAME = SQL_UTILS.getColumnNameFromField(FIELDS.get(i));

				final Object COLUMN_VALUE = super.getSQLValue(COLUMN_NAME, RESULT_SET, FIELDS.get(i).getType());

				// calls the object setter
				ObjectAccessUtils.<E>callSetter(object, FIELD_NAME, COLUMN_VALUE);

			}
		}
		return object;
	}

	private Object getSQLColumnValue(final Class<?> CLAZZ, final SQLWhereCondition WHERE_CONDITION,
			final boolean SHOW_SQL) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			SQLException, IntrospectionException, InstantiationException, SQLIdentifierException {

		// creates the select script
		final String SCRIPT = SQL_UTILS.createSQLSelectScript(CLAZZ, WHERE_CONDITION);

		if (SHOW_SQL) {
			SQL_UTILS.SQL_LOGGER(SCRIPT);
		}

		// gets all the fields
		final List<Field> FIELDS = SQL_UTILS.allFieldsToTable(CLAZZ);

		// creates the result
		final ResultSet RESULT_SET = super.executeSQLSelect(SCRIPT);

		// get the first and unique row
		RESULT_SET.next();

		// initialize the object
		Object object = ObjectAccessUtils.initObject(CLAZZ);

		// iterate over the fields
		for (final Field FIELD : FIELDS) {
			this.setValueToObject(object, FIELD.getType(), FIELD, RESULT_SET, SHOW_SQL);
		}

		return object;
	}

	private void setValueToObject(Object object, final Class<?> CLAZZ, final Field FIELD, final ResultSet RESULT_SET,
			final boolean SHOW_SQL) throws SQLException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IntrospectionException, InstantiationException, SQLIdentifierException {
		final String FIELD_NAME = FIELD.getName();

		if (SQL_UTILS.isFieldRelationship(FIELD)) {
			// get the name of table of the field
			final String COLUMN_NAME = SQL_UTILS.getColumnNameFromField(FIELD);

			// get the value
			final Object VALUE = super.getSQLValue(COLUMN_NAME, RESULT_SET, CLAZZ);

			// get the class that represents the table
			final Class<?> RELATIONSHIP = FIELD.getType();

			// get the name of the primary key
			final String PK_NAME = SQLClassHelper.getPrimaryKey(RELATIONSHIP).getAnnotation(SQLIdentifier.class)
					.identifierName();

			// creates a where condition to find the value of relationship
			final SQLWhereCondition WHERE = new SQLWhereCondition(PK_NAME, VALUE, SQLConditionType.EQUAL);

			// calls it recursively
			Object value = getSQLColumnValue(RELATIONSHIP, WHERE, SHOW_SQL);

			ObjectAccessUtils.callSetter(object, FIELD_NAME, value);

		} else {
			// if the field is just a normal column
			final String COLUMN_NAME = SQL_UTILS.getColumnNameFromField(FIELD);

			final Object COLUMN_VALUE = super.getSQLValue(COLUMN_NAME, RESULT_SET, FIELD.getType());

			ObjectAccessUtils.callSetter(object, FIELD_NAME, COLUMN_VALUE);
		}

	}

	private <E> void setPrimaryKeyValue(E object, final Class<?> CLAZZ, final ResultSet RESULT_SET,
			final Field PRIMARY_KEY) throws SQLException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IntrospectionException {
		if (CLAZZ.isAnnotationPresent(SQLInheritancePK.class)) {
			final Object VALUE = super.getSQLValue(CLAZZ.getAnnotation(SQLInheritancePK.class).primaryKeyName(),
					RESULT_SET, PRIMARY_KEY.getType());
			ObjectAccessUtils.<E>callSetter(object, PRIMARY_KEY.getName(), VALUE);
		} else {
			final Object VALUE = super.getSQLValue(SQL_UTILS.getColumnNameFromField(PRIMARY_KEY), RESULT_SET,
					PRIMARY_KEY.getType());
			ObjectAccessUtils.<E>callSetter(object, PRIMARY_KEY.getName(), VALUE);
		}
	}

	public static void main(String[] args) throws Exception {

		
		

		SQLJdbc jdbc = new JDBCConnection(SQLMySQLConstants.URL_LOCALHOST + "teste", "root", "");
		Field[] a = { Matheus.class.getDeclaredField("departamento"), Aluno.class.getDeclaredField("nota") };
		List<Matheus> obj = jdbc.selectFrom(Matheus.class,  true);
		System.out.println(obj.size());
		
		
	}

}
