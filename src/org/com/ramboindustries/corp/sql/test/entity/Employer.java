package org.com.ramboindustries.corp.sql.test.entity;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_EMPLOYER", dropTableIfExists = true)
@SQLInheritancePK(primaryKeyName = "EMPLOYER_ID")
public class Employer extends BaseEntity {

	@SQLColumn(name = "NAME", length = 60, required = true)
	private String name;
	
	@SQLColumn(name = "EMAIL", length = 32, required = true)
	private String email;
	
	@SQLForeignKey(name = "USER_ID", required = true)
	private User user;

	public Employer() {
	}
	
	public Employer(String name, String email, User user) {
		this.name = name;
		this.email = email;
		this.user = user;
	}
	
	public Employer(Long id) {
		super.id = id;
	}
	
	
	@Override
	public Long getId() {
		return super.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
}
