package org.com.ramboindustries.corp.sql.functional;

import java.util.List;

@FunctionalInterface
public interface SQLKeywords {

	/**
	 * Verifies if the user is using a SQLKeyword
	 * @param VALUE
	 * @param KEYWORDS
	 * @return
	 */
	public boolean isUsingSQLKeyword(final String VALUE, final List<String> KEYWORDS);
	
	
}
