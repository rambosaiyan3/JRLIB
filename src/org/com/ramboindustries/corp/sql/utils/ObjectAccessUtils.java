package org.com.ramboindustries.corp.sql.utils;


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
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.exceptions.SQLIdentifierException;

/**
 * @author kernelpanic_r
 */
public class ObjectAccessUtils {

	
	/**
	 * @author kernelpanic_r
	 * @param clazz class that you want the super classes
	 * @param getObjectClass if you want the Object class
	 * @return a List that contains all the superclass from @param clazz
	 */
	public static List<Class<?>> getSuperclassesFromClass(Class<?> clazz, boolean getObjectClass) {
		List<Class<?>> classes = new ArrayList<>();
		// if clazz is the Object
		if (clazz.getSimpleName().equals(Object.class.getSimpleName()))
			return classes;
		// we add it superclass to the set
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
	
	
	public static <E, V> V callGetter(String name, E object, Class<V> clazz)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		Object value = callGetter(name, object);
		if (value == null)
			return null;
		if (clazz.isAssignableFrom(Long.class)) {
			return clazz.cast(new Long(value.toString()));
		} else if (clazz.isAssignableFrom(Integer.class)) {
			return clazz.cast(new Integer(value.toString()));
		} else if (clazz.isAssignableFrom(Short.class)) {
			return clazz.cast(new Short(value.toString()));
		} else {
			return null;
		}
	}
	
	private static <E> SQLJavaField createSqlJavaField(Field field, E object)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		SQLJavaField sqlJavaField = new SQLJavaField();


		if (field.isAnnotationPresent(SQLColumn.class)) {
			// if the field its a normal column
			sqlJavaField.setSqlColumn(field.getAnnotation(SQLColumn.class).name());
		} else if(field.isAnnotationPresent(SQLIdentifier.class)){
			// if the field is a PK
			if (object.getClass().isAnnotationPresent(SQLInheritancePK.class)) {
				// if the column has the name of PK defined on class
				sqlJavaField.setSqlColumn(object.getClass().getAnnotation(SQLInheritancePK.class).primaryKeyName());
			} else {
				sqlJavaField.setSqlColumn(field.getAnnotation(SQLIdentifier.class).identifierName());
			}
		}else if(field.isAnnotationPresent(SQLForeignKey.class)) {
			// if the field is a FK
			sqlJavaField.setSqlColumn(field.getAnnotation(SQLForeignKey.class).name());
		}else {
			// the field has no annotation
			sqlJavaField.setSqlColumn(field.getName());
		}
		
		if (field.isAnnotationPresent(SQLForeignKey.class)) {
			// gets the class type of the PK 
			
			Object obj = ObjectAccessUtils.<E>callGetter(field.getName(), object);
			sqlJavaField.setValue(SQLClassHelper.getPrimaryKeyValue(obj));
						
		} else {
			sqlJavaField.setValue(ObjectAccessUtils.<E>callGetter(field.getName(), object));
		}
		

		return sqlJavaField;
	}
	
	public static <E> Set<SQLJavaField> getAllFieldFromClassAndSuperClass(E object, boolean getObjectClass)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		List<Class<?>> classes = getSuperclassesFromClass(object.getClass(), getObjectClass);
		Set<SQLJavaField> allFields = new HashSet<>();
	
		// the fields of the super classes
		if (classes != null && !classes.isEmpty()) {
			for (int i = classes.size() -1; i >= 0; i--) {
				Field fields[] = classes.get(i).getDeclaredFields();
				for (Field field : fields) {
					if (!field.isAnnotationPresent(SQLIgnore.class)) {
						// if the field is not ignorable for sql
						allFields.add(ObjectAccessUtils.<E>createSqlJavaField(field, object));
					}
				}
			}
		}
		
		//the fields that the was declared from clazz
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(SQLIgnore.class)) {
				allFields.add(ObjectAccessUtils.<E>createSqlJavaField(field, object));
			}
		}
		return allFields;
	}
	
	public static <E> E initObject(Class<E> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
	
	
	
		
	
	
}



