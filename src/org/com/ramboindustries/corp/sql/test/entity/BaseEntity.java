package org.com.ramboindustries.corp.sql.test.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;

public abstract class BaseEntity {

	@SQLIdentifier(identifierName = "ID")
	protected Long id;

	private Date date;
	private LocalDate date1;
	private LocalDateTime date33;
	
	

	public LocalDateTime getDate33() {
		return date33;
	}

	public void setDate33(LocalDateTime date33) {
		this.date33 = date33;
	}

	public abstract Long getId();

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LocalDate getDate1() {
		return date1;
	}

	public void setDate1(LocalDate date1) {
		this.date1 = date1;
	}
	
	

}
