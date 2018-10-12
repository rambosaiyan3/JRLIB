package org.com.ramboindustries.corp.test;

import java.util.ArrayList;
import java.util.List;

import org.com.ramboindustries.corp.sql.SQLConditionType;
import org.com.ramboindustries.corp.sql.SQLWhereCondition;
import org.com.ramboindustries.corp.sql.abstracts.SQLConnection;
import org.com.ramboindustries.corp.sql.abstracts.SQLMySQLConstants;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		
		SQLConnection connection = new SQLConnection(SQLMySQLConstants.URL_LOCALHOST + "teste", "root", "");

		
		Pessoa pessoa = new Pessoa();
	//	pessoa.setId(1L);
		pessoa.setNome("Matheus Felipe Rambo");
		
		Pessoa pessoa1 = new Pessoa();
	//	pessoa1.setId(3L);
		pessoa1.setNome("Felipe Rambo");
		
		Departamento departamento = new Departamento();
		departamento.setNome("Desenvolvimento");
		departamento.setSigla("DEV");
		departamento.setLider(pessoa);
		
		Escola escola = new Escola();
		escola.setDiretor(pessoa1);
		escola.setEscolaId(1L);
		escola.setNomeEscola("Primavera");
		escola.setNumeroFuncionarios(20);
		

		
		SQLUtils sqlUtils = new SQLUtils();
		
//		String scriptDepartamento = sqlUtils.createTableScript(Departamento.class);
		String scriptInsertPessoa = sqlUtils.<Pessoa>createInsertScriptSQL(pessoa);
		String scriptInsertDepartamento = sqlUtils.<Departamento>createInsertScriptSQL(departamento);
		String scriptInsertEscola= sqlUtils.<Escola>createInsertScriptSQL(escola);
		String scriptAluno = sqlUtils.createTableScript(Matheus.class);
		
		System.out.println(scriptAluno);
		
		List<SQLWhereCondition> co = new ArrayList<>();
		co.add(new SQLWhereCondition("nome", "'Felipe Rambo'", SQLConditionType.EQUAL));
		co.add(new SQLWhereCondition("nota", "5200", SQLConditionType.GREATER_THAN_OR_EQUAL));
		
		System.out.println(sqlUtils.createSQLSelectScript(Matheus.class, co));

		
	}
		

}
