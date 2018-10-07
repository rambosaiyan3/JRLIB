package org.com.ramboindustries.corp.test;

import java.util.Date;

import org.com.ramboindustries.corp.sql.abstracts.SQLConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConstants;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		
		SQLConnection connection = new SQLConnection(SQLMySQLConstants.URL_LOCALHOST + "teste", "root", "");
		connection.createConnection();
		
		Pessoa pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Matheus Felipe Rambo");
		pessoa.setDataNascimento(null);
		
		Pessoa pessoa1 = new Pessoa();
		pessoa1.setId(3L);
		pessoa1.setNome("Felipe Rambo");
		pessoa1.setDataNascimento(null);
		
		Departamento departamento = new Departamento();
		departamento.setNome("Desenvolvimento");
		departamento.setSigla("DEV");
		departamento.setLider(pessoa);
		
		Escola escola = new Escola();
		escola.setDiretor(pessoa1);
		escola.setEscolaId(1L);
		escola.setNomeEscola("Primavera");
		escola.setNumeroFuncionarios(20);
		
		Aluno aluno = new Aluno();

		
		SQLUtils sqlUtils = new SQLUtils();
		
//		String scriptDepartamento = sqlUtils.createTableScript(Departamento.class);
		String scriptInsertPessoa = sqlUtils.<Pessoa>createInsertScriptSQL(pessoa);
		String scriptInsertDepartamento = sqlUtils.<Departamento>createInsertScriptSQL(departamento);
		String scriptInsertEscola= sqlUtils.<Escola>createInsertScriptSQL(escola);
		String scriptAluno = sqlUtils.createTableScript(Aluno.class);
		
		System.out.println(scriptAluno);
		
	//	System.out.println(scriptDepartamento);
	//	System.out.println(scriptInsertPessoa);
//		System.out.println(scriptInsertDepartamento);
	//	System.out.println(scriptInsertEscola);
	
		//connection.createPreparedStatement(scriptInsertPessoa);
	//	connection.executeSQL();
	//	connection.closePreparedStatement();
		

	//	connection.createPreparedStatement(scriptInsertDepartamento);
	//	connection.executeSQL();
	//	connection.closePreparedStatement();
		

	//	connection.createPreparedStatement(scriptInsertEscola);
	//	connection.executeSQL();
	//	connection.closePreparedStatement();
		
		
		
		/*
		connection.createPreparedStatement(scriptDepartamento);
		connection.executeSQL();
		connection.closePreparedStatement();
		
		connection.createPreparedStatement(scriptEscola);
		connection.executeSQL();
		connection.closePreparedStatement();
	*/
	}
		

}
