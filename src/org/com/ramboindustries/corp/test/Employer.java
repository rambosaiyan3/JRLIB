package org.com.ramboindustries.corp.test;

import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_EMPLOYER")
public class Employer extends Person {

	private Double salary;
	@SQLColumn(name = "has_childs")
	private Boolean hasChilds;

	public Employer(Long id, String name, Date dateOfBirth, Double salary, Boolean hasChilds) {
		super(id, name, dateOfBirth);
		this.salary = salary;
		this.hasChilds = hasChilds;
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

}
