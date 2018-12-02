package org.com.ramboindustries.corp.sql.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.constants.SQL_DDL;
import org.com.ramboindustries.corp.sql.enums.SQLSystem;
import org.com.ramboindustries.corp.sql.exceptions.SQLKeywordException;
import org.com.ramboindustries.corp.sql.functional.SQLKeywords;
import org.com.ramboindustries.corp.sql.types.SQLDataDefinition;
import org.com.ramboindustries.corp.sql.types.SQLMySqlType;
import org.com.ramboindustries.corp.sql.types.SQLType;
import org.com.ramboindustries.corp.sql.types.TypeClass;
import org.com.ramboindustries.corp.sql.types.SQLDataDefinitionImpl;

/**
 * @author kernelpanic_r
 *
 */
public class SQLClassHelper {

	
	/**
	 * Convert an attribute to a column of SQL
	 * 
	 * @param field that will be converted to column
	 * @return a string that represents the column
	 * @throws SQLKeywordException 
	 */
	public static String attributeToSQLColumn(final Field FIELD, final SQLSystem SYSTEM) throws SQLKeywordException {
		return transformJavaFieldToSqlColumn(FIELD, SYSTEM);
	}

	/**
	 * Returns the field that is the PK of the class
	 * 
	 * @param clazz that we want to know its PK
	 * @return
	 */
	public static Field getPrimaryKey(final Class<?> CLAZZ) {
		final Field[] FIELDS = CLAZZ.getDeclaredFields();
		for (Field FIELD : FIELDS) {
			if (FIELD.isAnnotationPresent(SQLIdentifier.class)) {
				return FIELD;
			}
		}
		// if the Primary key was not found, we can call it recursively
		if (CLAZZ.getSuperclass() != null && !CLAZZ.getSuperclass().getName().equals(Object.class.getName())) {
			return getPrimaryKey(CLAZZ.getSuperclass());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <E, V> V getPrimaryKeyValue(E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		if (object == null) {
			return null;
		}

		Field field = getPrimaryKey(object.getClass());
		// if the class does not have an PK
		if (field == null) {
			return null;
		}
		Class<V> clazz = (Class<V>) field.getType();
		return ObjectAccessUtils.<E, V>callGetter(field.getName(), object, clazz);
	}

	/**
	 * Call it to change the name of the PK
	 * 
	 * @param field
	 * @param clazz
	 * @return
	 * @throws SQLKeywordException 
	 */
	public static String attributeToSQLColumn(Field field, Class<?> clazz, final SQLSystem SYSTEM) throws SQLKeywordException {
		if (field.isAnnotationPresent(SQLIdentifier.class) && clazz.isAnnotationPresent(SQLInheritancePK.class)) {
			SQLInheritancePK COLUMN = clazz.getAnnotation(SQLInheritancePK.class);
			// set the ENUM used by user
			SQLType type = setSQLTypeEnum(SYSTEM, field.getType());
			
			// like SQL SERVER uses INCREMENT and MY_SQL uses AUTO_INCREMENT
			SQLDataDefinition data = setSQLDataStructure(SYSTEM);
			return createPrimaryKeyColumn(COLUMN, type, data);
		}
		return attributeToSQLColumn(field, SYSTEM);

	}

	private static String transformJavaFieldToSqlColumn(final Field FIELD, final SQLSystem SYSTEM) throws SQLKeywordException  {
		
		// set the ENUM used
		SQLType sqlType = setSQLTypeEnum(SYSTEM, FIELD.getType());
		return createColumnByFieldType(FIELD, sqlType, SYSTEM); 
		
	}

	// verifies if the column can be null
	private static String isColumnRequired(final SQLColumn COLUMN) {
		return COLUMN.required() ? SQL_DDL.NOT_NULL : "";
	}

	// verifies if the foreign can be optional
	private static String isColumnRequired(final SQLForeignKey FOREIGN_KEY) {
		return FOREIGN_KEY.required() ? SQL_DDL.NOT_NULL : "";
	}

	// set the column length
	private static String setColumnLength(final SQLColumn COLUMN) {
		return "(" + COLUMN.length() + ")";
	}
	
	private static String setColumnLengthPrecision(final SQLColumn COLUMN) {
		return "(" + COLUMN.length() + "," + COLUMN.precision() + ")";
	}

	private static String createPrimaryKeyColumn(final SQLIdentifier PRIMARY_KEY, final SQLType SQL_TYPE, final SQLDataDefinition SQL_DATA) {
 		return PRIMARY_KEY.identifierName() + " " + SQL_TYPE.getSqlType() + " " + SQL_DDL.NOT_NULL + SQL_DATA.increment();
	}

	private static String createForeignKeyColumn(final SQLForeignKey FOREIGN_KEY, final SQLType SQL_TYPE) {
		return FOREIGN_KEY.name() + " " + SQL_TYPE.getSqlType() + " " + isColumnRequired(FOREIGN_KEY);
	}

	private static String createColumn(final SQLColumn COLUMN, final SQLType SQL_TYPE, boolean isLength) {
		if (isLength)
			return COLUMN.name() + " " + SQL_TYPE.getSqlType()+ setColumnLength(COLUMN) + " "
					+ isColumnRequired(COLUMN);
		return COLUMN.name() + " " + SQL_TYPE.getSqlType() + " " + isColumnRequired(COLUMN);
	}

	private static String createPrimaryKeyColumn(final SQLInheritancePK PRIMARY_KEY, final SQLType SQL_TYPE, final SQLDataDefinition SQL_DATA) {
			return PRIMARY_KEY.primaryKeyName() + " " + SQL_TYPE.getSqlType() + " " + SQL_DDL.NOT_NULL + SQL_DATA.increment();
	}
	
	private static String createColumn(final Field FIELD, final SQLType SQL_TYPE, boolean isLength) {
		if (isLength)
			return FIELD.getName() + " " + SQL_TYPE.getSqlType() + "(" + SQL_TYPE.defaultSize() + ")";
		return FIELD.getName() + " " + SQL_TYPE.getSqlType();
	}
	
	private static String createColumnPrecision(final SQLColumn COLUMN, final SQLType SQL_TYPE) {
			return COLUMN.name() + " " + SQL_TYPE.getSqlType()+ setColumnLengthPrecision(COLUMN) + " "
					+ isColumnRequired(COLUMN);
	}
	private static String createColumnPrecision(final Field FIELD, final SQLType SQL_TYPE) {
		return FIELD.getName() + " " + SQL_TYPE.getSqlType() + "(" + SQL_TYPE.defaultSize() + ",2)";
	}
	
	
	private static String createColumnByFieldType(final Field FIELD,  SQLType sqlType, final SQLSystem SYSTEM) throws SQLKeywordException {
		final String FIELD_NAME = FIELD.getType().getSimpleName();
		
		// the Structure type of SQL
		SQLDataDefinition dataStructure = setSQLDataStructure(SYSTEM);
		
		if (FIELD.isAnnotationPresent(SQLIdentifier.class)) {
			SQLIdentifier PK = FIELD.getAnnotation(SQLIdentifier.class);
			
			if(isUsingKeyword(PK.identifierName(), dataStructure)) {
				// means that the column is using a invalid columns name
				throw new SQLKeywordException(PK.identifierName(), SYSTEM);
			}
			
			// if the field is a PRIMARY KEY
			return createPrimaryKeyColumn(PK,sqlType, dataStructure);
			
			
		} else if (FIELD.isAnnotationPresent(SQLColumn.class)) {
			final SQLColumn COLUMN = FIELD.getAnnotation(SQLColumn.class);
			
			if(isUsingKeyword(COLUMN.name(), dataStructure)) {
				// means that the column is using a invalid columns name
				throw new SQLKeywordException(COLUMN.name(), SYSTEM);
			}
			
			if(isEnum(FIELD)) {
				// if the field is a ENUM type
				return createColumn(COLUMN, sqlType, false);
			}
			TypeClass TYPE = TypeClass.getTypeByName(FIELD_NAME);
			switch (TYPE) {
			case BIG_DECIMAL:
			case DOUBLE:
			case FLOAT:
				return createColumnPrecision(COLUMN, sqlType);
			case STRING:
			case CHARACTER:
				return createColumn(COLUMN, sqlType, true);
			default:
				// integer, long, etc ...
				return createColumn(COLUMN, sqlType, false);
			}
		} else if(FIELD.isAnnotationPresent(SQLForeignKey.class)) {
			
			final SQLForeignKey COLUMN = FIELD.getAnnotation(SQLForeignKey.class);
			
			if(isUsingKeyword(COLUMN.name(), dataStructure)) {
				throw new SQLKeywordException(COLUMN.name(), SYSTEM);
			}
			
			// get the type of the primary key that references ant set to the SQL type
			sqlType = SQLMySqlType.getSqlType(getPrimaryKey(FIELD.getType()).getType());
			return createForeignKeyColumn(COLUMN, sqlType);
		} else {
			
			if(isUsingKeyword(FIELD.getName(), dataStructure)) {
				throw new SQLKeywordException(FIELD.getName(), SYSTEM);
			}
			
			if(isEnum(FIELD)) {
				return createColumn(FIELD, sqlType, false);
			}
			TypeClass TYPE = TypeClass.getTypeByName(FIELD_NAME);
			switch (TYPE) {
			case BIG_DECIMAL:
			case DOUBLE:
			case FLOAT:
				return createColumnPrecision(FIELD, sqlType);
			case STRING:
			case CHARACTER:
				return createColumn(FIELD, sqlType, true);
			default:
				 return createColumn(FIELD, sqlType, false);
			}
		}

	}
	
	
	/*
	 * Set the SQLType used by the user
	 */
	private static SQLType setSQLTypeEnum(final SQLSystem SYSTEM, final Class<?> CLAZZ) {
		switch (SYSTEM) {
		case MY_SQL:
			return SQLMySqlType.getSqlType(CLAZZ);
		default:
			return null;
		}
	}
	
	private static SQLDataDefinition setSQLDataStructure(final SQLSystem SYSTEM) {
		switch (SYSTEM) {
		case  MY_SQL:
			return SQLDataDefinitionImpl.MY_SQL;
		default:
			return null;
		}
	}
	
	private static boolean isEnum(final Field FIELD) {
		return FIELD.getType().isEnum();
	}
	
	private static boolean isUsingKeyword(final String field, SQLDataDefinition dataStructure) {
		SQLKeywords key = (String x, List<String> keys) -> keys.contains(x);
		return key.isUsingSQLKeyword(field.toUpperCase(), dataStructure.keywords());	
	}
	
	
}
