package org.com.ramboindustries.corp.sql.utils;

import java.sql.SQLException;

/**
 * Simple Log manager for SQLs queries
 * 
 * @author kernelpanic_r
 *
 */
public final class SQLLogger {

	private static final String INIT = "*|-|*|-|*|-|*|-|* INIT > > > > >";
	private static final String END = "< < < < < END *|-|*|-|*|-|*|-|*";
	private static final String MSG = "STARTING THE CONNECTION . . .";
	private static final String ERROR = "AN ERROR OCURRED WHILE EXECUTING THE FOLLOWING SQL SCRIPT > > > > > ";
	private static final String COMMIT_MSG = " > > > > > COMMIT < < < < <  ";
	private static final String ROLLBACK_MSG = " > > > > > ROLLBACK < < < < < ";
	private static final String CANT_COMMIT_OR_ROLLBACK = " >>>>> YOU CANT CALL COMMIT OR ROLLBACK WHEN USING AUTOCOMMIT <<<<< ";
	
	
	public static void initConnection() {
		System.out.println(INIT);
		System.out.println(countSpaces(INIT) + MSG);
		System.out.println(countSpaces(INIT + MSG) + END);
	}

	public static void showScript(final String SQL) {
		System.out.println(INIT);
		System.out.println(countSpaces(INIT) + SQL);
		System.out.println(countSpaces(INIT + SQL) + END);
	}

	public static void showDropTableScript(final String SQL) {
		System.out.println("  INIT PROCESS TO DROP TABLE > > > > > ");
		System.out.println(SQL);
		System.out.println(" < < < < < TABLE DROPPED! \n");
	}

	public static void showCreateTableScript(final String SQL) {
		System.out.println(" INIT PROCESS TO CREATE A NEW TABLE > > > > >");
		System.out.println(SQL);
		System.out.println(" < < < < < A NEW TABLE WAS SUCCESSFULLY CREATED!\n ");
	}

	public static void showException(final String SQL, SQLException ex) {
		System.out.println(ERROR + SQL + " < < < < <");
		System.out.println("Exception name: " + ex.getMessage());
		ex.printStackTrace();
	}

	private static String countSpaces(final String MSG) {
		final short LENGTH = (short) MSG.length();
		StringBuilder b = new StringBuilder();
		for (short i = 0; i < LENGTH; i++) {
			b.append(" ");
		}
		return b.toString();
	}

	public static void showCommit() {
		System.out.println(COMMIT_MSG);
	}
	
	public static void showRollback() {
		System.out.println(ROLLBACK_MSG);
	}

	public static void showCantCommitOrRollback() {
		System.out.println(CANT_COMMIT_OR_ROLLBACK);
	}
	
}
