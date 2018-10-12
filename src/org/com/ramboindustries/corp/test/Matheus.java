package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_MATEHUS", dropTableIfExists = false)
@SQLInheritancePK(primaryKeyName = "Matheus_ID")
public class Matheus extends Aluno {


	@SQLForeignKey(required = true, classReferenced = Departamento.class, name = "Departamento_ID")
	private Departamento departamento;

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@Override
	public String toString() {
		return "Matheus [departamento=" + departamento + ", getNota()=" + getNota() + ", getId()=" + getId()
				+ ", getNome()=" + getNome() + ", toString()=" + super.toString() + "]";
	}

	

	
	
	
}
