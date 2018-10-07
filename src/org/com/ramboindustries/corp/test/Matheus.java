package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB+A")
@SQLInheritancePK(primaryKeyName = "")
public class Matheus extends Aluno {


	private String nomeEmprego;
	@SQLForeignKey(required = true, classReferenced = Departamento.class, name = "Departamento_ID")
	private Departamento departamento;
}
