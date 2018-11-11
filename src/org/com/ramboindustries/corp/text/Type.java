package org.com.ramboindustries.corp.text;

public enum Type {

	BYTE(Byte.class.getSimpleName()),
	SHORT(Short.class.getSimpleName()),
	INTEGER(Integer.class.getSimpleName()),
	LONG(Long.class.getSimpleName()),
	FLOAT(Float.class.getSimpleName()),
	DOUBLE(Double.class.getSimpleName()),
	BIG_DECIMAL(java.math.BigDecimal.class.getSimpleName()),
	BOOLEAN(Boolean.class.getSimpleName()),
	STRING(String.class.getSimpleName()),
	DATE(java.util.Date.class.getSimpleName()),
	LOCAL_DATE(java.time.LocalDate.class.getSimpleName()),
	LOCAL_TIME(java.time.LocalTime.class.getSimpleName()),
	LOCAL_DATE_TIME(java.time.LocalDateTime.class.getSimpleName()),
	CHARACTER(Character.class.getSimpleName());
	
	private Type(String type) {
		this.type = type;
	}

	private String type;
	
	
	public String getType() {
		return type;
	}
	
	public static Type getTypeByName(String name) {
		for(Type type : Type.values()) {
			if(type.getType().equalsIgnoreCase(name))return type;
		}
		return null;
	}
}
