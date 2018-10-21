package org.com.ramboindustries.corp.test;

import org.com.ramboindustries.corp.sql.annotations.SQLInheritancePK;
import org.com.ramboindustries.corp.sql.annotations.SQLTable;

@SQLTable(table = "TB_DEPARTAMENTO", dropTableIfExists = true)
@SQLInheritancePK(primaryKeyName = "DEE")
public class Departamento extends Pessoa {

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
}
