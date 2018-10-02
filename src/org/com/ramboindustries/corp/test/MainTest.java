package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.SQLClassHelper;
import org.com.ramboindustries.corp.sql.utils.SQLUtils;

public class MainTest {

	public static void main(String[] args) throws Exception {
		
		Company company = new Company();
		company.setId(20L);
		
		Employer employer = new Employer();
		employer.setCompany(company);
		employer.setSalary(1500.0);
		
		SQLUtils sql = new SQLUtils();
		
		String companyScript = sql.createTableScript(Company.class);
		String employerScript = sql.createTableScript(Employer.class);
		
		System.out.println(companyScript);
		
		System.out.println("");
		
		System.out.println(employerScript);
		
		System.out.println(SQLClassHelper.<Company, Long>getPrimaryKeyValue(company));
		
	}
		

}
