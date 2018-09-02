package org.com.ramboindustries.corp.utils;

import java.util.Date;

import org.com.ramboindustries.corp.sql.utils.SqlUtils;

public class Teste {

	
	public static void main(String[] args)  throws Exception{
		
		SqlUtils sql = new SqlUtils();
		Diretor f = new Diretor();
		f.setIdade(21);
		f.setNome("Matheus");
		f.setSalario(2500.0);
		f.setData(new Date());
		f.setTemFilhos(false);
		System.out.println(sql.createInsertScriptSQL(f));
		
		
	}
	
}


class Pessoa {
	
	private int id;
	private String nome;
	private int idade;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
	
	
}

class Funcionario extends Pessoa {
	
	private Double salario;

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}
	
}

class Diretor extends Funcionario {
	
	private Date data;
	private boolean temFilhos;
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public boolean isTemFilhos() {
		return temFilhos;
	}
	public void setTemFilhos(boolean temFilhos) {
		this.temFilhos = temFilhos;
	}
	
}
