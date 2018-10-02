package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_EMPLOYER")
public class Employer {

	@SQLIdentifier(identifierName = "EMPLOYER_ID")
	private Long id;
	
	
	private Double salary;
	
	@SQLColumn(name = "has_childs")
	private Boolean hasChilds;

	@SQLForeignKey(classReferenced = Company.class, name = "COMPANY_ID", required = true)
	private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Boolean getHasChilds() {
		return hasChilds;
	}

	public void setHasChilds(Boolean hasChilds) {
		this.hasChilds = hasChilds;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	

	

}
