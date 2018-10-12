package org.com.ramboindustries.corp.sql;

import java.lang.reflect.Field;
import java.util.List;

public final class SQLCaptureData {

	private String select;
	private List<Field> columns;

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<Field> getColumns() {
		return columns;
	}

	public void setColumns(List<Field> columns) {
		this.columns = columns;
	}

}
