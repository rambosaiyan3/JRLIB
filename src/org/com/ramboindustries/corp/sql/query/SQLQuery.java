package org.com.ramboindustries.corp.sql.query;

import java.util.Optional;

/**
 * 
 * @author matheus_rambo
 *
 */
public interface SQLQuery {

	/**
	 * All subclasses have to implement the method, Oracle has one, MySQL other and
	 * so on...
	 * @param SQL
	 * @param TARGET
	 * @return
	 */
	public <T> Optional<T> createQueryInnerJoin(final String SQL, final Class<T> TARGET);

}
