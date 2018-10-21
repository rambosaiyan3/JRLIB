package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_ALUNO")
@SQLInheritancePK(primaryKeyName = "ALUNO_ID")
public class Aluno extends Pessoa {

	@Override
	public Long getId() {
		return null;
	}

	@SQLForeignKey(classReferenced = Departamento.class, name = "DE")
	private Departamento de;

	public Departamento getDe() {
		return de;
	}

	public void setDe(Departamento de) {
		this.de = de;
	}
	
	
	
}
