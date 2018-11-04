package org.com.ramboindustries.corp.sql.test.entity;

import org.com.ramboindustries.corp.sql.annotations.SQLTableOneToMany;

@SQLTableOneToMany(name = "Teste")
public class TestOneToMany {

	private User user;
	
}
