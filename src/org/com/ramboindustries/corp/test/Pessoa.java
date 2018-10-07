package org.com.ramboindustries.corp.test;

import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_PESSOA", dropTableIfExists = true)
public class Pessoa {

	@SQLIdentifier(identifierName = "PESSOA_ID")
	private Long id;
	
	@SQLColumn(name = "NOME", required = true, length = 120)
	private String nome;
	
	private Date dataNascimento;
	
	
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
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	
	
	
}
