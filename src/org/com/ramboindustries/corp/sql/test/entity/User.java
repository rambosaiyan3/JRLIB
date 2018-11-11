package org.com.ramboindustries.corp.sql.test.entity;

import java.math.BigDecimal;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_USER", dropTableIfExists = true)
@SQLInheritancePK(primaryKeyName = "USER_ID")
public class User extends BaseEntity{

	@SQLColumn(name = "NAME", required = true, length = 60)
	private String name;
	
	@SQLColumn(name = "LOGIN", required = true, length = 32)
	private String login;

	@SQLColumn(name = "PASSWORD", required = true, length = 32)
	private String password;
	
	private BigDecimal salary;
	private Double salary1;
	private Float salary2;
	
	public User() {
		
	}
	
	public User(String name, String login, String password) {
		this.login = login;
		this.name = name;
		this.password = password;
	}
	
	public User(Long id) {
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Double getSalary1() {
		return salary1;
	}

	public void setSalary1(Double salary1) {
		this.salary1 = salary1;
	}

	public Float getSalary2() {
		return salary2;
	}

	public void setSalary2(Float salary2) {
		this.salary2 = salary2;
	}

	
	
}
