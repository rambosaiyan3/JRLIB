package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;

public abstract class Pessoa {

	@SQLIdentifier(identifierName = "PESSOA_ID")
	protected Long id;

	public abstract Long getId();

	public void setId(Long id) {
		this.id = id;
	}

	
	
	
	
	
}
