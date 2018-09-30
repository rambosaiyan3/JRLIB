package org.com.ramboindustries.corp.text;

public class JRString {

	
	/**
	 * Returns the position of a character from a text
	 * @param character 
	 * @param text
	 * @return -1 if the character is not at the text
	 */
	public static int getFirstPositionCharacter(char character, String text) {
		return text.indexOf(character);
	}
	
	/**
	 * Returns the position of a character from a text
	 * @param character 
	 * @param text
	 * @return -1 if the character is not at the text
	 */
	public static int getLastPositionCharacter(char character, String text) {
		return text.lastIndexOf(character);
	}
	
	
}
