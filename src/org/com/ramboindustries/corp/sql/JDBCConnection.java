package org.com.ramboindustries.corp.sql;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.com.ramboindustries.corp.sql.abstracts.SQLConnection;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;
import org.com.ramboindustries.corp.sql.enums.SQLConditionType;
import org.com.ramboindustries.corp.sql.enums.SQLSystem;
import org.com.ramboindustries.corp.sql.exceptions.SQLScriptException;
import org.com.ramboindustries.corp.sql.exceptions.SQLTableException;
import org.com.ramboindustries.corp.sql.utils.SQLClassHelper;
import org.com.ramboindustries.corp.sql.utils.SQLLogger;
import org.com.ramboindustries.corp.sql.utils.SQLScripts;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;
import org.com.ramboindustries.corp.utils.ObjectAccessUtils;

/**
 * 
 * @author kernelpanic_r
 *
 */
public final class JDBCConnection {

	private final String URL;
	private final String USER;
	private final String PASS;
	private final SQLScripts SQL_SCRIPTS = new SQLScripts();
	private Connection connection;
	private SQLSystem system;

	private final SQLLogger SQL_LOGGER = new SQLLogger();

	public JDBCConnection(final String URL, final String USER, final String PASS, SQLSystem system) {
		this.URL = URL;
		this.USER = USER;
		this.PASS = PASS;
		this.system = system;
	}

	public JDBCConnection(SQLConnection sqlConnection) {
		URL = sqlConnection.getUrl();
		USER = sqlConnection.getUser();
		PASS = sqlConnection.getPassword();
		system = sqlConnection.getSystem();
	}

	 
	public void openConnection() throws SQLException {
		connection = DriverManager.getConnection(URL, USER, PASS);
		connection.setAutoCommit(false);
	}

	 
	public void closeAll() throws SQLException {
		if (connection != null)
			connection.close();
	}

	 
	public void commit() throws SQLException {
		connection.commit();
	}

	 
	public void rollback()  {
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	 
	public ResultSet executeSQLSelect(final String SQL) throws SQLException {
		if (connection == null)
			this.openConnection();
		return connection.createStatement().executeQuery(SQL);
	}

	public ResultSet executeSQLSelect(final PreparedStatement statement) throws SQLException {
		return statement.executeQuery();
	}

	 
	public void executeSQL(final String SQL) throws SQLException {
		if (connection == null)
			this.openConnection();
		connection.prepareStatement(SQL).executeUpdate();
	}

	public void executeSQL(final PreparedStatement statement) throws SQLException {
		statement.executeUpdate();
		statement.close();
	}

	 
	public <E> Optional<E> findOne(final Class<E> CLAZZ, final SQLWhereCondition SQL_WHERE_CONDITION, final boolean SHOW_SQL)
			throws SQLException {

		// Creates the SQL Script
		final String SCRIPT = SQL_SCRIPTS.<E>createSQLSelectScript(CLAZZ, SQL_WHERE_CONDITION);

		PreparedStatement statement = connection.prepareStatement(SCRIPT);

		// set the values to prepared statement, we set the one, because it has just one
		// where condition
		// so, we use a single ?
		SQLUtils.createPreparedStatementWhereCondition(SQL_WHERE_CONDITION, statement, 1);

		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);

		try {

			// Gets all the fields from class
			final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

			// Creates the resultSet
			final ResultSet RESULT_SET = executeSQLSelect(statement);
			if (!RESULT_SET.next()) {
				// if no result was found
				return Optional.empty();
			}
			E result = createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, false);
			
			close(statement, RESULT_SET);
			return Optional.of(result);
			
		} catch (SQLException e) {
			SQL_LOGGER.showException(SCRIPT);
			throw new SQLException(e);
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	public <E> Optional<E> findOne(final Class<E> CLAZZ, final Object IDENTIFIER_VALUE, boolean SHOW_SQL) throws SQLException {
		
		// find for name the Primary Key of the class
		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);
		
		// creates a where condition
		final SQLWhereCondition WHERE = new SQLWhereCondition(PK_NAME, IDENTIFIER_VALUE, SQLConditionType.EQUAL);
		return this.<E>findOne(CLAZZ, WHERE, SHOW_SQL);
	}
	
	 
	public <E> Optional<E> findOne(Class<E> CLAZZ, List<SQLWhereCondition> WHERE, boolean SHOW_SQL) throws SQLException {

		final String SCRIPT = SQL_SCRIPTS.<E>createSQLSelectScript(CLAZZ, WHERE);
		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);

		try {

			PreparedStatement statement = connection.prepareStatement(SCRIPT);
			SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, 1);

			// Gets all the fields from class
			final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

			// Creates the resultSet
			final ResultSet RESULT_SET = executeSQLSelect(statement);
			
			// while we have rows, we will iterate over it
			if (!RESULT_SET.next()) {
				// no result found
				close(statement, RESULT_SET);
				return Optional.empty();
			}
			
			E result = createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, false);
			close(statement, RESULT_SET);
			return Optional.of(result);
	
		} catch (SQLException e) {
			SQL_LOGGER.showException(SCRIPT);
			throw new SQLException(e);
		} catch (Exception e) {
			throw new SQLException(e);
		} 

	}

	/**
	 * Simple SELECT * FROM TABLE
	 * 
	 * @param clazz that represents the table
	 * @return an array list with all the objects and it's relationships
	 * @throws SQLException
	 */
	 
	public <E> List<E> select(final Class<E> CLAZZ, final boolean SHOW_SQL) throws SQLException {
		final String SCRIPT = SQL_SCRIPTS.<E>createSQLSelectScript(CLAZZ);
		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);

		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		// init the list of objects
		List<E> objects = new ArrayList<>();

		// will capture the lines of the table
		final ResultSet RESULT_SET = executeSQLSelect(SCRIPT);

		while (RESULT_SET.next()) {
			try {
				objects.add(createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, SHOW_SQL));
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException(e);
			}
		}
		close(null, RESULT_SET);
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
	 
	public <E> List<E> select(final Class<E> CLAZZ, final SQLWhereCondition WHERE_CONDITION, final boolean SHOW_SQL)
			throws SQLException {
		final String SCRIPT = SQL_SCRIPTS.createSQLSelectScript(CLAZZ, WHERE_CONDITION);
		
		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE_CONDITION, statement, 1);
		
		if (SHOW_SQL)SQL_LOGGER.showScript(SCRIPT);

		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		List<E> objects = new ArrayList<>();
		final ResultSet RESULT_SET = executeSQLSelect(statement);

		while (RESULT_SET.next()) {
			try {
				objects.add(createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, SHOW_SQL));
			} catch (SQLScriptException e) {
				SQL_LOGGER.showException(e.getScript());
				throw e;
			} catch (SQLException e) {
				throw new SQLException(e);
			} catch (Exception e) {
				SQL_LOGGER.showException(e.getMessage());
				throw new SQLException(e);
			}
		}
		
		close(statement, RESULT_SET);
		return objects;
	}

	 
	public <E> List<E> select(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE_CONDITIONS,
			final boolean SHOW_SQL) throws SQLException {

		final String SCRIPT = SQL_SCRIPTS.createSQLSelectScript(CLAZZ, WHERE_CONDITIONS);
		if (SHOW_SQL) SQL_LOGGER.showScript(SCRIPT);

		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE_CONDITIONS, statement, 1);
		
		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		List<E> objects = new ArrayList<>();
		final ResultSet RESULT_SET = executeSQLSelect(statement);

		while (RESULT_SET.next()) {
			try {
				objects.add(createObjectFromLine(RESULT_SET, FIELDS, CLAZZ, SHOW_SQL));
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException(e);
			}
		}

		close(statement, RESULT_SET);
		return objects;
	}

	/**
	 * Persist the java object to the database, and then return the object with all
	 * his relationships
	 */
	@SuppressWarnings("unchecked")
	public <E> Optional<E> insert(final E OBJECT, final boolean SHOW_SQL) throws Exception {

		final Class<E> CLAZZ = (Class<E>) OBJECT.getClass();

		// return a String that contains the SQL and a List that contains the values
		final SQLJavaStatement javaStatement = SQL_SCRIPTS.createSQLInsertScript(OBJECT);

		// get the Script that was generated
		final String SCRIPT = javaStatement.getSql();

		PreparedStatement statement = connection.prepareStatement(SCRIPT);

		// changes the ? to the real values
		SQLUtils.createPreparedStatementObject(javaStatement.getValues(), statement);

		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);

		executeSQL(statement);

		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);

		final String MAX_ID = SQL_SCRIPTS.createSQLMaxSelectScript(CLAZZ);
		final ResultSet RESULT_SET = this.executeSQLSelect(MAX_ID);

		if (SHOW_SQL) {
			SQL_LOGGER.showScript(MAX_ID);
		}

		Long maxID = null;
		if (RESULT_SET.next()) {
			maxID = RESULT_SET.getLong(1);
		}

		// creates a where condition to find the last register
		final SQLWhereCondition WHERE = new SQLWhereCondition(PK_NAME, maxID, SQLConditionType.EQUAL);

		// return the register and convert to object
		return this.<E>findOne(CLAZZ, WHERE, SHOW_SQL);
	}

	 
	public <E> void createSQLTable(final Class<E> CLAZZ, final boolean SHOW_SQL) throws SQLException {
		if (CLAZZ.isAnnotationPresent(SQLTable.class)) {
			final String CREATE_TABLE = SQL_SCRIPTS.createSQLTableScript(CLAZZ, system);
			String dropTable = null;

			// if the Class has to drop the table
			if (CLAZZ.getAnnotation(SQLTable.class).dropTableIfExists()) {
				dropTable = SQL_SCRIPTS.createSQLDropTableScript(CLAZZ);
				if (SHOW_SQL) {
					SQL_LOGGER.showDropTableScript(dropTable);
				}
			}
			if (SHOW_SQL) {
				SQL_LOGGER.showCreateTableScript(CREATE_TABLE);
			}
			if (dropTable != null) {
				// if the table exists, it will be dropped
				executeSQL(dropTable);
			}
			// execute the script to create the table
			executeSQL(CREATE_TABLE);
		} else {
			// if the class does not have the @SQLTable annotation
			throw new SQLTableException(CLAZZ);
		}
	}

	/**
	 * Delete a row at the database
	 * 
	 * @param CLAZZ
	 * @param WHERE
	 * @param SHOW_SQL
	 * @throws SQLException
	 */
	 
	public <E> void delete(final Class<E> CLAZZ, final SQLWhereCondition WHERE, final boolean SHOW_SQL)
			throws SQLException {
		
		final String SCRIPT = SQL_SCRIPTS.createSQLDeleteScript(CLAZZ, WHERE);
		
		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, 1);
		
		if (SHOW_SQL) 	SQL_LOGGER.showScript(SCRIPT);
		executeSQL(statement);
	}
	
	
	public <E> void delete(final Class<E> CLAZZ, final Object IDENTIFIER_VALUE, final boolean SHOW_SQL) throws SQLException {
		
		// we get the primary key name
		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);
		SQLWhereCondition WHERE = new SQLWhereCondition(PK_NAME, IDENTIFIER_VALUE, SQLConditionType.EQUAL);
		this.<E>delete(CLAZZ, WHERE, SHOW_SQL);
		
	}

	 
	public <E> void delete(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE, final boolean SHOW_SQL)
			throws SQLException {
		final String SCRIPT = SQL_SCRIPTS.createSQLDeleteScript(CLAZZ, WHERE);
		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, 1);
		if (SHOW_SQL) SQL_LOGGER.showScript(SCRIPT);
		executeSQL(statement);
	}

	/**
	 * Merge object to database, usually at update statements
	 */
	@SuppressWarnings("unchecked")
	public <E> Optional<E> update(E OBJECT, SQLWhereCondition WHERE, boolean SHOW_SQL) throws Exception {

		final SQLJavaStatement javaStatement = SQL_SCRIPTS.createSQLUpdateScript(OBJECT, WHERE);
		final String SCRIPT = javaStatement.getSql();
		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);

		PreparedStatement statement = connection.prepareStatement(SCRIPT);

		SQLUtils.createPreparedStatementObject(javaStatement.getValues(), statement);

		// like we just have one where statement, we get the number of parameters
		int numberStatements = statement.getParameterMetaData().getParameterCount();

		// set to the WHERE Statement
		SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, numberStatements);

		// makes a downcast to generic class
		Class<E> CLAZZ = (Class<E>) OBJECT.getClass();

		// we make the update
		executeSQL(statement);

		// get the primary key value of the object
		final Object PRIMARY_KEY_VALUE = SQLClassHelper.getPrimaryKeyValue(OBJECT);

		// get the name of the primary key
		final String PRIMARY_KEY_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);

		// creates a where condition to find the row that was updated
		final SQLWhereCondition WHERE_CONDITION = new SQLWhereCondition(PRIMARY_KEY_NAME, PRIMARY_KEY_VALUE,
				SQLConditionType.EQUAL);

		// return the object with all its relationships
		return this.<E>findOne(CLAZZ, WHERE_CONDITION, SHOW_SQL);
	}
	
	/**
	 * Let the user update an object with only his identifier value
	 * @param OBJECT
	 * @param IDENTIFIER_VALUE
	 * @param SHOW_SQL
	 * @return
	 * @throws Exception
	 */
	public <E> Optional<E> update(final E OBJECT, final Object IDENTIFIER_VALUE, final boolean SHOW_SQL) throws Exception{
		
		// get the name of the primary kery
		final String PK_NAME = SQLUtils.getPrimaryKeyName(OBJECT.getClass());
		final SQLWhereCondition WHERE = new SQLWhereCondition(PK_NAME, IDENTIFIER_VALUE, SQLConditionType.EQUAL);
		return this.<E>update(OBJECT, WHERE, SHOW_SQL);
	}
	

	@SuppressWarnings("unchecked")
	public <E> Optional<E> update(final E OBJECT, final List<SQLWhereCondition> WHERE, final boolean SHOW_SQL)
			throws Exception {

		SQLJavaStatement javaStatement = SQL_SCRIPTS.createSQLUpdateScript(OBJECT, WHERE);
		final String SCRIPT = javaStatement.getSql();
		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);
		// makes a downcast to generic class
		Class<E> CLAZZ = (Class<E>) OBJECT.getClass();

		PreparedStatement statement = connection.prepareStatement(SCRIPT);

		SQLUtils.createPreparedStatementObject(javaStatement.getValues(), statement);

		// the number of where conditions
		int whereNumber = WHERE.size() - 1;

		// we get the number where the where statement characters starts
		int numberOccurrences = statement.getParameterMetaData().getParameterCount() - whereNumber;

		// make the statement of the list of WHERE conditions
		SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, numberOccurrences);

		// we make the update
		executeSQL(statement);

		// get the primary key value of the object
		final Object PRIMARY_KEY_VALUE = SQLClassHelper.getPrimaryKeyValue(OBJECT);

		// get the name of the primary key
		final String PRIMARY_KEY_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);

		// creates a where condition to find the row that was updated
		final SQLWhereCondition WHERE_CONDITION = new SQLWhereCondition(PRIMARY_KEY_NAME, PRIMARY_KEY_VALUE,
				SQLConditionType.EQUAL);

		// return the object with all its relationships
		return this.<E>findOne(CLAZZ, WHERE_CONDITION, SHOW_SQL);
	}

	private <E> E createObjectFromLine(final ResultSet RESULT_SET, final List<Field> FIELDS, final Class<E> CLAZZ,
			final boolean SHOW_SQL) throws SQLException, Exception {

		// Create an object, and we set it the primary key value to the object, we get
		// the first field that is the one who represents primary key
		E object = setPrimaryKeyValue(ObjectAccessUtils.<E>initObject(CLAZZ), CLAZZ, RESULT_SET, FIELDS.get(0));

		final byte FIELDS_SIZE = (byte) FIELDS.size();

		// iterate over fields, the primary key was already set, so we can start at 1
		for (byte i = 1; i < FIELDS_SIZE; i++) {

			final Field ACTUAL_FIELD = FIELDS.get(i);

			// get the name of the field
			final String FIELD_NAME = ACTUAL_FIELD.getName();

			if (SQLUtils.isFieldRelationship(ACTUAL_FIELD)) {
				// if the field is a foreign key, so we have to create an object

				if (ACTUAL_FIELD.getAnnotation(SQLForeignKey.class).lazyLoad())
					continue;
				// if lazy load was seated to false, we do not need to load the
				// relationship

				// the column of the actual table
				final String COLUMN_RELATIONSHIP = SQLUtils.getColumnNameFromField(ACTUAL_FIELD);

				// the column that is the name of the Foreign Key
				final String COLUMN_FOREIGN = SQLUtils.getPrimaryKeyName(ACTUAL_FIELD.getType());

				// gets the value of the actual table
				final Object COLUMN_VALUE = SQLUtils.getSQLValue(COLUMN_RELATIONSHIP, RESULT_SET,
						ACTUAL_FIELD.getType());

				// creates a where condition
				final SQLWhereCondition WHERE_CONDITION = new SQLWhereCondition(COLUMN_FOREIGN, COLUMN_VALUE,
						SQLConditionType.EQUAL);

				final Object VALUE = this.getSQLColumnValue(ACTUAL_FIELD.getType(), WHERE_CONDITION, CLAZZ, SHOW_SQL);

				ObjectAccessUtils.<E>callSetter(object, FIELD_NAME, VALUE);

			} else {
				// this is a normal field, that we do not need to create an object to them

				final String COLUMN_NAME = SQLUtils.getColumnNameFromField(FIELDS.get(i));

				final Object COLUMN_VALUE = SQLUtils.getSQLValue(COLUMN_NAME, RESULT_SET, FIELDS.get(i).getType());

				// calls the object setter
				ObjectAccessUtils.<E>callSetter(object, FIELD_NAME, COLUMN_VALUE);

			}
		}
		return object;
	}

	private Object getSQLColumnValue(final Class<?> CLAZZ, final SQLWhereCondition WHERE_CONDITION,
			Class<?> DECLARED_CLASS, final boolean SHOW_SQL) throws Exception {

		// creates the select script
		final String SCRIPT = SQL_SCRIPTS.createSQLSelectScript(CLAZZ, WHERE_CONDITION);
		if (SHOW_SQL)
			SQL_LOGGER.showScript(SCRIPT);

		// gets all the fields
		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		ResultSet resultSet = null;

		try {
			// creates the result
			resultSet = executeSQLSelect(SCRIPT);
			// get the first and unique row
			resultSet.next();

		} catch (SQLException e) {
			// if the script was incorrect formed
			throw new SQLScriptException(e.getMessage(), SCRIPT);
		}

		// initialize the object
		Object object = ObjectAccessUtils.initObject(CLAZZ);

		// iterate over the fields
		for (final Field FIELD : FIELDS) {
			this.setValueToObject(object, FIELD.getType(), FIELD, resultSet, CLAZZ, SHOW_SQL);
		}

		return object;
	}

	private void setValueToObject(Object object, final Class<?> CLAZZ, final Field FIELD, final ResultSet RESULT_SET,
			final Class<?> DECLARED_CLASS, final boolean SHOW_SQL) throws Exception {
		final String FIELD_NAME = FIELD.getName();

		if (SQLUtils.isFieldRelationship(FIELD)) {
			// get the name of table of the field
			final String COLUMN_NAME = SQLUtils.getColumnNameFromField(FIELD);

			// get the value
			final Object VALUE = SQLUtils.getSQLValue(COLUMN_NAME, RESULT_SET, CLAZZ);

			// get the class that represents the table
			final Class<?> RELATIONSHIP = FIELD.getType();

			// get the name of the PK of the field that represents the table
			final String PK_NAME = SQLUtils.getPrimaryKeyName(RELATIONSHIP);

			// creates a where condition to find the value of relationship
			final SQLWhereCondition WHERE = new SQLWhereCondition(PK_NAME, VALUE, SQLConditionType.EQUAL);

			// Class that declared the field, so we have to pass it here
			final Class<?> DECLARING_CLASS = FIELD.getDeclaringClass();

			// calls it recursively,
			Object value = getSQLColumnValue(RELATIONSHIP, WHERE, DECLARING_CLASS, SHOW_SQL);

			ObjectAccessUtils.callSetter(object, FIELD_NAME, value);

		} else {
			// if the column is not a relationship
			String columnName = null;
			Object columnValue = null;

			if (DECLARED_CLASS.isAnnotationPresent(SQLInheritancePK.class)
					&& FIELD.isAnnotationPresent(SQLIdentifier.class)) {
				// if the field is a primary key of another class, and
				// we have the SQLInheritancePK on the class that inheritance the field
				columnName = SQLUtils.getPrimaryKeyName(DECLARED_CLASS);
				columnValue = SQLUtils.getSQLValue(columnName, RESULT_SET, FIELD.getType());

			} else {
				columnName = SQLUtils.getColumnNameFromField(FIELD);
				columnValue = SQLUtils.getSQLValue(columnName, RESULT_SET, FIELD.getType());

			}
			ObjectAccessUtils.callSetter(object, FIELD_NAME, columnValue);

		}

	}

	/**
	 * Set the primary key value
	 * 
	 * @param object
	 * @param CLAZZ
	 * @param RESULT_SET
	 * @param PRIMARY_KEY
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	private <E> E setPrimaryKeyValue(E object, final Class<?> CLAZZ, final ResultSet RESULT_SET,
			final Field PRIMARY_KEY) throws SQLException, Exception {
		Object value = null;
		/**
		 * If the class has the annotation that set the name of the Primary Keys
		 */
		if (CLAZZ.isAnnotationPresent(SQLInheritancePK.class)) {
			value = SQLUtils.getSQLValue(CLAZZ.getAnnotation(SQLInheritancePK.class).primaryKeyName(), RESULT_SET,
					PRIMARY_KEY.getType());
		} else {
			value = SQLUtils.getSQLValue(SQLUtils.getColumnNameFromField(PRIMARY_KEY), RESULT_SET,
					PRIMARY_KEY.getType());
		}
		ObjectAccessUtils.<E>callSetter(object, PRIMARY_KEY.getName(), value);
		return object;
	}
	
	private void close(PreparedStatement statement, ResultSet resultSet) throws SQLException {
		if(statement != null && !statement.isClosed()) statement.close();
		if(resultSet != null && !resultSet.isClosed()) resultSet.close();
	}

}
