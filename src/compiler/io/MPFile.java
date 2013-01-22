package compiler.io;

import java.util.HashMap;

/**
 * Represents the micropascal file by containing a mapping from line number to
 * the actual line
 */
public class MPFile {

	private HashMap<Integer, Line> lines = new HashMap<Integer, Line>();
	private int curLine; // 1-based
	private int curCol; // 1-based

	/**
	 * Adds a line to the file and maps it to the next line number
	 * 
	 * @param line
	 */
	public void addLine(Line line) {
		lines.put(lines.size() + 1, line);
	}

	public Line getLine(int number) {
		this.getNextChar(true, false);
		return lines.get(number);
	}

	/**
	 * Gets the next character in the file. Advances the column and/or line
	 * indices. Will return a EOF if it reaches the end of the file.
	 * 
	 * @param skipWhiteSpace
	 *            - if true the next character ignores whitespace since the
	 *            previous character.
	 * @param lineWrap
	 *            if true if necessary it will check new line(s).
	 * 
	 * @return the next character in the file
	 * @throws NoCharException
	 *             if there are no matching characters left and lineWrap is
	 *             false, should not be thrown at the end of the file
	 */
	public char getNextChar(boolean skipWhiteSpace, boolean lineWrap)
			throws NoCharException {
		// Line tmpLine = lines.get(curLine);
		// int tmpIdx = tmpLine.getNextCharIdx(curCol - 1, skipWhiteSpace);
		// char tmpChar = tmpLine.getChar(tmpIdx);
		// TODO: add line wrapping
		// curCol = tmpIdx + 1 //convert index to 1 based
		return ' ';
	}

	/**
	 * Returns if the file contains more characters. Does not advance the line
	 * or column indices.
	 * 
	 * @param skipWhiteSpace
	 *            if true the next character ignores whitespace since the
	 *            previous character.
	 * @param lineWrap
	 *            if true if necessary it will check new line(s).
	 * 
	 * @return true if and only if there are characters left matching the
	 *         parameters
	 */
	public boolean hasNextChar(boolean skipWhiteSpace, boolean lineWrap) {
		// Line tmpLine = lines.get(curLine);
		// int tmpIdx = tmpLine.getNextCharIdx(curCol, skipWhiteSpace);
		// TODO: add line wrapping
		// return true
		return false;
	}

	/**
	 * Peeks at the next character in the file. Does not advance the line or
	 * column indices. Will return a EOF if it reaches the end of the file.
	 * 
	 * @param skipWhiteSpace
	 *            if true the next character ignores whitespace since the
	 *            previous character.
	 * @param lineWrap
	 *            if true if necessary it will check new line(s).
	 * 
	 * @return the next character in the file.
	 * @throws NoCharException
	 *             if there are no matching characters left and lineWrap is
	 *             false, is not thrown at end of the file
	 */
	public char peek(boolean skipWhiteSpace, boolean lineWrap) {
		// Line tmpLine = lines.get(curLine);
		// int tmpIdx = tmpLine.getNextCharIdx(curCol, skipWhiteSpace);
		// TODO: add line wrapping
		// return tmpLine.getChar(tmpIdx);
		return ' ';
	}
}
