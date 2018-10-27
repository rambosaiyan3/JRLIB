package org.com.ramboindustries.corp.sql.test.entity;

import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;

public abstract class BaseEntity {

	@SQLIdentifier(identifierName = "ID")
	protected Long id;
	
	public abstract Long getId();
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
