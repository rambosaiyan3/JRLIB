package org.com.ramboindustries.corp.sql;

public class SQLClassColumn {

	private String name;
	private Object type;
	private Integer length;
	private Boolean required;

	public SQLClassColumn() {

	}

	public SQLClassColumn(String name, Object type, Integer length, Boolean required) {
		super();
		this.name = name;
		this.type = type;
		this.length = length;
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

}
