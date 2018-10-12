package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_PESSOA", dropTableIfExists = true)
public class Pessoa {

	@SQLIdentifier(identifierName = "PESSOA_ID")
	private Long id;
	
	@SQLColumn(name = "NOME", required = true, length = 120)
	private String nome;
	
	
	

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

	
	
	
	
}
