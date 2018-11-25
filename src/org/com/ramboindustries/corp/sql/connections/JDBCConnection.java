package org.com.ramboindustries.corp.sql.connections;

import static org.com.ramboindustries.corp.sql.utils.SQLLogger.showException;
import static org.com.ramboindustries.corp.sql.utils.SQLLogger.showScript;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.com.ramboindustries.corp.sql.abstracts.SQLBasicWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLInfo;
import org.com.ramboindustries.corp.sql.abstracts.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;
import org.com.ramboindustries.corp.sql.classsql.SQLJavaStatement;
import org.com.ramboindustries.corp.sql.enums.SQLBasicConditionType;
import org.com.ramboindustries.corp.sql.exceptions.SQLTableException;
import org.com.ramboindustries.corp.sql.factory.SQLConnectionFactory;
import org.com.ramboindustries.corp.sql.utils.JDBCUtils;
import org.com.ramboindustries.corp.sql.utils.ObjectAccessUtils;
import org.com.ramboindustries.corp.sql.utils.SQLClassHelper;
import org.com.ramboindustries.corp.sql.utils.SQLLogger;
import org.com.ramboindustries.corp.sql.utils.SQLScripts;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;

/**
 * The final User can use this class explicit
 * @author kernelpanic_r
 *
 */
class JDBCConnection {

	private SQLInfo sqlConnection;
	protected Connection connection;
	private boolean SHOW_SQL;
	protected boolean autoCommit;

	public JDBCConnection(SQLInfo sqlConnection, final boolean SHOW_SQL, final boolean autoCommit) {
		this.sqlConnection = sqlConnection;
		this.SHOW_SQL = SHOW_SQL;
		this.autoCommit = autoCommit;
	}

	 
	public void openConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = new SQLConnectionFactory().getConnection(sqlConnection);
			connection.setAutoCommit(autoCommit);
		}
	}

	 
	public void closeAll() throws SQLException {
		if (connection != null)
			connection.close();
	}

	public void commit() throws SQLException {
		if (autoCommit) {
			SQLLogger.showCantCommitOrRollback();
		} else {
			connection.commit();
			SQLLogger.showCommit();
		}
	}

	public void rollback() throws SQLException {
		if (autoCommit) {
			SQLLogger.showCantCommitOrRollback();
		} else {
			connection.rollback();
			SQLLogger.showRollback();
		}
	}

	 
	public ResultSet executeSQLSelect(final String SQL) throws SQLException {
		openConnection();
		return connection.createStatement().executeQuery(SQL);
	}

	public ResultSet executeSQLSelect(final PreparedStatement statement) throws SQLException {
		return statement.executeQuery();
	}

	 
	public void executeSQL(final String SQL) throws SQLException {
		openConnection();
		connection.prepareStatement(SQL).executeUpdate();
	}

	public void executeSQL(final PreparedStatement statement) throws SQLException {
		statement.executeUpdate();
		statement.close();
	}

	 
	protected <E> Optional<E> findOne(final Class<E> CLAZZ, final SQLWhereCondition SQL_WHERE_CONDITION)
			throws SQLException {

		// open the connection
		openConnection();
		
		// Creates the SQL Script
		final String SCRIPT = SQLScripts.<E>createSQLSelectScript(CLAZZ, SQL_WHERE_CONDITION);

		PreparedStatement statement = connection.prepareStatement(SCRIPT);

		// set the values to prepared statement, we set the one, because it has just one
		// where condition
		// so, we use a single ?
		SQLUtils.createPreparedStatementWhereCondition(SQL_WHERE_CONDITION, statement, 1);

		if (SHOW_SQL) showScript(SCRIPT);
		try {

			// Gets all the fields from class
			final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

			// Creates the resultSet
			final ResultSet RESULT_SET = executeSQLSelect(statement);
			if (!RESULT_SET.next()) {
				// if no result was found
				return Optional.empty();
			}
			E result = createObjectFromLine(RESULT_SET, FIELDS, CLAZZ);
			
			close(statement, RESULT_SET);
			return Optional.of(result);
			
		} catch (SQLException e) {
			showException(SCRIPT, e);
			throw new SQLException(e);
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	protected <E> Optional<E> findOne(final Class<E> CLAZZ, final Object IDENTIFIER_VALUE) throws SQLException {
		
		// find for name the Primary Key of the class
		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);
		
		// creates a where condition
		final SQLWhereCondition WHERE = new SQLBasicWhereCondition(PK_NAME, IDENTIFIER_VALUE, SQLBasicConditionType.EQUAL);
		return this.<E>findOne(CLAZZ, WHERE);
	}
	
	 
	protected <E> Optional<E> findOne(Class<E> CLAZZ, List<SQLWhereCondition> WHERE) throws SQLException {

		openConnection();
		
		final String SCRIPT = SQLScripts.<E>createSQLSelectScript(CLAZZ, WHERE);
		if (SHOW_SQL) showScript(SCRIPT);
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
			
			E result = createObjectFromLine(RESULT_SET, FIELDS, CLAZZ);
			close(statement, RESULT_SET);
			return Optional.of(result);
	
		} catch (SQLException e) {
			showException(SCRIPT, e);
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
	 
	protected <E> List<E> select(final Class<E> CLAZZ) throws SQLException {
		final String SCRIPT = SQLScripts.<E>createSQLSelectScript(CLAZZ);
		if (SHOW_SQL) showScript(SCRIPT);

		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		// init the list of objects
		List<E> objects = new ArrayList<>();

		// will capture the lines of the table
		final ResultSet RESULT_SET = executeSQLSelect(SCRIPT);

		while (RESULT_SET.next()) {
			try {
				objects.add(createObjectFromLine(RESULT_SET, FIELDS, CLAZZ));
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
	 * @throws Exception
	 */
	 
	protected <E> List<E> select(final Class<E> CLAZZ, final SQLWhereCondition WHERE_CONDITION)
			throws SQLException {
		final String SCRIPT = SQLScripts.<E>createSQLSelectScript(CLAZZ, WHERE_CONDITION);
		
		openConnection();
		
		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE_CONDITION, statement, 1);
		
		if (SHOW_SQL)showScript(SCRIPT);

		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		List<E> objects = new ArrayList<>();
		final ResultSet RESULT_SET = executeSQLSelect(statement);

		while (RESULT_SET.next()) {
			try {
				objects.add(createObjectFromLine(RESULT_SET, FIELDS, CLAZZ));
			} catch (SQLException e) {
				showException(SCRIPT, e);
				throw new SQLException(e);
			}
		}
		
		close(statement, RESULT_SET);
		return objects;
	}

	 
	protected <E> List<E> select(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE_CONDITIONS) throws SQLException {

		openConnection();
		
		final String SCRIPT = SQLScripts.<E>createSQLSelectScript(CLAZZ, WHERE_CONDITIONS);
		if (SHOW_SQL) showScript(SCRIPT);

		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE_CONDITIONS, statement, 1);
		
		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		List<E> objects = new ArrayList<>();
		final ResultSet RESULT_SET = executeSQLSelect(statement);

		while (RESULT_SET.next()) {
			try {
				objects.add(createObjectFromLine(RESULT_SET, FIELDS, CLAZZ));
			} catch (SQLException e) {
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
	protected <E> Optional<E> insert(final E OBJECT) throws Exception {

		openConnection();
		
		final Class<E> CLAZZ = (Class<E>) OBJECT.getClass();

		// return a String that contains the SQL and a List that contains the values
		final SQLJavaStatement javaStatement = SQLScripts.<E>createSQLInsertScript(OBJECT);

		// get the Script that was generated
		final String SCRIPT = javaStatement.getSql();

		PreparedStatement statement = connection.prepareStatement(SCRIPT);

		// changes the ? to the real values
		SQLUtils.createPreparedStatementObject(javaStatement.getValues(), statement);

		if (SHOW_SQL) showScript(SCRIPT);

		executeSQL(statement);

		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);

		final String MAX_ID = SQLScripts.createSQLMaxSelectScript(CLAZZ);
		final ResultSet RESULT_SET = executeSQLSelect(MAX_ID);

		if (SHOW_SQL) {
			showScript(MAX_ID);
		}

		Long maxID = null;
		if (RESULT_SET.next()) {
			maxID = RESULT_SET.getLong(1);
		}

		// creates a where condition to find the last register
		final SQLWhereCondition WHERE = new SQLBasicWhereCondition(PK_NAME, maxID, SQLBasicConditionType.EQUAL);

		// return the register and convert to object
		return this.<E>findOne(CLAZZ, WHERE);
	}

	 
	protected <E> void createSQLTable(final Class<E> CLAZZ) throws SQLException {
		if (CLAZZ.isAnnotationPresent(SQLTable.class)) {
			final String CREATE_TABLE = SQLScripts.createSQLTableScript(CLAZZ, sqlConnection.getSystem());
			String dropTable = null;

			// if the Class has to drop the table
			if (CLAZZ.getAnnotation(SQLTable.class).dropTableIfExists()) {
				dropTable = SQLScripts.createSQLDropTableScript(CLAZZ);
				if (SHOW_SQL) {
					SQLLogger.showDropTableScript(dropTable);
				}
			}
			if (SHOW_SQL) {
				SQLLogger.showCreateTableScript(CREATE_TABLE);
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
	 
	protected <E> void delete(final Class<E> CLAZZ, final SQLWhereCondition WHERE)
			throws SQLException {
		
		openConnection();
		
		final String SCRIPT = SQLScripts.<E>createSQLDeleteScript(CLAZZ, WHERE);
		
		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, 1);
		
		if (SHOW_SQL) showScript(SCRIPT);
		executeSQL(statement);
	}
	
	
	protected <E> void delete(final Class<E> CLAZZ, final Object IDENTIFIER_VALUE) throws SQLException {
		
		// we get the primary key name
		final String PK_NAME = SQLUtils.getPrimaryKeyName(CLAZZ);
		SQLWhereCondition WHERE = new SQLBasicWhereCondition(PK_NAME, IDENTIFIER_VALUE, SQLBasicConditionType.EQUAL);
		this.<E>delete(CLAZZ, WHERE);
		
	}

	 
	protected <E> void delete(final Class<E> CLAZZ, final List<SQLWhereCondition> WHERE) throws SQLException {
		openConnection();
		final String SCRIPT = SQLScripts.<E>createSQLDeleteScript(CLAZZ, WHERE);
		PreparedStatement statement = connection.prepareStatement(SCRIPT);
		SQLUtils.createPreparedStatementWhereCondition(WHERE, statement, 1);
		if (SHOW_SQL) showScript(SCRIPT);
		executeSQL(statement);
	}

	/**
	 * Merge object to database, usually at update statements
	 */
	@SuppressWarnings("unchecked")
	protected <E> Optional<E> update(E OBJECT, SQLWhereCondition WHERE) throws Exception {

		openConnection();
		
		final SQLJavaStatement javaStatement = SQLScripts.<E>createSQLUpdateScript(OBJECT, WHERE);
		final String SCRIPT = javaStatement.getSql();
		if (SHOW_SQL) showScript(SCRIPT);

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
		final SQLWhereCondition WHERE_CONDITION = new SQLBasicWhereCondition(PRIMARY_KEY_NAME, PRIMARY_KEY_VALUE,
				SQLBasicConditionType.EQUAL);

		// return the object with all its relationships
		return this.<E>findOne(CLAZZ, WHERE_CONDITION);
	}
	
	/**
	 * Let the user update an object with only his identifier value
	 * @param OBJECT
	 * @param IDENTIFIER_VALUE
	 * @param SHOW_SQL
	 * @return
	 * @throws Exception
	 */
	protected <E> Optional<E> update(final E OBJECT, final Object IDENTIFIER_VALUE) throws Exception{
		
		// get the name of the primary kery
		final String PK_NAME = SQLUtils.getPrimaryKeyName(OBJECT.getClass());
		final SQLWhereCondition WHERE = new SQLBasicWhereCondition(PK_NAME, IDENTIFIER_VALUE, SQLBasicConditionType.EQUAL);
		return this.<E>update(OBJECT, WHERE);
	}
	

	@SuppressWarnings("unchecked")
	protected <E> Optional<E> update(final E OBJECT, final List<SQLWhereCondition> WHERE)
			throws Exception {

		openConnection();
		SQLJavaStatement javaStatement = SQLScripts.<E>createSQLUpdateScript(OBJECT, WHERE);
		final String SCRIPT = javaStatement.getSql();
		if (SHOW_SQL)showScript(SCRIPT);
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
		final SQLWhereCondition WHERE_CONDITION = new SQLBasicWhereCondition(PRIMARY_KEY_NAME, PRIMARY_KEY_VALUE,
				SQLBasicConditionType.EQUAL);

		// return the object with all its relationships
		return this.<E>findOne(CLAZZ, WHERE_CONDITION);
	}

	private <E> E createObjectFromLine(final ResultSet RESULT_SET, final List<Field> FIELDS, final Class<E> CLAZZ) throws SQLException {
  
		try {
		// Create an object, and we set it the primary key value to the object, we get
		// the first field that is the one who represents primary key
		E object = JDBCUtils.setPrimaryKeyValue(ObjectAccessUtils.<E>initObject(CLAZZ), CLAZZ, RESULT_SET, FIELDS.get(0));

		final byte FIELDS_SIZE = (byte) FIELDS.size();

		// iterate over fields, the primary key was already set, so we can start at 1
		for (byte i = 1; i < FIELDS_SIZE; i++) {

			final Field ACTUAL_FIELD = FIELDS.get(i);

			// get the name of the field
			final String FIELD_NAME = ACTUAL_FIELD.getName();

			if (SQLUtils.isFieldRelationship(ACTUAL_FIELD)) {
				// if the field is a foreign key, so we have to create an object

				// if the field is a lazy load, we will not load it
				if (ACTUAL_FIELD.getAnnotation(SQLForeignKey.class).fetch().isLazy())
					continue;
			

				// the column of the actual table
				final String COLUMN_RELATIONSHIP = SQLUtils.getColumnNameFromField(ACTUAL_FIELD);

				// the column that is the name of the Foreign Key
				final String COLUMN_FOREIGN = SQLUtils.getPrimaryKeyName(ACTUAL_FIELD.getType());

				// gets the value of the actual table
				final Object COLUMN_VALUE = SQLUtils.getSQLValue(COLUMN_RELATIONSHIP, RESULT_SET,
						ACTUAL_FIELD.getType());

				// creates a where condition
				final SQLWhereCondition WHERE_CONDITION = new SQLBasicWhereCondition(COLUMN_FOREIGN, COLUMN_VALUE,
						SQLBasicConditionType.EQUAL);

				final Object VALUE = getSQLColumnValue(ACTUAL_FIELD.getType(), WHERE_CONDITION, CLAZZ);

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
		}catch(Exception e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
	}

	private Object getSQLColumnValue(final Class<?> CLAZZ, final SQLWhereCondition WHERE_CONDITION,	Class<?> DECLARED_CLASS) throws Exception {

		// creates the select script
		final String SCRIPT = SQLScripts.createSQLSelectScript(CLAZZ, WHERE_CONDITION);
		if (SHOW_SQL) showScript(SCRIPT);

		// gets all the fields
		final List<Field> FIELDS = SQLUtils.allFieldsToTable(CLAZZ);

		ResultSet resultSet = null;

		try {

			// creates the result
			resultSet = executeSQLSelect(SCRIPT);
			// get the first and unique row
			resultSet.next();

			// initialize the object
			Object object = ObjectAccessUtils.initObject(CLAZZ);

			// iterate over the fields
			for (final Field FIELD : FIELDS) {
				setValueToObject(object, FIELD.getType(), FIELD, resultSet, CLAZZ);
			}
			return object;
		} catch (SQLException e) {
			throw e;
		}

		
	}

	private void setValueToObject(Object object, final Class<?> CLAZZ, final Field FIELD, final ResultSet RESULT_SET,
			final Class<?> DECLARED_CLASS) throws Exception {
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
			final SQLWhereCondition WHERE = new SQLBasicWhereCondition(PK_NAME, VALUE, SQLBasicConditionType.EQUAL);

			// Class that declared the field, so we have to pass it here
			final Class<?> DECLARING_CLASS = FIELD.getDeclaringClass();

			// calls it recursively,
			Object value = getSQLColumnValue(RELATIONSHIP, WHERE, DECLARING_CLASS);

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
	
	private void close(PreparedStatement statement, ResultSet resultSet) throws SQLException {
		if(statement != null && !statement.isClosed()) statement.close();
		if(resultSet != null && !resultSet.isClosed()) resultSet.close();
	}

}
