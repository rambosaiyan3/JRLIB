package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_DEPARTAMENTO", dropTableIfExists = true)
public class Departamento {
	
	@SQLIdentifier(identifierName = "DEPARTAMENTO_ID")
	private Long id;
	
	@SQLColumn(name = "NOME", length = 120, required = true)
	private String nome;
	
	@SQLColumn(name = "SIGLA", length = 3, required = true)
	private String sigla;
	
	@SQLForeignKey(name = "LIDER_ID", required = true, classReferenced = Pessoa.class)
	private Pessoa lider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Pessoa getLider() {
		return lider;
	}

	public void setLider(Pessoa lider) {
		this.lider = lider;
	}

	
	
	
}
