package org.com.ramboindustries.corp.testClass;

import java.util.Date;

import org.com.ramboindustries.corp.sql.annotations.SqlIgnore;
import org.com.ramboindustries.corp.sql.annotations.SqlTable;
import org.com.ramboindustries.corp.sql.utils.SqlUtils;

public class Person {

	public Person(String nome, int idade) {
		this.nome = nome;
		this.idade = idade;
	}
	
	private String nome;
	private int idade;
	
	
public static void main(String[] args) throws Exception{
		
		Person pe = new Person("Matheus" , 25);
		Company com = new Company("Herval", new Date());
		
		String sql = SqlUtils.createInsertScript("TB_PERSON",pe);
		
		String sql1 = SqlUtils.createInsertScript( com);
		System.out.println(sql);
		System.out.println("\n" + sql1);
		
		
	}
	
}

@SqlTable(table = "TB_C")
class Company {
	
	@SqlIgnore
	private String companyName;
	
	
	private Date dataOfBirth;
	
	public Company(String n, Date a ) {
		this.companyName = n;
		this.dataOfBirth = a;
	}
	
}


