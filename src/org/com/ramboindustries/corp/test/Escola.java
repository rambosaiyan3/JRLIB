package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLColumn;
import org.com.ramboindustries.corp.sql.annotations.SQLForeignKey;
import org.com.ramboindustries.corp.sql.annotations.SQLIdentifier;
import org.com.ramboindustries.corp.sql.annotations.SQLIgnore;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_ESCOLA")
public class Escola {

	@SQLIdentifier(identifierName = "ESCOLA_ID")
	private Long id;

	@SQLColumn(name = "NOME", length = 40, required = true)
	private String nomeEscola;

	@SQLIgnore
	private Integer numeroFuncionarios;

	@SQLForeignKey(name = "DIRETOR_ID", required = true, classReferenced = Pessoa.class)
	private Pessoa diretor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeEscola() {
		return nomeEscola;
	}

	public void setNomeEscola(String nomeEscola) {
		this.nomeEscola = nomeEscola;
	}

	public Integer getNumeroFuncionarios() {
		return numeroFuncionarios;
	}

	public void setNumeroFuncionarios(Integer numeroFuncionarios) {
		this.numeroFuncionarios = numeroFuncionarios;
	}

	public Pessoa getDiretor() {
		return diretor;
	}

	public void setDiretor(Pessoa diretor) {
		this.diretor = diretor;
	}

}
