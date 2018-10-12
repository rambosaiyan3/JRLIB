package org.com.ramboindustries.corp.text;

public enum Type {

	BYTE("Byte"),
	SHORT("Short"),
	INTEGER("Integer"),
	LONG("Long"),
	FLOAT("Float"),
	DOUBLE("Double"),
	BIG_DECIMAL("BigDecimal"),
	BOOLEAN("Boolean"),
	STRING("String");
	
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
