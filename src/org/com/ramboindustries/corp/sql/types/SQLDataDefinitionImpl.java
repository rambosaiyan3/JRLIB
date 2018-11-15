package org.com.ramboindustries.corp.sql.types;

import java.util.Objects;

import org.com.ramboindustries.corp.sql.enums.SQLSystem;

public enum SQLDataDefinitionImpl implements SQLDataStructure {

	MY_SQL(SQLSystem.MY_SQL) {

		@Override
		public String increment() {
			return " AUTO_INCREMENT ";
		}
	},
	SQL_SERVER(SQLSystem.SQL_SERVER) {

		@Override
		public String increment() {
			return " IDENTITY ";
		}

	};

	private SQLSystem system;

	private SQLDataDefinitionImpl(SQLSystem system) {
		this.system = system;
	}

	public static SQLDataDefinitionImpl getDataDefinition(SQLSystem system) {
		if (Objects.isNull(system))
			return null;
		for (SQLDataDefinitionImpl data : SQLDataDefinitionImpl.values())
			if (data.system == system)
				return data;
		return null;
	}

}
