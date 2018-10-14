package org.com.ramboindustries.corp.sql.utils;

public final class SQLLogger {

	private final  String INIT = "*|-|*|-|*|-|*|-|* INIT > > > > >";
	private final  String END = "< < < < < END *|-|*|-|*|-|*|-|*";
	private final  String MSG = "STARTING THE CONNECTION . . .";

	
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
	
	private String countSpaces(final String MSG) {
		final short LENGTH = (short)MSG.length();
		StringBuilder b = new StringBuilder();
		for(short i = 0; i < LENGTH; i++) {
			b.append(" ");
		}
		return b.toString();
	}
	
	
}
