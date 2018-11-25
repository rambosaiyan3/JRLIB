package org.com.ramboindustries.corp.sql.annotations.enums;

public enum FetchType {

	FETCH_LAZY(true), FETCH_EAGER(false);

	private FetchType(boolean lazy) {
		this.lazy = lazy;
	}

	private boolean lazy;

	public boolean isLazy() {
		return lazy;
	}

}
