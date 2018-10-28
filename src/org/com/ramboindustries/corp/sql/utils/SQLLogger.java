package org.com.ramboindustries.corp.sql.utils;

/**
 * Simple Log manager for SQLs queries
 * @author kernelpanic_r
 *
 */
public final class SQLLogger {

	private final  String INIT = "*|-|*|-|*|-|*|-|* INIT > > > > >";
	private final  String END = "< < < < < END *|-|*|-|*|-|*|-|*";
	private final  String MSG = "STARTING THE CONNECTION . . .";
	private final  String ERROR = "AN ERROR OCURRED WHILE EXECUTING THE FOLLOWING SQL SCRIPT > > > > > ";
	
	
	public void initConnection() {
		System.out.println(INIT);
		System.out.println(this.countSpaces(INIT) + MSG);
		System.out.println(this.countSpaces(INIT + MSG) + END);
	}
	
	public void showScript(final String SQL) {
		System.out.println(INIT);
		System.out.println(this.countSpaces(INIT) + SQL);
		System.out.println(this.countSpaces(INIT + SQL) + END);
	}
	
	public void showDropTableScript(final String SQL) {
		System.out.println("  INIT PROCESS TO DROP TABLE > > > > > ");
		System.out.println(SQL);
		System.out.println(" < < < < < TABLE DROPPED! \n");
	}
	
	public void showCreateTableScript(final String SQL) {
		System.out.println(" INIT PROCESS TO CREATE A NEW TABLE > > > > >");
		System.out.println(SQL);
		System.out.println(" < < < < < A NEW TABLE WAS SUCCESSFULLY CREATED!\n ");
	}
	
	public void showException(final String SQL) {
		System.out.println(ERROR + SQL + " < < < < <");
	}
	
	private String countSpaces(final String MSG) {
		final short LENGTH = (short)MSG.length();
		StringBuilder b = new StringBuilder();
		for(short i = 0; i < LENGTH; i++) {
			b.append(" ");
		}
		return b.toString();
	}
	
	
}
