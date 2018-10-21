package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.utils.SQLScripts;

public class MainTest {

	public static void main(String[] args) throws Exception {

		SQLScripts ll = new SQLScripts();
		System.out.println(ll.createTableScript(Aluno.class));
		
		

		
	}


}