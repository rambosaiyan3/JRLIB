package org.com.ramboindustries.corp.utils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.com.ramboindustries.corp.sql.annotations.SqlColumn;
import org.com.ramboindustries.corp.sql.annotations.SqlIgnore;

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
	
	public static <E> List<Class<?>> getSuperclassesFromClass(E object, boolean getObjectClass){
		List<Class<?>> classes = new ArrayList<>();
		if(object.getClass().getSimpleName().equals(Object.class.getSimpleName()))return classes;
		classes.add(object.getClass().getSuperclass());
		if(classes.get(0) != null) {
			int i = 0;
			while(true) {
				if(classes.get(i) != null) {
				classes.add(classes.get(i).getSuperclass());
					i++;
				}else {
					classes.remove(i);
					break;
				}
			}
		}
		if(!getObjectClass)classes.remove(classes.size() - 1);
		return classes;
	}
}




