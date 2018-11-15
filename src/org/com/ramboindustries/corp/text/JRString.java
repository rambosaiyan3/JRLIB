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
	
	/**
	 * Count how occurrences the text has with the character
	 * @param character
	 * @param text
	 * @param index
	 * @param times
	 * @return
	 */
	public static int countCharacterOccurrences(char character, String text, int index, int times) {
		if(index >= text.length()) {
			return times;
		}
		times += character == text.charAt(index) ? 1 : 0;
		return countCharacterOccurrences(character, text, index + 1, times);
	}
	
	
}
