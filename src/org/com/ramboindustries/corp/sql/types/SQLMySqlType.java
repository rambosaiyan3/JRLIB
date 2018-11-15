package org.com.ramboindustries.corp.sql.types;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public enum SQLMySqlType implements SQLType {

	BYTE(Byte.class) {
		@Override
		public String getSqlType() {
			return "TINYINT";
		}

		@Override
		public Integer defaultSize() {
			return 4;
		}
	},
	SHORT(Short.class) {
		@Override
		public String getSqlType() {
			return "SMALLINT";
		}

		@Override
		public Integer defaultSize() {
			return 6;
		}
	},
	INT(Integer.class) {
		@Override
		public String getSqlType() {
			return "INT";
		}

		@Override
		public Integer defaultSize() {
			return 11;
		}
	},
	LONG(Long.class) {
		@Override
		public String getSqlType() {
			return "BIGINT";
		}

		@Override
		public Integer defaultSize() {
			return 20;
		}
	},
	FLOAT(Float.class) {
		@Override
		public String getSqlType() {
			return "FLOAT";
		}

		@Override
		public Integer defaultSize() {
			return 9;
		}
	},
	DECIMAL(BigDecimal.class) {
		@Override
		public String getSqlType() {
			return "DECIMAL";
		}

		@Override
		public Integer defaultSize() {
			return 10;
		}
	},
	DOUBLE(Double.class) {
		@Override
		public String getSqlType() {
			return "DOUBLE";
		}

		@Override
		public Integer defaultSize() {
			return 19;
		}
	},
	STRING(String.class) {
		@Override
		public String getSqlType() {
			return "VARCHAR";
		}

		@Override
		public Integer defaultSize() {
			return 32;
		}
	},
	CHAR(Character.class) {
		@Override
		public String getSqlType() {
			return "CHAR";
		}

		@Override
		public Integer defaultSize() {
			return 3;
		}

	},
	BOOLEAN(Boolean.class) {
		@Override
		public String getSqlType() {
			return "BOOLEAN";
		}

		@Override
		public Integer defaultSize() {
			return null;
		}
	},
	DATE(Date.class) {
		@Override
		public String getSqlType() {
			return "DATE";
		}

		@Override
		public Integer defaultSize() {
			return null;
		}
	},
	LOCAL_DATE(LocalDate.class) {
		@Override
		public String getSqlType() {
			return "DATE";
		}

		@Override
		public Integer defaultSize() {
			return null;
		}
	},
	LOCAL_TIME(LocalTime.class) {
		@Override
		public String getSqlType() {
			return "TIME";
		}

		@Override
		public Integer defaultSize() {
			return null;
		}
	},
	LOCAL_DATE_TIME(LocalDateTime.class) {
		@Override
		public String getSqlType() {
			return "DATETIME";
		}

		@Override
		public Integer defaultSize() {
			return null;
		}
	};

	private SQLMySqlType(Class<?> clazz) {
		this.clazz = clazz;
	}

	private Class<?> clazz;

	public static SQLMySqlType getSqlType(Class<?> clazz) {
		for (SQLMySqlType type : SQLMySqlType.values())
			if (type.clazz.equals(clazz))
				return type;
		return null;
	}
}
