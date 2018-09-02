package org.com.ramboindustries.corp.utils;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.interfaces.PBEKey;

import org.com.ramboindustries.corp.sql.annotations.SqlColumn;
import org.com.ramboindustries.corp.sql.annotations.SqlIgnore;

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
	 */
	public static <E> Map<String, Object> getFieldsValuesFromSQLEntity(E object)
			throws IllegalAccessException, SecurityException {
		Map<String, Object> keyValue = new HashMap<>();
		Field[] objectFields = object.getClass().getDeclaredFields();
		for (Field field : objectFields) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(SqlIgnore.class)) {
				if (field.isAnnotationPresent(SqlColumn.class)) {
					keyValue.put(field.getDeclaredAnnotation(SqlColumn.class).name(), field.get(object));
				} else {
					keyValue.put(field.getName(), field.get(object));
				}
			}
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
	
	public static List<Method> getSettersMethodsFromClass(Class<?> clazz) {
		Method [] methods = clazz.getDeclaredMethods();
		List<Method> setters = new ArrayList<>();
		for(Method x : methods) {
			if(x.getName().startsWith("set") && x.getParameterCount() == 1) {
				setters.add(x);
			}
		}
		return setters;
	}
	
	private static <E> void callSetter(E object, String setterName, Object value)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(setterName, object.getClass());
		propertyDescriptor.getWriteMethod().invoke(object, value);
	}
	
	public static <E> List<JRFieldValue> getFieldValue(E object)
			throws IllegalArgumentException, IllegalAccessException {
		List<JRFieldValue> jrFieldValue = new ArrayList<>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(SqlIgnore.class)) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(SqlColumn.class))
					jrFieldValue.add(new JRFieldValue(field.getAnnotation(SqlColumn.class).name(), field.get(object)));
				else
					jrFieldValue.add(new JRFieldValue(field.getName(), field.get(object)));
				field.setAccessible(false);
			}
		}
		return jrFieldValue;
	}

}



