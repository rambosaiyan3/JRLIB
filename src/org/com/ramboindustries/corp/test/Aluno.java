package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_ALUNO")
public class Aluno extends Pessoa {

	
	@SQLColumn(name = "NOTA", required = true)
	private Double nota;

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}
	
	
}
