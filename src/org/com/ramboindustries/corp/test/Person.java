package org.com.ramboindustries.corp.test;

import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;

public abstract class Person {

	@SQLIdentifier(identifierName = "PERSON_ID")
	private Long id;
	
	@SQLColumn(name = "NAME", length = 50, required = true)
	private String name;
	
	@SQLColumn(name = "DATa", required = false)
	private Date dateOfBirth;

	@SQLForeignKey(classReferenced = Company.class, name  =  "COMPANY_ID")
	private Company companyId;
	
	
	public Person(Long id, String name, Date dateOfBirth) {
		super();
		this.id = id;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Company getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}

	
}
