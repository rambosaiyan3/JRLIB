package org.com.ramboindustries.corp.utils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.com.ramboindustries.corp.sql.SQLJavaField;
import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;

/**
 * @author kernelpanic_r
 */
public class ObjectAccessUtils {

	/**
	 * Gets the fields from the instance, if the class contains the 
	 * @SqlColumn at a field, it will get the name from the annotation
	 * @param object instance of the class
	 * @return a map that contains the field name and its value
	 * @author kernelpanic_r
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws SQLIdentifierException 
	 */
	public static <E> Map<String, Object> getFieldsValuesFromSQLEntity(E object)
			throws IllegalAccessException, SecurityException, SQLIdentifierException {
		Map<String, Object> keyValue = new HashMap<>();
		Field[] objectFields = object.getClass().getDeclaredFields();
		byte identifier = 0;
		
		for (Field field : objectFields) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(SQLIgnore.class)) {
				if(field.isAnnotationPresent(SQLIdentifier.class)) {
					keyValue.put(field.getDeclaredAnnotation(SQLIdentifier.class).identifierName(), field.get(object));
					++identifier;
				}
				else if (field.isAnnotationPresent(SQLColumn.class)) {
					keyValue.put(field.getDeclaredAnnotation(SQLColumn.class).name(), field.get(object));
				} else {
					keyValue.put(field.getName(), field.get(object));
				}
				field.setAccessible(false);
				if(identifier > 1) {
					throw new SQLIdentifierException();
				}
			}
		}
		if(identifier == 0) {
			throw new SQLIdentifierException("The " + object.getClass().getSimpleName() + " does not have a identifier!");
		}
		return keyValue;
	}
	/**
	 * @author kernelpanic_r
	 * @param clazz class that you want the super classes
	 * @param getObjectClass if you want the Object class
	 * @return a List that contains all the superclass from @param clazz
	 */
	public static List<Class<?>> getSuperclassesFromClass(Class<?> clazz, boolean getObjectClass) {
		List<Class<?>> classes = new ArrayList<>();
		if (clazz.getSimpleName().equals(Object.class.getSimpleName()))
			return classes;
		classes.add(clazz.getSuperclass());
		if (classes.get(0) != null) {
			byte i = 0;
			do {
				classes.add(classes.get(i++).getSuperclass());
			} while (classes.get(i) != null);
			classes.remove(i);
		}
		if (!getObjectClass)
			classes.remove(classes.size() - 1);
		return classes;
	}
		
	public static <E> void callSetter(E object, String setterName, Object value)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(setterName, object.getClass());
		propertyDescriptor.getWriteMethod().invoke(object, value);
	}
	
	public static <E> Object callGetter(String name, E object)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, object.getClass());
		return propertyDescriptor.getReadMethod().invoke(object);
	}
	
	private static <E> SQLJavaField createSqlJavaField(Field field, E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		SQLJavaField sqlJavaField = new SQLJavaField();
		field.setAccessible(true);
		sqlJavaField.setSqlColumn(field.getName());
		if (field.isAnnotationPresent(SQLColumn.class)) {
			sqlJavaField.setSqlColumn(field.getAnnotation(SQLColumn.class).name());
		}
		sqlJavaField.setValue(callGetter(field.getName(), object));
		field.setAccessible(false);
		return sqlJavaField;
	}
	
	public static <E> Set<SQLJavaField> getAllFieldFromClassAndSuperClass(E object, boolean getObjectClass)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		List<Class<?>> classes = getSuperclassesFromClass(object.getClass(), getObjectClass);
		Set<SQLJavaField> allFields = new HashSet<>();
		if (classes != null && !classes.isEmpty()) {
			for (int i = classes.size() -1; i >= 0; i--) {
				Field fields[] = classes.get(i).getDeclaredFields();
				for (Field field : fields) {
					if (!field.isAnnotationPresent(SQLIgnore.class)) {
						allFields.add(createSqlJavaField(field, object));
					}
				}
			}
		}
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(SQLIgnore.class)) {
				allFields.add(createSqlJavaField(field, object));
			}
		}
		return allFields;
	}
	
	
}



