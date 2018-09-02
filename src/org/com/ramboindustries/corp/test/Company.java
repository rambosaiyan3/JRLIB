package org.com.ramboindustries.corp.test;

import java.util.List;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_COMPANY")
public class Company {

	@SQLColumn(name = "id_company")
	private Long id;
	private String name;
	@SQLIgnore
	private List<Employer> employers;

	public Company(Long id, String name, List<Employer> employers) {
		super();
		this.id = id;
		this.name = name;
		this.employers = employers;
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

	public List<Employer> getEmployers() {
		return employers;
	}

	public void setEmployers(List<Employer> employers) {
		this.employers = employers;
	}

}
